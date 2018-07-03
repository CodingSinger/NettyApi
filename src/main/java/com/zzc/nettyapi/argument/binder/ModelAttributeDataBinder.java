package com.zzc.nettyapi.argument.binder;

import com.google.common.base.Throwables;
import com.zzc.nettyapi.argument.conversion.DefaultConversion;
import com.zzc.nettyapi.argument.utils.*;
import com.zzc.nettyapi.exception.ConvertException;
import com.zzc.nettyapi.request.RequestDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author zhengzechao
 * @date 2018/4/22
 * Email ooczzoo@gmail.com
 */
public class ModelAttributeDataBinder implements DataBinder {


    private DefaultConversion conversion;
    private PropertyAccessor propertyAccessor;

    public ModelAttributeDataBinder(DefaultConversion conversion) {
        this.propertyAccessor = new PropertyAccessor();
        this.conversion = conversion;
    }

    private static final Logger log = LoggerFactory.getLogger(ModelAttributeDataBinder.class);

    @Override
    public void doBinder(Object attribute, RequestDetail requestDetail, Class type) throws Exception {
        /**
         * TODO 将请求参数中的键和type中的属性值对应起来 注入到attribute中 目前只支持一层注入
         */
        Map<String, List<String>> parameterMap = requestDetail.getParamters();
        final Set<Map.Entry<String, List<String>>> entries = parameterMap.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String name = entry.getKey();
            List<String> list = entry.getValue();
            Object sourceValue = null;
            String[] strings = new String[list.size()];
            sourceValue = list.toArray(strings);
            setNestedProperty(name,attribute, sourceValue);
        }
    }


    private void setNestedProperty(String nestedPropertyPath, Object attribute,Object sourceValue) throws Exception {

        Class type = attribute.getClass();
        final int index = nestedPropertyPath.indexOf(".");
        if (index < 0) {
            setProperty(nestedPropertyPath,sourceValue,attribute,type);

        } else {
            String attributeName = nestedPropertyPath.substring(0,index);
            BeanWrapper beanWrapper = BeanWrapperFactory.getDataFactory(type);
            PropertyHandler propertyHandler = beanWrapper.getPropertyHandler(attributeName);
            if (Objects.isNull(propertyHandler)) {
                propertyHandler = ReflectionTool.resolveProperty(attributeName, type, beanWrapper);
                if (Objects.isNull(propertyHandler)) {
                    log.info("Class:{} no property for name: {}", type.getName(), attributeName);
                }

            }
            Object nestedValue = propertyAccessor.getNestedPropertyValue(attribute,propertyHandler);
            nestedPropertyPath = nestedPropertyPath.substring(index+1);
            setNestedProperty(nestedPropertyPath,nestedValue,sourceValue);

        }

    }


    /**
     * 设置attribute中propertyName的值 如果是嵌套属性的话 该方法会递归到最底层才调用
     * @param name
     * @param sourceValue
     * @param attribute
     * @param attributeType
     * @throws Exception
     */
    private void setProperty(String name, Object sourceValue, Object attribute, Class attributeType) throws Exception {



        //获取attribute中对应该name的set方法
        //如果beanWrapper中包括该name的propertyHandler,直接提取
        //否则的话去该class类型解析得到该propertyHandler
        //对其进行判断处理
        BeanWrapper beanWrapper = BeanWrapperFactory.getDataFactory(attributeType);
        //缓存中是否有存储过
        PropertyHandler propertyHandler = beanWrapper.getPropertyHandler(name);

        //缓存中没有 尝试重新解析
        if (Objects.isNull(propertyHandler)) {
            //从该bean类型找找到是该key的属性
            propertyHandler = ReflectionTool.resolveProperty(name, attributeType, beanWrapper);
            if (Objects.isNull(propertyHandler)) {

                log.info("Class:{} no property for name: {}", attributeType.getName(), name);
                return;
            }

        }
        Class propertyType = propertyHandler.getType();
        if (!propertyType.isArray()) {
            //TODO 如果propertyType是list ,bean类型推迟
            if (!ReflectionTool.isSimpleProperty(propertyType)) {
                //非简单类型
            }
            sourceValue = ((String[]) sourceValue)[0];
        }
        if (propertyHandler.isWritable()) {
            // TODO 类型转换、注入 将Conversion从DataBinder中解耦出去 以支持各处的类型转化
            Object targetValue = null;
            Method writeMethod = null;
            try {
                targetValue = conversion.convertIfNecessary(sourceValue.getClass(), propertyType, sourceValue);
                writeMethod = propertyHandler.getWriteMethod();
                writeMethod.invoke(attribute, targetValue);
            } catch (Exception e) {
                if (e instanceof ConvertException) {
                    log.error("inject value:{} in Class:{}.{} error!,Cause:{}", sourceValue, attributeType.getName(), name, Throwables.getStackTraceAsString(e));
                } else {
                    log.error("invoke set Method:{} error,Cause:{}", writeMethod.getName(), Throwables.getStackTraceAsString(e));
                }
                throw e;
            }

        } else {
            log.info("property name {} cant inject,because no set method", name);
        }
    }


}
