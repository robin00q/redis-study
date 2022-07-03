package me.sjlee.redis_study.learn.example.app_log_migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Import(AppLogMigration.Config.class)
public class AppLogMigration {

    @Autowired
    LogWriter writer;

    @Autowired
    LogReceiver receiver;

    @Test
    void write_test() {
        for (int i = 0 ; i < 10 ; i++) {
            writer.log("log" + i);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void read_test() throws InterruptedException {
        receiver.start();
    }

    @TestConfiguration
    static class Config {

        @Autowired
        ApplicationContext applicationContext;

        @Autowired
        RedisTemplate<String, String> stringRedisTemplate;

        @Bean
        public LogWriter logWriter() {
            return new LogWriterV2(stringRedisTemplate);
        }

        @Bean
        public LogReceiver logReceiver() {
            return new LogReceiverV2(stringRedisTemplate);
        }
    }


}
