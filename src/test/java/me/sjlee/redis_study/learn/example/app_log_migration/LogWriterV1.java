package me.sjlee.redis_study.learn.example.app_log_migration;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogWriterV1 implements LogWriter {

    private final RedisTemplate redisTemplate;

    private static final String KEY_WAS_LOG = "was:log";

    LogWriterV1(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void log(String log) {
        DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        redisTemplate.opsForValue()
                .append(KEY_WAS_LOG, LocalDateTime.now().format(format) + log + "\n");
    }
}
