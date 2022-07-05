package me.sjlee.redis_study.learn.example.pv_uv;

import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PageVisit {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY_PAGE_VIEW = "page:view:";
    private static final String KEY_UNIQUE_VISITOR = "unique:visitor:";
    private static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");

    public PageVisit(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 특정 사용자의 순 방문횟수, 누적 방문횟수 증가
     */
    public void visit(int userSequence) {
        visitAtDate(LocalDate.now(), userSequence);
    }

    public void visitAtDate(LocalDate date, int userSequence) {
        String now = date.format(yyyyMMdd);
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            ((StringRedisConnection) connection).incr(KEY_PAGE_VIEW + now);
            ((StringRedisConnection) connection).setBit(KEY_UNIQUE_VISITOR + now, userSequence, true);
            return null;
        });
    }

    /**
     * 요청된 날짜의 누적 방문자 수 조회
     */
    public Long getPVCount(LocalDate date) {
        String now = date.format(yyyyMMdd);
        return Long.valueOf(redisTemplate.opsForValue().get(KEY_PAGE_VIEW + now));

    }

    /**
     * 요청된 날짜의 순 방문자수 조회
     */
    public Long getUVCount(LocalDate date) {
        String now = date.format(yyyyMMdd);
        return redisTemplate.execute(
                (RedisCallback<Long>) connection -> ((StringRedisConnection) connection).bitCount(KEY_UNIQUE_VISITOR + now)
        );
    }

    /**
     * 주어진기간 매일 방문한 순 방문자수 조회
     */
    public Long getBetweenUvCount(LocalDate start, LocalDate end) {

        int diff = Period.between(start, end).getDays();
        String keys[] = new String[diff + 1];
        for (int i = 0 ; i < keys.length ; i++) {
            keys[i] = KEY_UNIQUE_VISITOR + start.plusDays(i).format(yyyyMMdd);
        }

        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            ((StringRedisConnection) connection).bitOp(RedisStringCommands.BitOperation.AND, "target:uv", keys);
            return ((StringRedisConnection) connection).bitCount("target:uv");
        });
    }

    public void deletePVUV(LocalDate date) {
        String now = LocalDate.now().format(PageVisit.yyyyMMdd);
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            ((StringRedisConnection) connection).del(KEY_PAGE_VIEW + now);
            ((StringRedisConnection) connection).del(KEY_UNIQUE_VISITOR + now);
            return null;
        });
    }
}
