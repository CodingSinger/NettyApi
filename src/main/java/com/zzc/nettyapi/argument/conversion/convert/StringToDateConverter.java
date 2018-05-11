package com.zzc.nettyapi.argument.conversion.convert;

import com.zzc.nettyapi.exception.ConvertException;

import java.util.Date;

/**
 * @author zhengzechao
 * @date 2018/4/27
 * Email ooczzoo@gmail.com
 */
public class StringToDateConverter implements Converter<String,Date> {
    @Override
    public Date convert(String source, Class targetClass) throws ConvertException {
        return null;
    }

    @Override
    public boolean match(Class targetClass, Class sourceClass) {
        return false;

    }
}
