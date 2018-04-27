package com.zzc.nettyapi.argument.binder;

import com.google.common.collect.Maps;
import com.zzc.nettyapi.argument.conversion.DefaultConversion;
import com.zzc.nettyapi.argument.utils.MethodParameter;
import com.zzc.nettyapi.argument.resolver.ArgumentResolver;
import com.zzc.nettyapi.argument.resolver.SimpleValueArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class DataBinderFactory {


    private static final Logger log = LoggerFactory.getLogger(DataBinderFactory.class);

    private static HashMap<Class,DataBinder> dataBinderEntry ;

    public DataBinderFactory() {
        dataBinderEntry = Maps.newHashMap();


    }

    private void initEntry() {
        /**
         * TODO         将所有的ArgumentResolver类型收集起来,和对应的DataBinder的也收集起来。包括之前的ArugumentResolver在初始化
         *              时也可以通过收集起来的Resolver进行添加
         *
         */

        DefaultConversion conversion = new DefaultConversion();
        dataBinderEntry.put(SimpleValueArgumentResolver.class,new SimpleRequestDataBinder(conversion));
        dataBinderEntry.put(ModelAttributeDataBinder.class,new ModelAttributeDataBinder(conversion));
    }

    

    public DataBinder getFactoryData(ArgumentResolver resolver, MethodParameter parameter){
        //根据参数类型和resolver来获取对应的DataBinder
        DataBinder dataBinder = dataBinderEntry.get(resolver.getClass());


        if(Objects.nonNull(dataBinder)){

            log.debug("type:{} ------> dataBinder:{}",parameter.getType().getName(),dataBinder.getClass().getName());
            return dataBinder;
        }
        return null;
    }
}
