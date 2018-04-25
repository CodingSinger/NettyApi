package com.zzc.nettyapi.argument;

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


    private static final String SET_PRE = "set";
    private static final String GET_PRE = "get";
    private Class targetClass;
    private Method[] writeMethods;

    private Method[] readMethods;

    private Map<String,Method> methodMap;


    public BeanWrapper(Class targetClass) {
        this.targetClass = targetClass;
        this.methodMap = Maps.newHashMap();
        resolve();
    }

    private void resolve() {
        Method[] allMethods = targetClass.getMethods();
        for (Method method : allMethods) {
            String methodName = method.getName();
            if(methodName.startsWith(SET_PRE)){
                String propertyName = methodName.substring(3);
                if (!Strings.isNullOrEmpty(propertyName)){

                    propertyName = propertyName.substring(0,1).toLowerCase()+
                                   propertyName.substring(1);
                    methodMap.put(propertyName,method);
                }

            }else if(methodName.startsWith(GET_PRE)){
            }
        }
    }



    public Method getWriteMethod(String property){
        return methodMap.get(property);
    }


}
