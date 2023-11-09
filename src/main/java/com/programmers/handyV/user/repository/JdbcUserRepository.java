package com.programmers.handyV.user.repository;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.programmers.handyV.user.domain.User;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcUserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        String insertSQL = "INSERT INTO users(user_id, created_at, updated_at, car_number, authority) VALUES (UUID_TO_BIN(:userId), :createdAt, :updatedAt, :carNumber, :authority)";
        Map<String, Object> parameterMap = toParameterMap(user);
        int saveCount = namedParameterJdbcTemplate.update(insertSQL, parameterMap);
        if (saveCount == 0) {
            throw new NoSuchElementException("유저 저장을 실패했습니다.");
        }
        return user;
    }

    private Map<String, Object> toParameterMap(User user) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("userId", user.getUserId().toString().getBytes());
        parameterMap.put("createdAt", Timestamp.valueOf(user.getCreatedAt()));
        parameterMap.put("updatedAt", Timestamp.valueOf(user.getUpdatedAt()));
        parameterMap.put("carNumber", user.getCarFullNumber());
        parameterMap.put("authority", user.getAuthority().name());
        return Collections.unmodifiableMap(parameterMap);
    }
}
