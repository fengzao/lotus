package cn.cokebook.lotus.core;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface Msg {

    String getContent();

    Integer getRetries();

    Long getTs();


    static Msg of(String content, long expireAtTs) {
        return of(content, expireAtTs, 0);
    }

    static Msg of(String content, long expireAtTs, int retries) {

        return new MsgImpl().setContent(content)
                .setTs(expireAtTs)
                .setRetries(retries);

    }

    @Data
    @Accessors(chain = true)
    class MsgImpl implements Msg {
        private String content;
        private Long ts;
        private Integer retries = 0;
    }

}
