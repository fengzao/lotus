package cn.cokebook.lotus.api;

import cn.cokebook.lotus.core.DelayMsgQueue;
import cn.cokebook.lotus.core.Msg;
import cn.cokebook.lotus.redis.RedisMsgQueueFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/delay-queue")
public class DelayQueueController {


    @Autowired
    RedisMsgQueueFactory delayMsgQueueFactory;

    @GetMapping("/version")
    public String version() {
        return delayMsgQueueFactory.getClass().getName();
    }

    /**
     * 队列新增延迟消息
     *
     * @param queueName  队列名
     * @param msgDTOList 消息: 消息内容 + 消息到期时间点
     */
    @PostMapping("/{queueName:[\\w:-]+}")
    public void enqueue(@PathVariable("queueName") String queueName, @RequestBody List<MsgDTO> msgDTOList) {
        DelayMsgQueue queue = delayMsgQueueFactory.get(queueName);
        queue.offer(msgDTOList);
    }

    /**
     * 获取已到期消息
     *
     * @param queueName 队列名
     * @param lockMs    为避免消费者崩溃导致消息丢失, 消息延后多久可以再次被获取.
     * @param size      拉去消息条数 : 消费者需要考虑消费能力, 避免一次性拉去太多, 导致消息消费重叠。
     * @return 消息列表
     */
    @GetMapping("/{queueName:[\\w:-]+}")
    public List<Msg> dequeue(@PathVariable("queueName") String queueName,
                             @RequestParam(value = "lockMs", defaultValue = "5000", required = false) Long lockMs,
                             @RequestParam(value = "size", defaultValue = "1", required = false) Integer size) {

        DelayMsgQueue queue = delayMsgQueueFactory.get(queueName);
        return queue.poll(size, Duration.ofMillis(lockMs));
    }

    /**
     * 延迟消息 ACK : 该操作表示消息已经被正常消费,消息将从队列中移除
     *
     * @param queueName 队列名
     * @param msgList   消息列表
     */
    @DeleteMapping("/{queueName:[\\w:-]+}")
    public void ack(@PathVariable("queueName") String queueName, @RequestBody List<String> msgList
    ) {
        if (msgList == null || msgList.isEmpty()) {
            return;
        }
        DelayMsgQueue queue = delayMsgQueueFactory.get(queueName);
        queue.ack(msgList);
    }


    @Data
    public static class MsgDTO implements Msg {
        private String content;
        private Long ts;

        @Override
        public int getRetries() {
            return 0;
        }
    }


}
