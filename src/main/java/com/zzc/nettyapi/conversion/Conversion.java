package com.zzc.nettyapi.conversion;

/**
 * @author zhengzechao
 * @date 2018/4/15
 * Email ooczzoo@gmail.com
 */
public interface Conversion {



    Object convert(Class targetType,String value);
}
