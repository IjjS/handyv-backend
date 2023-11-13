package com.programmers.handyV.charger.repository;

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

import com.programmers.handyV.charger.domain.Charger;
import com.programmers.handyV.charger.domain.ChargerStatus;
import com.programmers.handyV.charger.domain.ChargerType;
import com.programmers.handyV.common.utils.UUIDConverter;

@Repository
public class JdbcChargerRepository implements ChargerRepository {
    private static final RowMapper<Charger> chargerRowMapper = (resultSet, i) -> mapToCharger(resultSet);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcChargerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Charger mapToCharger(ResultSet resultSet) throws SQLException {
        UUID chargerId = UUIDConverter.from(resultSet.getBytes("charger_id"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        String chargerTypeName = resultSet.getString("type");
        ChargerType chargerType = ChargerType.findChargerTypeByName(chargerTypeName);
        ChargerStatus chargerStatus = ChargerStatus.valueOf(resultSet.getString("status"));
        LocalDateTime bookedAt = resultSet.getTimestamp("booked_at") != null ? resultSet.getTimestamp("booked_at").toLocalDateTime() : null;
        UUID stationId = UUIDConverter.from(resultSet.getBytes("station_id"));
        UUID userId = resultSet.getBytes("user_id") != null ? UUIDConverter.from(resultSet.getBytes("user_id")) : null;
        return new Charger(chargerId, createdAt, updatedAt, chargerType, chargerStatus, bookedAt, stationId, userId);
    }

    @Override
    public Charger save(Charger charger) {
        String insertSQL = "INSERT INTO chargers(charger_id, created_at, updated_at, type, status, booked_at, station_id, user_id) VALUES (UUID_TO_BIN(:chargerId), :createdAt, :updatedAt, :chargerType, :chargerStatus, :bookedAt, UUID_TO_BIN(:stationId), UUID_TO_BIN(:userId))";
        String updateSQL = "UPDATE chargers SET updated_at = :updatedAt, status = :chargerStatus, booked_at = :bookedAt, user_id = UUID_TO_BIN(:userId) WHERE charger_id = UUID_TO_BIN(:chargerId)";
        String saveSQL = findById(charger.getChargerId()).isEmpty() ? insertSQL : updateSQL;
        Map<String, Object> parameterMap = toParameterMap(charger);
        int saveCount = jdbcTemplate.update(saveSQL, parameterMap);
        if (saveCount == 0) {
            throw new NoSuchElementException("충전기 저장을 실패했습니다.");
        }
        return charger;
    }

    @Override
    public List<Charger> findByStationId(UUID stationId) {
        String findByStationIdSQL = "SELECT * FROM chargers WHERE station_id = UUID_TO_BIN(:stationId)";
        return jdbcTemplate.query(findByStationIdSQL, Collections.singletonMap("stationId", stationId.toString().getBytes()), chargerRowMapper);
    }

    @Override
    public Optional<Charger> findById(UUID chargerId) {
        String findByIdSQL = "SELECT * FROM chargers WHERE charger_id = UUID_TO_BIN(:chargerId)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findByIdSQL, Collections.singletonMap("chargerId", chargerId.toString().getBytes()), chargerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void refreshStatus() {
        String refreshSQL = "UPDATE chargers SET status = 'AVAILABLE', booked_at = NULL WHERE booked_at IS NOT NULL AND booked_at <= now() - INTERVAL 10 MINUTE";
        jdbcTemplate.update(refreshSQL, Collections.emptyMap());
    }

    private Map<String, Object> toParameterMap(Charger charger) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("chargerId", charger.getChargerId().toString().getBytes());
        parameterMap.put("createdAt", Timestamp.valueOf(charger.getCreatedAt()));
        parameterMap.put("updatedAt", Timestamp.valueOf(charger.getUpdatedAt()));
        parameterMap.put("chargerType", charger.getChargerTypeName());
        parameterMap.put("chargerStatus", charger.getChargerStatus().name());
        parameterMap.put("bookedAt", charger.getBookedAt() != null ? Timestamp.valueOf(charger.getBookedAt()) : null);
        parameterMap.put("stationId", charger.getStationId().toString().getBytes());
        parameterMap.put("userId", charger.getUserId() != null ? charger.getUserId().toString().getBytes() : null);
        return Collections.unmodifiableMap(parameterMap);
    }
}
