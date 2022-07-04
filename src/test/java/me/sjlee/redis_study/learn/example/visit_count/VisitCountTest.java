package me.sjlee.redis_study.learn.example.visit_count;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class VisitCountTest {

    @Autowired
    private VisitCount visitCount;

    @Test
    void test() {
        int before = visitCount.getTotalVisitCount();

        visitCount.addVisit("1234");
        visitCount.addVisit("2345");
        visitCount.addVisit("3456");

        int after = visitCount.getTotalVisitCount();

        assertThat(after - before).isEqualTo(3);
    }

    @TestConfiguration
    static class Config {

        @Autowired
        RedisTemplate<String, String> redisTemplate;

        @Bean
        public VisitCount visitCountWriter() {
            return new VisitCountV2(redisTemplate);
        }
    }
}
