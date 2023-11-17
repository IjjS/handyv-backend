package com.programmers.handyV.station.repository;

import com.programmers.handyV.common.utils.JdbcUtils;
import com.programmers.handyV.station.domain.Station;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcStationRepository implements StationRepository {
    private static final RowMapper<Station> stationRowMapper = (resultSet, i) -> mapToStation(resultSet);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcStationRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static Station mapToStation(ResultSet resultSet) throws SQLException {
        UUID stationId = JdbcUtils.toUUID(resultSet.getBytes("station_id"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        String name = resultSet.getString("name");
        return new Station(stationId, createdAt, updatedAt, name);
    }

    @Override
    public Station save(Station station) {
        String insertSQL = "INSERT INTO stations(station_id, created_at, updated_at, name)"
                + "VALUES (UUID_TO_BIN(:stationId), :createdAt, :updatedAt, :name)";
        Map<String, Object> paramaterMap = toParamaterMap(station);
        int saveCount = namedParameterJdbcTemplate.update(insertSQL, paramaterMap);
        if (saveCount == 0) {
            throw new NoSuchElementException("충전소 저장을 실패했습니다.");
        }
        return station;
    }

    @Override
    public List<Station> findAll() {
        String findAllSQL = "SELECT * FROM stations";
        return namedParameterJdbcTemplate.query(findAllSQL, stationRowMapper);
    }

    @Override
    public List<Station> findByPartialName(String partialName) {
        String findPartialNameSQL = "SELECT * FROM stations WHERE name LIKE :partialName";
        Map<String, Object> parameterMap = Collections.singletonMap("partialName", "%" + partialName + "%");
        return namedParameterJdbcTemplate.query(findPartialNameSQL, parameterMap, stationRowMapper);
    }

    @Override
    public Station findById(UUID stationId) {
        String findByIdSQL = "SELECT * FROM stations WHERE station_id = UUID_TO_BIN(:stationId)";
        Map<String, Object> parameterMap = Collections.singletonMap("stationId", stationId.toString().getBytes());
        return namedParameterJdbcTemplate.queryForObject(findByIdSQL, parameterMap, stationRowMapper);
    }

    @Override
    public Optional<Station> findByName(String name) {
        String findByNameSQL = "SELECT * FROM stations WHERE name = :name";
        try {
            Map<String, Object> parameterMap = Collections.singletonMap("name", name);
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(findByNameSQL, parameterMap, stationRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Map<String, Object> toParamaterMap(Station station) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("stationId", JdbcUtils.toBinary(station.getStationId()));
        parameterMap.put("createdAt", Timestamp.valueOf(station.getCreatedAt()));
        parameterMap.put("updatedAt", Timestamp.valueOf(station.getUpdatedAt()));
        parameterMap.put("name", station.getName());
        return Collections.unmodifiableMap(parameterMap);
    }
}
