package cn.cokebook.lotus.redis.boot;

import cn.cokebook.lotus.core.QueueProperties;
import cn.cokebook.lotus.redis.RedisMsgQueueFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnProperty(value = "cokebook.dq.mode", havingValue = "redis", matchIfMissing = true)
public class RedisDelayQueueConfiguration {

    @Bean
    @ConditionalOnMissingBean(RedisMsgQueueFactory.class)
    public RedisMsgQueueFactory redisDelayMsgQueueFactory(
            RedisTemplate<String, String> redisTemplate,
            QueueProperties properties) {
        return new RedisMsgQueueFactory(redisTemplate, properties);
    }


    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(QueueProperties.SPRING_CONFIG_PREFIX)
    public static QueueProperties queueProperties() {
        return new QueueProperties();
    }

}
