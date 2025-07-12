package cn.cokebook.lotus.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class Threads {


    public static ThreadFactory createThreadFactory(String name) {
        final AtomicLong index = new AtomicLong();
        return r -> new Thread(r, name + "-" + index.incrementAndGet());
    }

}
