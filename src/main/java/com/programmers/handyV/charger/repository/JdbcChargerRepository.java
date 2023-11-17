package com.programmers.handyV.charger.repository;

import com.programmers.handyV.charger.domain.Charger;
import com.programmers.handyV.charger.domain.ChargerStatus;
import com.programmers.handyV.charger.domain.ChargerType;
import com.programmers.handyV.common.utils.JdbcUtils;
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
public class JdbcChargerRepository implements ChargerRepository {
    private static final RowMapper<Charger> chargerRowMapper = (resultSet, i) -> mapToCharger(resultSet);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcChargerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Charger mapToCharger(ResultSet resultSet) throws SQLException {
        UUID chargerId = JdbcUtils.toUUID(resultSet.getBytes("charger_id"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        String chargerTypeName = resultSet.getString("type");
        ChargerType chargerType = ChargerType.findChargerTypeByName(chargerTypeName);
        ChargerStatus chargerStatus = ChargerStatus.valueOf(resultSet.getString("status"));
        LocalDateTime bookedAt = JdbcUtils.toNullableLocalDateTime(resultSet.getTimestamp("booked_at"));
        UUID stationId = JdbcUtils.toUUID(resultSet.getBytes("station_id"));
        UUID userId = JdbcUtils.toNullableUUID(resultSet.getBytes("user_id"));
        return new Charger(chargerId, createdAt, updatedAt, chargerType, chargerStatus, bookedAt, stationId, userId);
    }

    @Override
    public Charger save(Charger charger) {
        String insertSQL = "INSERT INTO chargers(charger_id, created_at, updated_at, "
                + "type, status, booked_at, station_id, user_id)"
                + "VALUES (UUID_TO_BIN(:chargerId), :createdAt, :updatedAt,"
                + ":chargerType, :chargerStatus, :bookedAt, UUID_TO_BIN(:stationId), UUID_TO_BIN(:userId))";
        String updateSQL = "UPDATE chargers "
                + "SET updated_at = :updatedAt, status = :chargerStatus, booked_at = :bookedAt, user_id = UUID_TO_BIN(:userId)"
                + "WHERE charger_id = UUID_TO_BIN(:chargerId)";
        String saveSQL = findById(charger.getChargerId()).isEmpty() ? insertSQL : updateSQL;
        Map<String, Object> parameterMap = toParameterMap(charger);
        int saveCount = jdbcTemplate.update(saveSQL, parameterMap);
        if (saveCount == 0) {
            throw new NoSuchElementException("충전기 저장을 실패했습니다.");
        }
        return charger;
    }

    @Override
    public List<Charger> findAll() {
        String findAllSQL = "SELECT * FROM chargers";
        return jdbcTemplate.query(findAllSQL, chargerRowMapper);
    }

    @Override
    public List<Charger> findByStationId(UUID stationId) {
        String findByStationIdSQL = "SELECT * FROM chargers WHERE station_id = UUID_TO_BIN(:stationId)";
        Map<String, Object> parameterMap = Collections.singletonMap("stationId", stationId.toString().getBytes());
        return jdbcTemplate.query(findByStationIdSQL, parameterMap, chargerRowMapper);
    }

    @Override
    public Optional<Charger> findById(UUID chargerId) {
        String findByIdSQL = "SELECT * FROM chargers WHERE charger_id = UUID_TO_BIN(:chargerId)";
        Map<String, Object> parameterMap = Collections.singletonMap("chargerId", chargerId.toString().getBytes());
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findByIdSQL, parameterMap, chargerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void refreshStatus() {
        String refreshSQL = "UPDATE chargers "
                + "SET updated_at = :now, status = 'AVAILABLE', booked_at = NULL, user_id = NULL "
                + "WHERE status = 'BOOKED' AND booked_at <= :now - INTERVAL 10 MINUTE";
        Map<String, Object> parameterMap = Collections.singletonMap("now", LocalDateTime.now());
        jdbcTemplate.update(refreshSQL, parameterMap);
    }

    private Map<String, Object> toParameterMap(Charger charger) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("chargerId", JdbcUtils.toBinary(charger.getChargerId()));
        parameterMap.put("createdAt", Timestamp.valueOf(charger.getCreatedAt()));
        parameterMap.put("updatedAt", Timestamp.valueOf(charger.getUpdatedAt()));
        parameterMap.put("chargerType", charger.getChargerTypeName());
        parameterMap.put("chargerStatus", charger.getChargerStatus().name());
        parameterMap.put("bookedAt", JdbcUtils.toNullableTimestamp(charger.getBookedAt()));
        parameterMap.put("stationId", JdbcUtils.toBinary(charger.getStationId()));
        parameterMap.put("userId", JdbcUtils.toNullableBinary(charger.getUserId()));
        return Collections.unmodifiableMap(parameterMap);
    }
}
