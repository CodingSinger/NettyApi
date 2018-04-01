package com.zzc.nettyapi.apiutil;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */

//可回收的
public interface Recyclable {
    /**
     * 回收方法
     */
    void reset();
}
