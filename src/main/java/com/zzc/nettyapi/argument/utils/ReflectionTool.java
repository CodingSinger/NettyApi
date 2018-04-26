package com.zzc.nettyapi.argument.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import javax.enterprise.inject.spi.Bean;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author zhengzechao
 * @date 2018/4/26
 * Email ooczzoo@gmail.com
 */
public class ReflectionTool {

    /** set方法前缀*/
    private static final String SET_PRE = "set";
    /** get方法前缀*/
    private static final String GET_PRE = "get";


    private void resolveWriteMethod(Class targetClass) {


        HashMap<String,Method> methodMap = Maps.newHashMap();

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

    //解析出对应的propertyHandler 并且将该propertyHandler放入到指定的BeanWrapper;
    private PropertyHandler resolveProperty(){

        PropertyHandler propertyHandler = new PropertyHandler();
        return propertyHandler;
    }

}
