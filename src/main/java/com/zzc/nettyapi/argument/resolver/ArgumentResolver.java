package com.zzc.nettyapi.argument.resolver;

import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.argument.MethodParameter;
import com.zzc.nettyapi.request.RequestDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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


    private ArgumentResolver next;


    public void setNext(ArgumentResolver next) {
        this.next = next;
    }


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
    abstract Object resolve(MethodParameter methodParameter,RequestDetail requestDetail) throws ConvertException;


    /**
     *
     * 找到支持该参数的解析器 并且解析
     * @param methodParameter
     * @return
     */
    public Object handleResolver(MethodParameter methodParameter,RequestDetail detail) throws ConvertException {



        Object obj = null;
        if (this.supportsParameter(methodParameter)){
            obj = this.resolve(methodParameter,detail);
        }else{

            /**
             * if next is not null
             */
            if (next != null){
                obj = next.handleResolver(methodParameter,detail);

            }else{
                log.info("No suitable argument resolver found!");
            }
        }
        return obj;


    }
}
