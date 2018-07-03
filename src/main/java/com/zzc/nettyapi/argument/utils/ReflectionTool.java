package com.zzc.nettyapi.argument.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map;

/**
 * @author zhengzechao
 * @date 2018/4/26
 * Email ooczzoo@gmail.com
 */
public class ReflectionTool {

    /**
     * set方法前缀
     */
    public static final String SET_PRE = "set";
    /**
     * get方法前缀
     */
    public static final String GET_PRE = "get";

    private final static Map<Class, Class> primitiveWrapperTypeMap;


    static {
        primitiveWrapperTypeMap = Maps.newHashMap();
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
    }

    public static void resolveMethods(BeanWrapper beanWrapper) {
        Class targetClass = beanWrapper.getTargetClass();
        Map<String, Method> writeMethodMap = Maps.newHashMap();
        Map<String, Method> readMethodMap = Maps.newHashMap();

        Method[] allMethods = targetClass.getMethods();
        for (Method method : allMethods) {
            String methodName = method.getName();
            if (methodName.startsWith(SET_PRE)) {
                String propertyName = methodName.substring(3);
                if (!Strings.isNullOrEmpty(propertyName)) {

                    propertyName = propertyName.substring(0, 1).toLowerCase() +
                            propertyName.substring(1);
                    writeMethodMap.put(propertyName, method);
                }

            } else if (methodName.startsWith(GET_PRE)) {
                String propertyName = methodName.substring(3);
                if (!Strings.isNullOrEmpty(propertyName)) {

                    propertyName = propertyName.substring(0, 1).toLowerCase() +
                            propertyName.substring(1);
                    readMethodMap.put(propertyName, method);
                }
            }
        }


        beanWrapper.setReadMethodMap(readMethodMap);
        beanWrapper.setWriteMethodMap(writeMethodMap);

    }


    /**
     * 解析出对应的propertyHandler 并且将该propertyHandler放入到指定的BeanWrapper;
     *
     * @param name
     * @param type
     * @return
     */

    public static PropertyHandler resolveProperty(String name, Class type, BeanWrapper beanWrapper) {

        //先从类型中查看是否能找到该name对应的field
        PropertyHandler propertyHandler = new PropertyHandler(name);
        Field field = findField(name, type, propertyHandler);
        if (Objects.nonNull(field)) {

            Class propertyType = field.getType();

            propertyHandler.setType(propertyType);
            propertyHandler.setPrimitive(propertyType.isPrimitive());
            propertyHandler.setArray(propertyType.isArray());
            Map<String, Method> writeMethodMap = beanWrapper.getWriteMethodMap();
            Map<String, Method> readMethodMap = beanWrapper.getReadMethodMap();
            if (Objects.isNull(writeMethodMap) || Objects.nonNull(readMethodMap)) {
                resolveMethods(beanWrapper);

                writeMethodMap = beanWrapper.getWriteMethodMap();
                readMethodMap = beanWrapper.getReadMethodMap();
                Method writeMethod = writeMethodMap.get(name);
                Method readMethod = readMethodMap.get(name);
                propertyHandler.setWritable(Objects.nonNull(writeMethod));
                propertyHandler.setWriteMethod(writeMethod);
                propertyHandler.setReadMethod(readMethod);
            }
        } else {
            return null;
        }

        //加入缓存
        beanWrapper.putPropertyHandler(name, propertyHandler);
        return propertyHandler;
    }

    public static Field findField(String name, Class type, PropertyHandler propertyHandler) {

        Class queryClass = type;
        Field[] fields;
        Field targetField = null;
        while (queryClass != null) {
            fields = queryClass.getDeclaredFields();
            targetField = Arrays.stream(fields)
                    .filter(t -> name.equals(t.getName()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(targetField)) {
                propertyHandler.setBelongType(queryClass);
                break;
            }
            queryClass = queryClass.getSuperclass();
        }

        return targetField;
    }


    public static Boolean isPrimitiveTypeOrWrapped(Class clazz) {
        return clazz.isPrimitive() || primitiveWrapperTypeMap.containsKey(clazz);

    }

    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[]) {
            return (Object[]) source;
        }
        if (source == null) {
            return new Object[0];
        }
        if (!source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        }
        int length = Array.getLength(source);
        if (length == 0) {
            return new Object[0];
        }
        //该方法会返回Object形式的元素
        Class<?> wrapperType = Array.get(source, 0).getClass();
        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
        for (int i = 0; i < length; i++) {
            newArray[i] = Array.get(source, i);
        }
        return newArray;
    }
}
