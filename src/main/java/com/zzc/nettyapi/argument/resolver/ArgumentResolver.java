package com.zzc.nettyapi.argument.resolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.deploy.util.ArrayUtil;
import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.argument.MethodParameter;
import com.zzc.nettyapi.argument.binder.DataBinderFactory;
import com.zzc.nettyapi.request.RequestDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;
import java.util.Objects;


/**
 *
 *
 *
 *
 * 参数解析过程 获取何时的ArgumentResolver并且获得参数绑定器，参数绑定器里进行bean属性的绑定，有必要进行转化的则进行转化类型
 */

/**
 * @author zhengzechao
 * @date 2018/4/18
 * Email ooczzoo@gmail.com
 */
public abstract class ArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(ArgumentResolver.class);
//    public void resolver();

    protected DataBinderFactory binderFactory;



    /**
     *  参数解析器
     *
     */
    public static List<ArgumentResolver> argumentResolvers = Lists.newLinkedList();

    public static Map<MethodParameter,ArgumentResolver> cachedArgumentResolver ;
    public ArgumentResolver(DataBinderFactory binderFactory) {
        this.binderFactory = binderFactory;

    }

    static{
        initArgumentResplver();
        cachedArgumentResolver = Maps.newConcurrentMap();
    }

    public ArgumentResolver() {


    }

    /**
     * TODO :分为简单类和自定义类来做
     * SpringMVC中的简单类判别：
     * public static boolean isSimpleValueType(Class<?> clazz) {
     return (ClassUtils.isPrimitiveOrWrapper(clazz) ||
     Enum.class.isAssignableFrom(clazz) ||
     CharSequence.class.isAssignableFrom(clazz) ||
     Number.class.isAssignableFrom(clazz) ||
     Date.class.isAssignableFrom(clazz) ||
     URI.class == clazz || URL.class == clazz ||
     Locale.class == clazz || Class.class == clazz);
     }
     */

    /**
     * 查看能解析该方法参数的解析器
     * @param methodParameter
     * @return
     */
    abstract boolean supportsParameter(MethodParameter methodParameter);
    /**
     *
     * 将客户请求中传递的合适的参数解析到MethodParameter中去,其中对简单类型包括类型转换，对Pojo类型包括参数转换和属性注入
     * @param requestDetail
     * @return
     */
    abstract Object resolve(MethodParameter methodParameter,RequestDetail requestDetail) throws Exception;


    public static void initArgumentResplver(){
        DataBinderFactory binderFactory = new DataBinderFactory();
        SimpleValueArgumentResolver simpleValueArgumentResolver = new SimpleValueArgumentResolver(binderFactory);
        argumentResolvers.add(simpleValueArgumentResolver);

        argumentResolvers.add(new ModelAttributeArgumentResolver(binderFactory));

    }


    public static Boolean supportMethodParameter(List<ArgumentResolver> argumentResolvers, MethodParameter methodParameter) {

        ArgumentResolver cachedResolver = cachedArgumentResolver.get(methodParameter);

        if (Objects.nonNull(cachedResolver)){

            for (ArgumentResolver argumentResolver : argumentResolvers) {
                if (argumentResolver.supportsParameter(methodParameter)){
                    cachedArgumentResolver.put(methodParameter,argumentResolver);
                    return true;
                }
            }

        }



        return cachedResolver != null;

    }

    public static Object resolveMethodParameter(MethodParameter methodParameter,RequestDetail requestDetail) throws Exception {
        ArgumentResolver argumentResolver = cachedArgumentResolver.get(methodParameter);

        return argumentResolver.resolve(methodParameter,requestDetail);

    }
}
