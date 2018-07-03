package com.zzc.nettyapi.argument.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author zhengzechao
 * @date 2018/4/18
 * Email ooczzoo@gmail.com
 */
public class HandleMethodArgumentParser {


    public HandleMethodArgumentParser() {
    }


    public MethodParameter[] parse(Method apiMethod) {

        Parameter[] parameters = apiMethod.getParameters();
        MethodParameter[] methodParameters = new MethodParameter[parameters.length];

        for (int i = 0; i < parameters.length; i++) {

            methodParameters[i] = new MethodParameter(parameters[i], i);

        }

        return methodParameters;

    }
}
