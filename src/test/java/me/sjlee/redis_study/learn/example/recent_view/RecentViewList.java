package me.sjlee.redis_study.learn.example.recent_view;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecentViewList {

    private static final String KEY_VIEW_LIST = "recent:view:zset:";
    private static final int MAX_LIST_SIZE = 30;
    private final RedisTemplate<String, String> redisTemplate;

    public RecentViewList(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 최근 조회 상품 목록을 추가한다.<br>
     * 최대값을 넘어갈 시, 마지막 상품한개를 제거한다.
     */
    public void add(String userId, String productNo) {
        long nano = System.nanoTime();
        redisTemplate.opsForZSet().add(KEY_VIEW_LIST + userId, productNo, nano);
        int firstIndex = -(MAX_LIST_SIZE + 1);
        redisTemplate.opsForZSet().removeRange(KEY_VIEW_LIST + userId, firstIndex, firstIndex);
    }

    public List<String> getRecentViewList(String userId) {
        return new ArrayList<>(redisTemplate.opsForZSet().reverseRange(KEY_VIEW_LIST + userId, 0, -1));
    }

    public List<String> getRecentViewList(String userId, int cnt) {
        return new ArrayList<>(redisTemplate.opsForZSet().reverseRange(KEY_VIEW_LIST + userId, 0, cnt -1));
    }

    public void removeAll(String userId) {
        redisTemplate.delete(KEY_VIEW_LIST + userId);
    }

    public int getMaxListSize() {
        return MAX_LIST_SIZE;
    }
}
