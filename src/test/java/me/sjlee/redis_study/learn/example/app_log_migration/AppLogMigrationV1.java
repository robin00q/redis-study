package me.sjlee.redis_study.learn.example.app_log_migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@Import(AppLogMigrationV1.Config.class)
public class AppLogMigrationV1 {

    @Autowired
    LogWriter writer;

    @Autowired
    LogReceiver logReceiver;

    @Test
    void write_test() {
        for (int i = 0 ; i < 100 ; i++) {
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
        logReceiver.start();
    }

    @TestConfiguration
    static class Config {

        @Autowired
        ApplicationContext applicationContext;

        @Autowired
        RedisTemplate<String, String> stringRedisTemplate;

        @Bean
        public LogWriter logWriter() {
            return new LogWriter(stringRedisTemplate);
        }

        @Bean
        public LogReceiver logReceiver() {
            return new LogReceiver(stringRedisTemplate);
        }
    }

    static class LogWriter {

        private final RedisTemplate redisTemplate;

        private static final String KEY_WAS_LOG = "was:log";

        LogWriter(RedisTemplate redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        public void log(String log) {
            redisTemplate.opsForValue().append(KEY_WAS_LOG, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + log + "\n");
        }
    }

    static class LogReceiver {
        private final RedisTemplate redisTemplate;

        private static final String KEY_WAS_LOG = "was:log";

        LogReceiver(RedisTemplate redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        public void start() throws InterruptedException {
            while (true) {
                String value = (String) redisTemplate.opsForValue().getAndSet(KEY_WAS_LOG, "");

                writeToFiles(value);

                Thread.sleep(5000L);
            }
        }

        private void writeToFiles(String value) {
            System.out.println(value);
        }
    }
}
