package me.sjlee.redis_study.learn.example.like_posting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LikePostingTest {

    @Autowired
    private LikePosting likePosting;

    private String postNo = "12345";
    private String userId = "sjlee";

    @BeforeEach
    void setUp() {
        likePosting.deletePostingLikeInfo(postNo);
    }

    @Test
    void 좋아요_2회_눌러도_한번만() {
        // given: none

        // when
        likePosting.like(postNo, userId);
        likePosting.like(postNo, userId);

        // then
        Long likeCount = likePosting.getLikeCount(postNo);
        assertThat(likeCount).isEqualTo(1);
    }

    @Test
    void 좋아요_누른_횟수_가져오기() {
        // given: none
        List<String> userIds = Arrays.asList(
                "a", "b", "c"
        );

        // when
        userIds.forEach(userId -> likePosting.like(postNo, userId));

        // then
        assertThat(likePosting.getLikeCount(postNo)).isEqualTo(3);

        // when2
        likePosting.unLike(postNo, "a");

        // then2
        assertThat(likePosting.getLikeCount(postNo)).isEqualTo(2);
    }

    @TestConfiguration
    static class Config {

        @Autowired
        RedisTemplate<String, String> redisTemplate;

        @Bean
        public LikePosting likePosting() {
            return new LikePosting(redisTemplate);
        }
    }

}