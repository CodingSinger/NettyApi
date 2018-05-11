package com.zzc.nettyapi.argument.conversion;

import com.zzc.nettyapi.exception.ConvertException;
import com.zzc.nettyapi.argument.conversion.convert.Converter;


/**
 * @author zhengzechao
 * @date 2018/4/15
 * Email ooczzoo@gmail.com
 */
public interface Conversion {


    /**
     * 检测是否能转换
     * @param sourceClass
     * @param targetClass
     * @return
     */
    Boolean canCanvert(Class sourceClass,Class targetClass);


    /**
     * 获取对应pair(sourceClass,targetClass)的转换器
     * @param sourceClass
     * @param targetClass
     * @return
     */
    Converter getConvert(Class sourceClass, Class targetClass);


    /**
     * 获取转换器转换
     * @param sourceClass
     * @param targetClass
     * @param value
     * @return
     * @throws ConvertException
     */
    Object convert(Class sourceClass,Class targetClass, Object value) throws ConvertException;
}
