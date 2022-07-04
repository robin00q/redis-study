package me.sjlee.redis_study.learn.example.visit_count;

import org.springframework.data.redis.core.RedisTemplate;

public class VisitCountV1 implements VisitCount {

    private static final String PAGE_EVENT_KEY_PREFIX = "event:click:";
    private static final String TOTAL_EVENT_KEY_PREFIX = "event:click:total";

    private final RedisTemplate<String, String> redisTemplate;

    public VisitCountV1(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addVisit(String eventId) {
        String key = PAGE_EVENT_KEY_PREFIX + eventId;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.opsForValue().increment(TOTAL_EVENT_KEY_PREFIX);
    }

    @Override
    public int getVisitCount(String eventId) {
        return Integer.parseInt(redisTemplate.opsForValue().get(PAGE_EVENT_KEY_PREFIX + eventId));
    }

    @Override
    public int getTotalVisitCount() {
        return Integer.parseInt(redisTemplate.opsForValue().get(TOTAL_EVENT_KEY_PREFIX));
    }
}
