package cn.cokebook.lotus.core;

import java.time.Duration;
import java.util.List;

public interface DelayMsgQueue {

    Duration DEFAULT_LOCK_TIME = Duration.ofSeconds(5);
    Integer MAX_FETCH_SIZE = 1000;
    Integer DEF_FETCH_SIZE = 10;

    void offer(String msg, long ts);

    void offer(List<? extends Msg> msgList);

    default List<Msg> poll(Duration lockTime) {
        return poll(DEF_FETCH_SIZE, lockTime);
    }

    List<Msg> poll(Integer size, Duration lockTime);

    void ack(String msg);

    void ack(List<String> msg);

}
