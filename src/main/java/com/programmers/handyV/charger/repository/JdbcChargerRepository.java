package com.programmers.handyV.charger.repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.programmers.handyV.charger.domain.Charger;

@Repository
public class JdbcChargerRepository implements ChargerRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcChargerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Charger save(Charger charger) {
        String insertSQL = "INSERT INTO chargers(charger_id, created_at, updated_at, type, status, booked_at, station_id, user_id) VALUES (UUID_TO_BIN(:chargerId), :createdAt, :updatedAt, :chargerType, :chargerStatus, :bookedAt, UUID_TO_BIN(:stationId), UUID_TO_BIN(:userId))";
        Map<String, Object> parameterMap = toParameterMap(charger);
        int saveCount = namedParameterJdbcTemplate.update(insertSQL, parameterMap);
        if (saveCount == 0) {
            throw new NoSuchElementException("충전기 저장을 실패했습니다.");
        }
        return charger;
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
