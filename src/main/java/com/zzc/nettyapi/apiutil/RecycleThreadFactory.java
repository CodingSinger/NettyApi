package com.zzc.nettyapi.apiutil;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */
public class RecycleThreadFactory implements ThreadFactory{

    private final AtomicInteger count = new AtomicInteger(1);
    private final String namePrefix ;

    private final ThreadGroup group;

    public RecycleThreadFactory(String namePrefix) {


        SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {

        Thread t = new Thread(group,r);
        t.setDaemon(false);
        t.setName(namePrefix+"--"+count.getAndIncrement());

        return t;
    }
}
