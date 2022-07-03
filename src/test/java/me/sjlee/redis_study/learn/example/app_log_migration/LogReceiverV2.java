package me.sjlee.redis_study.learn.example.app_log_migration;

import org.springframework.data.redis.core.RedisTemplate;

public class LogReceiverV2 implements LogReceiver {

    private final RedisTemplate redisTemplate;

    private static final String KEY_WAS_LOG_LIST = "was:log:list";

    LogReceiverV2(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void start() {
        // 외부에서 5초마다 호출하도록 변경한다.
        while (true) {
            String value = (String) redisTemplate
                    .opsForList().rightPop(KEY_WAS_LOG_LIST);
            if (value == null) {
                break;
            }
            writeToFiles(value);
        }
    }

    private void writeToFiles(String value) {
        System.out.println(value);
    }
}