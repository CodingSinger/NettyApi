package com.zzc.nettyapi.argument.utils;

import java.lang.reflect.Method;

/**
 * @author zhengzechao
 * @date 2018/4/26
 * Email ooczzoo@gmail.com
 */


public class PropertyHandler {

    /** 所属类类型*/
    private Class belongType;
    /** 自身类*/
    private Class type;

    /** 属性名*/
    private String propertyName;

    /** 是否基本类型*/
    private boolean primitive;


    /** 是否数组类型*/
    private boolean array;


    /** 是否可写*/
    private boolean isWritable;

    /** 写方法*/
    private Method writeMethod;

    /** 读方法*/
    private Method readMethod;

    public PropertyHandler(String name) {
    }

    public PropertyHandler() {
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public Class getBelongType() {
        return belongType;
    }

    public void setBelongType(Class belongType) {
        this.belongType = belongType;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String isPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public boolean isWritable() {
        return isWritable;
    }

    public void setWritable(boolean writable) {
        isWritable = writable;
    }
}
