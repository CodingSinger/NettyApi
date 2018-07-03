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
        this.conversion = conversion;
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
            for (int i = 0; i < sourceArray.length; i++) {
                Object o1 = sourceArray[i];
                //将该参数转换成适当的类型
                final Object target = conversion.convertIfNecessary(sourceType, targetType, o1);
                Array.set(o,i,target);
            }

            return o;
        }
    }

    @Override
    public boolean match(Class targetClass, Class sourceClass) {
        if (targetClass.isArray() && sourceClass.isArray()) {
            return true;
        }
        return false;
    }


}
