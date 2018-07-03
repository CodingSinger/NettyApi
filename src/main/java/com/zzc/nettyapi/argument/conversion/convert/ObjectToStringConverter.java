package com.zzc.nettyapi.argument.conversion.convert;

import com.zzc.nettyapi.exception.ConvertException;

/**
 * @author zhengzechao
 * @date 2018/7/3
 * Email ooczzoo@gmail.com
 */
public class ObjectToStringConverter implements Converter<Object,String>{
    @Override
    public String convert(Object source, Class targetClass) throws ConvertException {
        return source.toString();
    }

    @Override
    public boolean match(Class targetClass, Class sourceClass) {
        if (sourceClass.equals(Object.class) && targetClass.equals(String.class)){
            return true;
        }
        return false;
    }
}
