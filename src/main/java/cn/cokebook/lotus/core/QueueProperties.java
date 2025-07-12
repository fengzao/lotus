package cn.cokebook.lotus.core;

public class QueueProperties {

    public static final String SPRING_CONFIG_PREFIX = "cokebook.dq";

    private Integer maxPollSize = 1000;

    public Integer getMaxPollSize() {
        return maxPollSize;
    }

    public void setMaxPollSize(Integer maxPollSize) {
        this.maxPollSize = maxPollSize;
    }
}
