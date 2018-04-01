package com.zzc.nettyapi.conversion;

/**
 * @author zhengzechao
 * @date 2018/4/1
 */
public class SimpleConversion {

    public Object convert(Class targetType,String value){

        if("java.lang.Integer".equals(targetType.getName())||"int".equals(targetType.getName())){
            return Integer.valueOf(value);
        }
        return null;
    }
}
