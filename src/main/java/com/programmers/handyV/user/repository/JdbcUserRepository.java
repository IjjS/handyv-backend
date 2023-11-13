package com.programmers.handyV.user.repository;

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

import com.programmers.handyV.common.utils.UUIDConverter;
import com.programmers.handyV.user.domain.CarNumber;
import com.programmers.handyV.user.domain.User;
import com.programmers.handyV.user.domain.UserAuthority;

@Repository
public class JdbcUserRepository implements UserRepository {
    private static final RowMapper<User> userRowMapper = (resultSet, i) -> mapToUser(resultSet);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static User mapToUser(ResultSet resultSet) throws SQLException {
        UUID userId = UUIDConverter.from(resultSet.getBytes("user_id"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        CarNumber carNumber = new CarNumber(resultSet.getString("car_number"));
        UserAuthority userAuthority = UserAuthority.valueOf(resultSet.getString("authority"));
        return new User(userId, createdAt, updatedAt, carNumber, userAuthority);
    }

    @Override
    public User save(User user) {
        String insertSQL = "INSERT INTO users(user_id, created_at, updated_at, car_number, authority) VALUES (UUID_TO_BIN(:userId), :createdAt, :updatedAt, :carNumber, :authority)";
        Map<String, Object> parameterMap = toParameterMap(user);
        int saveCount = jdbcTemplate.update(insertSQL, parameterMap);
        if (saveCount == 0) {
            throw new NoSuchElementException("유저 저장을 실패했습니다.");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        String findAllSQL = "SELECT * FROM users";
        return jdbcTemplate.query(findAllSQL, userRowMapper);
    }

    @Override
    public Optional<User> findByCarNumber(CarNumber carNumber) {
        String findByCarNumberSQL = "SELECT * FROM users WHERE car_number = :carNumber";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findByCarNumberSQL, Collections.singletonMap("carNumber", carNumber.getFullNumber()), userRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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
