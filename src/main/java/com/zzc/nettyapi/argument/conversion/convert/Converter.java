package com.zzc.nettyapi.argument.conversion.convert;

import com.zzc.nettyapi.exception.ConvertException;

/**
 * @author zhengzechao
 * @date 2018/4/27
 * Email ooczzoo@gmail.com
 */

public interface Converter<S, T> {



    /**
     * // T为转换类型，S为原始类型
     * @param source
     * @param targetClass
     * @return
     * @throws ConvertException
     */
    T convert(S source,Class targetClass) throws ConvertException;


    boolean match(Class targetClass,Class sourceClass);

}
