package com.zzc.nettyapi.argument.conversion;

import com.zzc.nettyapi.Exception.ConvertException;

/**
 * @author zhengzechao
 * @date 2018/4/15
 * Email ooczzoo@gmail.com
 */
public interface Conversion {





    Object convert(Class type, String value) throws ConvertException;
}
