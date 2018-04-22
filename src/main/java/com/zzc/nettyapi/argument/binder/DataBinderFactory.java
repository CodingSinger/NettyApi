package com.zzc.nettyapi.argument.binder;

import com.google.common.collect.Maps;
import com.zzc.nettyapi.argument.MethodParameter;
import com.zzc.nettyapi.argument.resolver.ArgumentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class DataBinderFactory {


    private static final Logger log = LoggerFactory.getLogger(DataBinderFactory.class);

    private final HashMap<ArgumentResolver,DataBinder> dataBinderEntry ;

    public DataBinderFactory() {
        dataBinderEntry = Maps.newHashMap();
    }

    public DataBinder getDataFactory(ArgumentResolver resolver, MethodParameter parameter){
        //根据参数类型和resolver来获取对应的DataBinder
        DataBinder dataBinder = dataBinderEntry.get(resolver);

        if(Objects.nonNull(dataBinder)){

            log.debug("type:{} ------> dataBinder:{}",parameter.getType().getName(),dataBinder.getClass().getName());
            return dataBinder;
        }
        return null;
    }
}
