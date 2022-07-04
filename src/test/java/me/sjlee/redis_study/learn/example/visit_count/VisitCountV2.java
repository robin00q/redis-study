package me.sjlee.redis_study.learn.example.visit_count;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VisitCountV2 implements VisitCount {

    private static final String PAGE_EVENT_KEY_PREFIX = "event:click:";
    private static final String PAGE_EVENT_KEY_HASH_PREFIX = "event:click:hash:";
    private static final String TOTAL_EVENT_KEY_PREFIX = "event:click:total";

    private final RedisTemplate<String, String> redisTemplate;

    public VisitCountV2(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addVisit(String eventId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        redisTemplate.opsForValue().increment(PAGE_EVENT_KEY_PREFIX + eventId);
        redisTemplate.opsForHash().increment(PAGE_EVENT_KEY_HASH_PREFIX + eventId, today, 1);
        redisTemplate.opsForValue().increment(TOTAL_EVENT_KEY_PREFIX);
    }

    @Override
    public int getVisitCount(String eventId) {
        return Integer.parseInt(redisTemplate.opsForValue().get(PAGE_EVENT_KEY_HASH_PREFIX + eventId));
    }

    @Override
    public int getTotalVisitCount() {
        return Integer.parseInt(redisTemplate.opsForValue().get(TOTAL_EVENT_KEY_PREFIX));
    }
}
