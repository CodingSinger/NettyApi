package com.zzc.nettyapi.apiutil;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */
public class Result {

    private int status;
    private Object obj;

    public Result(int status, Object obj) {
        this.status = status;
        this.obj = obj;
    }

    public Result() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getObj() {

        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
