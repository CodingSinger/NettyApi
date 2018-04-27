package com.zzc.nettyapi.argument.binder;

import com.zzc.nettyapi.Exception.ConvertException;

import com.zzc.nettyapi.request.RequestDetail;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public interface DataBinder {


    void doBinder(Object attribute, RequestDetail requestDetail,Class Type) throws Exception;

    default Object convertIfNecessary(Class sourceClass,Class targetClass,Object value) throws ConvertException{
        return null;
    }
}
