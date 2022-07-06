package me.sjlee.redis_study.learn.example.recent_view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RecentViewListTest {

    @Autowired private RecentViewList recentViewList;

    private String userId = "123456";

    @BeforeEach
    void setUp() {
        recentViewList.removeAll(userId);
    }

    @Test
    void testAdd() {
        for (int i = 1 ; i <= 50 ; i++) {
            recentViewList.add(userId, String.valueOf(i));
        }

        List<String> results = this.recentViewList.getRecentViewList(userId);
        assertThat(results.size()).isEqualTo(recentViewList.getMaxListSize());
    }

    @Test
    void getRecentView() {
        for (int i = 1 ; i <= 50 ; i++) {
            recentViewList.add(userId, String.valueOf(i));
        }

        List<String> results = this.recentViewList.getRecentViewList(userId, 4);
        assertThat(results.size()).isEqualTo(4);
        assertThat(results).contains("50", "49", "48", "47");
    }



    @TestConfiguration
    static class Config {
        @Autowired
        private RedisTemplate<String, String> redisTemplate;

        @Bean
        public RecentViewList recentViewList() {
            return new RecentViewList(redisTemplate);
        }
    }
}
