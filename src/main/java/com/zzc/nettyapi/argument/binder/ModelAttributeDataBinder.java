package com.zzc.nettyapi.argument.binder;

import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.request.RequestDetail;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class ModelAttributeDataBinder implements DataBinder{

    @Override
    public void doBinder(Object attribute, RequestDetail requestDetail,Class type) {
        /**
         * TODO 将请求参数中的键和type中的属性值对应起来 注入到attribute中
         */
    }

    @Override
    public Object convertIfNecessary(Class type, String value) throws ConvertException {
        return null;
    }
}
