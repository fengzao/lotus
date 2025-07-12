package cn.cokebook.lotus.redis;

import cn.cokebook.lotus.core.Msg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScoreUtilsTest {

    @Test
    public void test_score() {
        Msg msg = Msg.of("abc", System.currentTimeMillis(), 2);
        Assertions.assertEquals(msg.getTs() + ".02", ScoreUtils.score(msg));

        msg = Msg.of("abc", System.currentTimeMillis(), 90);
        Assertions.assertEquals((msg.getTs()) + ".90", ScoreUtils.score(msg));

        msg = Msg.of("abc", System.currentTimeMillis(), 121);
        Assertions.assertEquals((msg.getTs()) + ".21", ScoreUtils.score(msg));


        msg = Msg.of("abc", System.currentTimeMillis(), 110121);
        Assertions.assertEquals((msg.getTs()) + ".21", ScoreUtils.score(msg));


    }

}
