package com.zzc.nettyapi.argument.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sun.javafx.collections.MappingChange;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/25
 * Email ooczzoo@gmail.com
 */
public class BeanWrapper {


    private Class targetClass;
    private Method[] writeMethods;

    private Method[] readMethods;

    private Map<String,PropertyHandler> propertyMap;

    private Map<String,Method> writeMethodMap;

    private Map<String,Method> readMethodMap;


    public BeanWrapper(Class targetClass) {
        this.targetClass = targetClass;
    }

    public BeanWrapper() {
    }



    public PropertyHandler getPropertyHandler(String name){
        return propertyMap.get(name);
    }

    public void putPropertyHandler(String name,PropertyHandler propertyHandler){
        propertyMap.put(name,propertyHandler);
    }
    public Map<String, Method> getReadMethodMap() {
        return readMethodMap;
    }

    public void setReadMethodMap(Map<String, Method> readMethodMap) {
        this.readMethodMap = readMethodMap;
    }

    public Map<String, Method> getWriteMethodMap() {
        return writeMethodMap;
    }

    public void setWriteMethodMap(Map<String, Method> writeMethodMap) {
        this.writeMethodMap = writeMethodMap;
    }
}
