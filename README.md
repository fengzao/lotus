### lotus

-  [English](./README.md)  [中文](./README.CN.md)

> Redis-Based Delayed Message/Task Queue: Implemented with a Single SortedSet, Supporting ACK Mechanism for Reliable
> Message Delivery

### docs

- example : cn.cokebook.lotus.redis.RedisDelayQueueTest

- web api :  cn.cokebook.lotus.api.DelayQueueController

### Note

- a single message can be retried up to 99 times. After reaching 99 retries, the retry count starts over from 0.