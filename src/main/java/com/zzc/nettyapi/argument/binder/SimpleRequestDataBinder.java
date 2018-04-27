package com.zzc.nettyapi.argument.binder;

import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.argument.conversion.DefaultConversion;
import com.zzc.nettyapi.request.RequestDetail;

import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class SimpleRequestDataBinder implements DataBinder {




    private DefaultConversion conversion;


    public SimpleRequestDataBinder(DefaultConversion conversion) {
        this.conversion = conversion;

    }

    @Override
    public void doBinder(Object attribute, RequestDetail requestDetail,Class type) {
    }

    @Override
    public Object convertIfNecessary(Class sourceClass,Class targetClass, Object value ) throws ConvertException {

        Object obj = null;
        if(!Objects.equals(sourceClass,String.class)){
            obj = conversion.convert(sourceClass,targetClass,value);
        }
        return obj;
    }


}
