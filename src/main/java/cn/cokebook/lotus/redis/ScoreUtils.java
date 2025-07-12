package cn.cokebook.lotus.redis;

import cn.cokebook.lotus.core.Msg;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScoreUtils {

    static final int DP = 2;
    static final int POT = (int) Math.pow(10, DP);

    public static String score(Long ts, Integer retries) {
        int tRetries = retries == null ? 0 : retries % POT;
        final long tts = ts == null ? 0 : ts;
        return BigDecimal.valueOf(tts).setScale(DP, RoundingMode.HALF_UP)
                .add(BigDecimal.valueOf(tRetries, DP)).toString();
    }

    public static String score(Msg msg) {
        return score(msg.getTs(), msg.getRetries());
    }

}
