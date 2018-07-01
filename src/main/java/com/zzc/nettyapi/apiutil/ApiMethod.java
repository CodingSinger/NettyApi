package com.zzc.nettyapi.apiutil;

import com.zzc.nettyapi.argument.utils.MethodParameter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 *
 *
 *
 * 存储方法元数据(方法名，类名，方法参数类型，方法参数名字，方法对应的uri的正则等)
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ApiMethod implements Serializable {
    private volatile Method method; //对应方法
    private Class handleClass;//对应类
    private String url; //对应url
    private Set<String> supportMethods = new HashSet<String>(); //支持的方法 GET POST
    private String className;
    private Object handler;//处理对象
    private String methodName; //方法名
    private String regex;
    private List<String> parameterNames = new LinkedList<>();
//    private String[] parameterNames ;
    private Class[] parameterTypes ;
    private Boolean valid;
    private volatile Boolean reload; //其class是否被重新加载

    public Boolean getReload() {
        return reload;
    }

    public void setReload(Boolean reload) {
        this.reload = reload;
    }

    public ApiMethod() {
        valid = false;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    private MethodParameter[] parameters;

    public MethodParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(MethodParameter[] parameters) {
        this.parameters = parameters;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public List<String> getParameterNames() {
        return parameterNames;
    }

    public void setParameterNames(List<String> parameterNames) {
        this.parameterNames = parameterNames;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
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
                parameterNames.add(strings[i].substring(1)); //路径参数名
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
