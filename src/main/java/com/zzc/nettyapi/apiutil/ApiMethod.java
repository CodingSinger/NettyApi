package com.zzc.nettyapi.apiutil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ApiMethod implements Serializable {
    private volatile Method method; //对应方法
    private Class handleClass;//对应类
    private String url; //对应url
    private Set<String> supportMethods = new HashSet<String>(); //支持的方法
    private String className;
    private Object handler;//处理对象
    private String methodName; //方法名
    private String regex;
    private LinkedList<String> parameterNames = new LinkedList<>();
    private Class[] parameterTypes ;

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public LinkedList<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(LinkedList<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;


    }

    public Class getHandleClass() {
        return handleClass;
    }

    public void setHandleClass(Class handleClass) {
        this.handleClass = handleClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        String[]      strings       = url.split("/");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0, len = strings.length; i < len; i++) {
            if (strings[i].length() == 0) {
                continue;
            }
            stringBuilder.append("/");
            if (strings[i].startsWith(":")) {
                parameterNames.add(strings[i].substring(1));
                stringBuilder.append("([^/]+)");
            } else {
                stringBuilder.append(strings[i]);
            }
        }

        this.regex = stringBuilder.toString();
    }

    public Set<String> getSupportMethods() {
        return supportMethods;
    }

    public void setSupportMethods(Set<String> supportMethods) {
        this.supportMethods = supportMethods;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }
}
