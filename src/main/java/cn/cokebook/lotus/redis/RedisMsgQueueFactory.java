package cn.cokebook.lotus.redis;

import cn.cokebook.lotus.core.DelayMsgQueue;
import cn.cokebook.lotus.core.QueueProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;


@Slf4j
public class RedisMsgQueueFactory {

    private final RedisTemplate<String, String> redisTemplate;
    private final QueueProperties queueProperties;

    public RedisMsgQueueFactory(RedisTemplate<String, String> redisTemplate, QueueProperties queueProperties) {
        this.redisTemplate = redisTemplate;
        this.queueProperties = queueProperties;
    }

    public DelayMsgQueue get(String name) {
        return new RedisDelayQueue(name, redisTemplate, queueProperties);
    }

}
