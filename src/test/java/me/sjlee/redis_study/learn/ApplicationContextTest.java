package me.sjlee.redis_study.learn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ApplicationContextTest {

    @Autowired ApplicationContext applicationContext;

    @Test
    void lettuceClientAutoConfiguration() {
        RedisConnectionFactory redisConnectionFactoryBean = applicationContext.getBean(RedisConnectionFactory.class);
        assertNotNull(redisConnectionFactoryBean);

        // StringRedisTemplate, RedisTemplate
        // AutoConfiguration 으로 인해, 2개의 레디스 템플릿이 등록된다.
        Map<String, RedisTemplate> beansOfType = applicationContext.getBeansOfType(RedisTemplate.class);
        assertNotNull(beansOfType);
        assertThat(beansOfType.size()).isEqualTo(2);
        assertTrue(beansOfType.containsKey("stringRedisTemplate"));
        assertTrue(beansOfType.containsKey("redisTemplate"));
    }
}
