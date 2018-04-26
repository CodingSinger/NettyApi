package com.zzc.nettyapi.argument.binder;

import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.argument.utils.BeanWrapper;
import com.zzc.nettyapi.argument.utils.BeanWrapperFactory;
import com.zzc.nettyapi.argument.utils.PropertyHandler;
import com.zzc.nettyapi.request.RequestDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class ModelAttributeDataBinder implements DataBinder {


    private static final Logger log = LoggerFactory.getLogger(ModelAttributeDataBinder.class);
    @Override
    public void doBinder(Object attribute, RequestDetail requestDetail, Class type) {
        /**
         * TODO 将请求参数中的键和type中的属性值对应起来 注入到attribute中
         */
        Map<String, List<String>> parameterMap = requestDetail.getParamters();
        final Set<Map.Entry<String, List<String>>> entries = parameterMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String name = entry.getKey();
            List<String> list = entry.getValue();
            if (list.size() >= 0) {
                String sourceValue = list.get(0);
                setProperty(name, sourceValue, attribute, type);
            } else {
                //注入的是数组

                /**
                 * TODO 目前只支持简单数据属性的注入 list,或者bean属性的注入推后
                 */
            }
        }
    }

    private void setProperty(String name, String sourceValue, Object attribute, Class type) {

        /**
         * TODO
         */
        //获取attribute中对应该name的set方法
        //如果beanWrapper中包括该name的propertyHandler,直接提取
        //否则的话去该class类型解析得到该propertyHandler
        //对其进行判断处理
        BeanWrapper beanWrapper = BeanWrapperFactory.getDataFactory(type);
        PropertyHandler propertyHandler = beanWrapper.getPropertyHandler(name);

        if (Objects.isNull(propertyHandler)){

        }
        if(propertyHandler.isWritable()){
            // TODO 类型转换、注入
            Method writeMethod = propertyHandler.getWriteMethod();
        }else{
            log.info("property name {} cant inject,because no set method",name);
        }
    }

    @Override
    public Object convertIfNecessary(Class type, String value) throws ConvertException {
        return null;
    }
}
