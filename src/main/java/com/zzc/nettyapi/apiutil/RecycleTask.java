package com.zzc.nettyapi.apiutil;

import java.util.Queue;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */
public class RecycleTask implements Runnable{


    private Recyclable recycleObj;

    private Queue recycleContainer;

    public RecycleTask(Recyclable recycleObj, Queue recycleContainer) {
        this.recycleObj = recycleObj;
        this.recycleContainer = recycleContainer;
    }


    @Override
    public void run() {
        recycleObj.reset();
        recycleContainer.offer(recycleObj);
    }



}
