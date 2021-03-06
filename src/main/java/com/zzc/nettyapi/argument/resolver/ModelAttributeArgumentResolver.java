package com.zzc.nettyapi.argument.resolver;

import com.zzc.nettyapi.argument.utils.MethodParameter;
import com.zzc.nettyapi.argument.binder.DataBinder;
import com.zzc.nettyapi.argument.binder.DataBinderFactory;
import com.zzc.nettyapi.argument.utils.ReflectionTool;
import com.zzc.nettyapi.request.RequestDetail;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

/**
 * @author zhengzechao
 * @date 2018/4/25
 * Email ooczzoo@gmail.com
 */
public class ModelAttributeArgumentResolver extends ArgumentResolver {
    public ModelAttributeArgumentResolver(DataBinderFactory binderFactory) {
        super(binderFactory);
    }

    @Override
    boolean supportsParameter(MethodParameter methodParameter) {
        return !isSimpleProperty(methodParameter.getType());

    }

    //判断是否是简单类型
    public static boolean isSimpleProperty(Class<?> clazz) {
        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
    }

    public static boolean isSimpleValueType(Class<?> clazz) {
        return (ReflectionTool.isPrimitiveTypeOrWrapped(clazz)||
                Enum.class.isAssignableFrom(clazz) ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                URI.class == clazz || URL.class == clazz ||
                Locale.class == clazz || Class.class == clazz);
    }


    @Override
    Object resolve(MethodParameter methodParameter, RequestDetail requestDetail) throws Exception {

        String name = methodParameter.name();

        Object attribute = createAttribute(methodParameter.getType(), requestDetail);
        DataBinder dataBinder = this.binderFactory.getFactoryData(this, methodParameter);
        dataBinder.doBinder(attribute, requestDetail, methodParameter.getType());

        return attribute;

    }

    private Object createAttribute(Class type, RequestDetail requestDetail) throws Exception {

        //获取所有public 的构造函数
        Constructor[] constructors = type.getConstructors();


        /**
         * TODO 暂且只支持无参的constructor类型构造器。
         */
        Constructor constructor = null;
        try {
            constructor = type.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {

            if (e instanceof NoSuchMethodException) {
                throw new IllegalStateException("No default constructor for class: " + type.getName(), e);
            } else {
                throw new IllegalStateException("create Object attribute error for class: " + type.getName(), e);
            }


        }


    }
}
