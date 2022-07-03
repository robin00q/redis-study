package me.sjlee.redis_study.learn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OperationLearningTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    void set_and_get() {
        // given
        String key = "redisbook2";
        String value = "Hello Redis";

        // when
        redisTemplate.opsForValue()
                .set(key, value);

        // then
        String result = redisTemplate.opsForValue()
                .get(key);

        assertThat(result).isEqualTo(value);
    }

    @Test
    void pipeline_migration_test() {
        int total = 10_000_000;

        long start = System.currentTimeMillis();

        Jedis jedis = new Jedis("localhost", 6379);
        jedis.connect();

        Pipeline p = jedis.pipelined();
        for (int i = 0; i < total; i++) {
            if (i % 1_000_000 == 0) {
                p.sync();
                p = jedis.pipelined();
            }
            String key = String.valueOf("last:" + i);
            String value = key;
            p.set(key, value);
        }
        p.sync();

        jedis.disconnect();

        long end = System.currentTimeMillis();
        long elapsed = end - start;
        System.out.println("초당 처리 건수 : " + total / elapsed * 1000f);
        System.out.println("소요시간 : " + elapsed / 1000f);
    }

}
