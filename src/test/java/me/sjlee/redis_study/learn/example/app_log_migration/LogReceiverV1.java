package me.sjlee.redis_study.learn.example.app_log_migration;

import org.springframework.data.redis.core.RedisTemplate;

public class LogReceiverV1 implements LogReceiver {

    private final RedisTemplate redisTemplate;

    private static final String KEY_WAS_LOG = "was:log";

    LogReceiverV1(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void start() {
        while (true) {
            String value = (String) redisTemplate.opsForValue().getAndSet(KEY_WAS_LOG, "");

            writeToFiles(value);

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToFiles(String value) {
        System.out.println(value);
    }
}