### lotus

> 基于 Redis 的延迟消息/任务 队列 : 使用单一 SortedSet, 支持 ACK 机制, 保障消息可靠投递

### 说明

- example : cn.cokebook.lotus.redis.RedisDelayQueueTest

- web api :  cn.cokebook.lotus.api.DelayQueueController

### 注意

- 针对单一消息默认最多重试 99 次, 到达99次后重试次数从0重新开发计算