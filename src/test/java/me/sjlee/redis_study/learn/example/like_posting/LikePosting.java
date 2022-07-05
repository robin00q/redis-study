package me.sjlee.redis_study.learn.example.like_posting;

import org.springframework.data.redis.core.RedisTemplate;

public class LikePosting {

    private static final String KEY_LIKE_SET = "posting:like:";

    private final RedisTemplate<String, String> redisTemplate;

    public LikePosting(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 지정된 게시물의 '좋아요 표시'
     * sadd O(1)
     */
    public Long like(String postingNo, String userId) {
        return redisTemplate.opsForSet().add(KEY_LIKE_SET + postingNo, userId);
    }

    /**
     * 지정된 게시물의 '좋아요 취소'
     * srem O(1)
     */
    public Long unLike(String postingNo, String userId) {
        return redisTemplate.opsForSet().remove(KEY_LIKE_SET + postingNo, userId);
    }

    /**
     * 사용자의 '좋아요 표시여부' 확인
     */
    public boolean isLike(String postingNo, String userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(KEY_LIKE_SET + postingNo, userId));
    }

    /**
     * 게시물에 대한 좋아요 정보를 완전히 삭제한다.
     */
    public boolean deletePostingLikeInfo(String postingNo) {
        return Boolean.TRUE.equals(redisTemplate.delete(KEY_LIKE_SET + postingNo));
    }

    /**
     * 게시물의 좋아요 횟수를 조회한다.
     */
    public Long getLikeCount(String postingNo) {
        return redisTemplate.opsForSet().size(KEY_LIKE_SET + postingNo);
    }

}
