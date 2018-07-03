package com.zzc.nettyapi.argument.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author zhengzechao
 * @date 2018/7/3
 * Email ooczzoo@gmail.com
 */
public class PropertyAccessor {


    /**
     * 获取attibute对象中的property表示的实际值
     *
     * @param attribute
     * @param propertyHandler
     * @return 可能返回null
     */
    private Object getPropertyValue(Object attribute, PropertyHandler propertyHandler) {
        Object propertyValue = null;
        if (propertyHandler.isReadable()) {
            try {
                propertyValue = propertyHandler.getReadMethod().invoke(attribute);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return propertyValue;


    }


    /**
     * 设置attibute属性中的propertyHandler的默认值
     *
     * @param attribute
     * @param propertyHandler
     * @return
     */
    private Object setDefaultValue(Object attribute, PropertyHandler propertyHandler) {
        Object instance = null;
        if (propertyHandler.isWritable()) {

            Class propertyType = propertyHandler.getType();
            Constructor declaredConstructor = null;
            try {
                declaredConstructor = propertyType.getDeclaredConstructor(null);
                instance = declaredConstructor.newInstance();
                Method writeMethod = propertyHandler.getWriteMethod();
                writeMethod.invoke(attribute, instance);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalStateException();

        }
        return instance;

    }


    /**
     * @param attribute
     * @param property
     * @return
     */
    public Object getNestedPropertyValue(Object attribute, PropertyHandler property) {

        Object value = getPropertyValue(attribute, property);

        if (value == null || (value instanceof Optional && !((Optional) value).isPresent())) {
            value = setDefaultValue(attribute, property);
        }

        return value;
    }

}
