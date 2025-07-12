package cn.cokebook.lotus.core;

public interface Msg {

    String getContent();

    int getRetries();

    Long getTs();

    static Msg of(String content, long expireAtTs) {
        return of(content, expireAtTs, 0);
    }

    static Msg of(String content, long expireAtTs, int retries) {

        return new Msg() {
            @Override
            public String getContent() {
                return content;
            }

            @Override
            public int getRetries() {
                return retries;
            }

            @Override
            public Long getTs() {
                return expireAtTs;
            }

            @Override
            public String toString() {
                return String.format("{content = %s , retries = %d , ts  = %s}", content, retries, expireAtTs);
            }
        };

    }

}
