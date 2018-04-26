package com.zzc.nettyapi.argument.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author zhengzechao
 * @date 2018/4/18
 * Email ooczzoo@gmail.com
 */
public class MethodParameter {


    public MethodParameter(Parameter parameter,int index) {
        this.parameter = parameter;
        this.index = index;


    }

    private Parameter parameter;

   //所属方法

    private Method method;


    //下标


    private int index;


    public Class getType(){
        return parameter.getType();
    }

    public String name(){
        return parameter.getName();
    }
    public Annotation[] getAnnotations(){
        return parameter.getDeclaredAnnotations();
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
