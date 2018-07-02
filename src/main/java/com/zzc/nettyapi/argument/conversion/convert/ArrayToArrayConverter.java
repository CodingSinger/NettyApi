package com.zzc.nettyapi.argument.conversion.convert;

import com.zzc.nettyapi.argument.conversion.Conversion;
import com.zzc.nettyapi.argument.conversion.DefaultConversion;
import com.zzc.nettyapi.argument.utils.ReflectionTool;
import com.zzc.nettyapi.exception.ConvertException;

import java.lang.reflect.Array;

/**
 * @author zhengzechao
 * @date 2018/7/2
 * Email ooczzoo@gmail.com
 */
public class ArrayToArrayConverter implements Converter<Object, Object> {

    private Conversion conversion = null;

    public ArrayToArrayConverter(Conversion conversion) {

    }

    @Override
    public Object convert(Object source, Class targetClass) throws ConvertException {

        //getComponentType获得数组的类型，不是数组则返回null
        Class sourceType = source.getClass().getComponentType();
        Class targetType = targetClass.getComponentType();
        if (sourceType.equals(targetType)) {
            return source;
        }
        //转换成Object[]数组
        Object[] sourceArray = ReflectionTool.toObjectArray(source);
        if (sourceArray.length == 0) {
            return null;
        } else {
            Object o = Array.newInstance(targetType, sourceArray.length);
            for (Object o1 : sourceArray) {
                final Object target = conversion.convert(sourceType, targetType, o1);
                Array.set(o,1,target);

            }
            return o;
        }
    }

    @Override
    public boolean match(Class targetClass, Class sourceClass) {
        if (targetClass.isArray() && sourceClass.isArray()) {
        }
        return false;
    }


}
