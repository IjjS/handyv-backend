package com.programmers.handyV.common.utils;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class JdbcUtils {
    private JdbcUtils() {
    }

    public static UUID toUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static UUID toNullableUUID(byte[] bytes) {
        return bytes == null ? null : JdbcUtils.toUUID(bytes);
    }

    public static byte[] toBinary(UUID uuid) {
        return uuid.toString().getBytes();
    }

    public static byte[] toNullableBinary(UUID uuid) {
        return uuid == null ? null : JdbcUtils.toBinary(uuid);
    }

    public static LocalDateTime toNullableLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static Timestamp toNullableTimestamp(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }
}
