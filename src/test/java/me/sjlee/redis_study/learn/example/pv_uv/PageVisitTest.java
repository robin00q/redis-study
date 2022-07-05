package me.sjlee.redis_study.learn.example.pv_uv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PageVisitTest {

    @Autowired
    private PageVisit pageVisit;

    @BeforeEach
    void setUp() {
        pageVisit.deletePVUV(LocalDate.now());
    }

    @Test
    void 한명이_같은_페이지_두번방문() {
        // given
        int userSeq = 1;
        LocalDate today = LocalDate.now();

        // when
        pageVisit.visit(1);
        pageVisit.visit(1);

        // then
        assertThat(pageVisit.getPVCount(today)).isEqualTo(2);
        assertThat(pageVisit.getUVCount(today)).isEqualTo(1);
    }

    @Test
    void 날짜_사이의_순방문자수_조회() {
        int userSeq = 1;
        List<LocalDate> dates = Arrays.asList(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 2),
                LocalDate.of(2022, 1, 3)
        );

        dates.forEach(date -> pageVisit.deletePVUV(date));
        dates.forEach(date -> pageVisit.visitAtDate(date, userSeq));

        Long betweenUvCount = pageVisit.getBetweenUvCount(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 3));
        assertThat(betweenUvCount).isEqualTo(1);
    }


    @TestConfiguration
    static class Config {

        @Autowired
        private RedisTemplate<String, String> redisTemplate;

        @Bean
        public PageVisit pageVisit() {
            return new PageVisit(redisTemplate);
        }
    }
}
