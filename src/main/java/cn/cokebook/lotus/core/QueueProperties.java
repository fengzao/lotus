package cn.cokebook.lotus.core;


import lombok.Getter;
import lombok.Setter;


public class QueueProperties {

    public static final String SPRING_CONFIG_PREFIX = "cokebook.dq";

    private Integer maxFetchSize = 1000;

    public Integer getMaxFetchSize() {
        return maxFetchSize;
    }

    public void setMaxFetchSize(Integer maxFetchSize) {
        this.maxFetchSize = maxFetchSize;
    }
}
