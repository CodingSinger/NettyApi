package com.zzc.nettyapi.argument.resolver;

import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.argument.utils.MethodParameter;
import com.zzc.nettyapi.argument.binder.DataBinder;
import com.zzc.nettyapi.argument.binder.DataBinderFactory;
import com.zzc.nettyapi.request.RequestDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/19
 * Email ooczzoo@gmail.com
 */
public class SimpleValueArgumentResolver extends ArgumentResolver{



    private static final Logger log = LoggerFactory.getLogger(SimpleValueArgumentResolver.class);




    public SimpleValueArgumentResolver(DataBinderFactory binderFactory) {
        super(binderFactory);

    }

    @Override
    boolean supportsParameter(MethodParameter methodParameter) {



        Class type = methodParameter.getType();

        return type.isPrimitive()||
                Number.class.isAssignableFrom(type)||
                CharSequence.class.isAssignableFrom(type);
    }



    @Override
    Object resolve(MethodParameter methodParameter, RequestDetail requestDetail) throws ConvertException {
        Map<String,List<String>> parametersMaps = requestDetail.getParamters();
        Object arg = null;

        //获取参数名
        String parameterName = methodParameter.name();

        Class type = methodParameter.getType();

        List<String> valueLists = parametersMaps.get(parameterName);

        if(Objects.nonNull(valueLists) && !valueLists.isEmpty()){
            String value = valueLists.get(0);

            /**
             * 获取对应的参数绑定器
             */

            DataBinder dataBinder = binderFactory.getFactoryData(this,methodParameter);

            /*
            * TODO 简单类型类型转换
            * */
            try {
                 arg = dataBinder.convertIfNecessary(type,value);


            } catch (ConvertException e) {
                log.error("convert error! excepted:{},but {}",type,value);
                throw e;
            }
        }else{
            return null;
        }
        return arg;

    }


}
