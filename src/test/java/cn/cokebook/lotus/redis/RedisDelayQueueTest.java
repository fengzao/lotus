package cn.cokebook.lotus.redis;

import cn.cokebook.lotus.core.DelayMsgQueue;
import cn.cokebook.lotus.core.Msg;
import cn.cokebook.lotus.redis.RedisDelayQueue;
import cn.cokebook.lotus.redis.RedisMsgQueueFactory;
import cn.cokebook.lotus.redis.boot.RedisDelayQueueConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {
        RedisAutoConfiguration.class,
        RedisDelayQueueConfiguration.class,
})
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
public class RedisDelayQueueTest {

    @Autowired
    RedisMsgQueueFactory factory;

    @Test
    public void test_offer() {
        DelayMsgQueue queue = factory.get("demo");
        queue.offer("hello world", 1000);

    }

    @Test
    public void test_batch_offer() {
        DelayMsgQueue queue = factory.get("demo");
        List<Msg> msgList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            msgList.add(Msg.of("id:" + i, System.currentTimeMillis()));
        }
        long st = System.currentTimeMillis();
        queue.offer(msgList);
        long et = System.currentTimeMillis();
        System.out.println("dt = " + (et - st) + "Ms , size = " + msgList.size());

    }


    @Test
    public void test_ack() {
        DelayMsgQueue queue = factory.get("demo");
        queue.ack("hello world");
    }


    @Test
    public void test_poll() {
//        test_batch_offer();
        RedisDelayQueue queue = (RedisDelayQueue) factory.get("demo");
        long st = System.currentTimeMillis();
        List<Msg> msgList = queue.poll(100, Duration.ofSeconds(20));
        long et = System.currentTimeMillis();
        System.out.println("------------- ts = " + (et - st) + "ms" + "size = " + msgList.size());
        for (Msg msg : msgList) {
            System.out.println("msg = " + msg);
        }
    }


}
