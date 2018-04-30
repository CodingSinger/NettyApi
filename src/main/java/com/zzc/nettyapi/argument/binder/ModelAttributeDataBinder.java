package com.zzc.nettyapi.argument.binder;

import com.google.common.base.Converter;
import com.google.common.base.Throwables;
import com.zzc.nettyapi.Exception.ConvertException;
import com.zzc.nettyapi.argument.conversion.DefaultConversion;
import com.zzc.nettyapi.argument.resolver.ArgumentResolver;
import com.zzc.nettyapi.argument.utils.BeanWrapper;
import com.zzc.nettyapi.argument.utils.BeanWrapperFactory;
import com.zzc.nettyapi.argument.utils.PropertyHandler;
import com.zzc.nettyapi.argument.utils.ReflectionTool;
import com.zzc.nettyapi.request.RequestDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
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


    private DefaultConversion conversion;

    public ModelAttributeDataBinder(DefaultConversion conversion) {
        this.conversion = conversion;
    }

    private static final Logger log = LoggerFactory.getLogger(ModelAttributeDataBinder.class);
    @Override
    public void doBinder(Object attribute, RequestDetail requestDetail, Class type) throws Exception {
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

    private void setProperty(String name, String sourceValue, Object attribute, Class attributeType) throws Exception {

        /**
         * TODO
         */
        //获取attribute中对应该name的set方法
        //如果beanWrapper中包括该name的propertyHandler,直接提取
        //否则的话去该class类型解析得到该propertyHandler
        //对其进行判断处理
        BeanWrapper beanWrapper = BeanWrapperFactory.getDataFactory(attributeType);
        PropertyHandler propertyHandler = beanWrapper.getPropertyHandler(name);

        if (Objects.isNull(propertyHandler)){

            propertyHandler = ReflectionTool.resolveProperty(name,attributeType,beanWrapper);

            if (Objects.isNull(propertyHandler)){

                log.info("Class:{} no property for name: {}",attributeType.getName(),name);
                return;
            }

        }
        if(propertyHandler.isWritable()){
            // TODO 类型转换、注入 将Conversion从DataBinder中解耦出去 以支持各处的类型转化
            Object targetValue = null;
            Method writeMethod = null;
            try {
                targetValue = conversion.convertIfNecessary(String.class,propertyHandler.getType(),sourceValue);
                writeMethod = propertyHandler.getWriteMethod();
                writeMethod.invoke(attribute,targetValue);
            } catch (Exception e) {
                if(e instanceof ConvertException){
                    log.error("inject value:{} in Class:{}.{} error!,Cause:{}",sourceValue,attributeType.getName(),name, Throwables.getStackTraceAsString(e));
                }else{
                    log.error("invoke set Method:{} error,Cause:{}",writeMethod.getName(),Throwables.getStackTraceAsString(e));
                }
                throw e;
            }



        }else{
            log.info("property name {} cant inject,because no set method",name);
        }
    }


}
