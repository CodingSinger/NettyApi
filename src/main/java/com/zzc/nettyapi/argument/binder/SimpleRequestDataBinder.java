package com.zzc.nettyapi.argument.binder;

import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.argument.conversion.SimpleConversion;
import com.zzc.nettyapi.argument.resolver.SimpleValueArgumentResolver;
import com.zzc.nettyapi.request.RequestDetail;

import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class SimpleRequestDataBinder implements DataBinder {




    private SimpleConversion conversion;


    public SimpleRequestDataBinder(SimpleConversion conversion) {
        this.conversion = conversion;
    }

    @Override
    public void doBinder(Object attribute, RequestDetail requestDetail,Class type) {
    }

    @Override
    public Object convertIfNecessary(Class type, String value) throws ConvertException {

        Object obj = null;
        if(!Objects.equals(type,String.class)){
            obj = conversion.convert(type,value);
        }
        return obj;
    }


}
