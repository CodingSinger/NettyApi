package com.zzc.nettyapi.argument.conversion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zzc.nettyapi.argument.conversion.convert.ArrayToArrayConverter;
import com.zzc.nettyapi.argument.conversion.convert.ObjectToStringConverter;
import com.zzc.nettyapi.exception.ConvertException;
import com.zzc.nettyapi.argument.conversion.convert.Converter;
import com.zzc.nettyapi.argument.conversion.convert.StringToNumberConverter;
import com.zzc.nettyapi.argument.conversion.util.ConvertKeyPair;
import java.util.Map;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengzechao
 * @date 2018/4/27
 * Email ooczzoo@gmail.com
 */
public class DefaultConversion implements Conversion {


    /**
     * 已经解析的缓存
     */
    private ConcurrentHashMap<ConvertKeyPair, Converter> converterCache = new ConcurrentHashMap<ConvertKeyPair, Converter>();

    /**
     * 因为ConcurrentHashMap不允许寸null的value，则我们制造个无用Convert，当做ConvertKeyPair无法匹配对应的Converter
     */

    private final NoMatchConvert NO_MATCH = new NoMatchConvert();


    /**
     * 当前可用已注册的转换器列表
     */
    private List<Converter> convertersRegistry = Lists.newLinkedList();


    private Map<Class,Class> primitiveToObjectMap = Maps.newHashMap();

    @Override
    public Boolean canCanvert(Class sourceClass, Class targetClass) {


        Converter converter = getConvert(sourceClass, targetClass);

        if (Objects.nonNull(converter)){
//            converterCache.put(new ConvertKeyPair(sourceClass,targetClass),converter);
            return true;
        }
        return false;

    }

    private Class resolvePrimitiveClass(Class targetClass) {
        if(targetClass.isPrimitive()){
            return primitiveToObjectMap.get(targetClass);
        }
        return targetClass;
    }

    /**
     * 从缓存中或者注册转换器中心中找
     * @param sourceClass
     * @param targetClass
     * @return
     */
    @Override
    public Converter getConvert(Class sourceClass, Class targetClass) {

        ConvertKeyPair keyPair = new ConvertKeyPair(sourceClass, targetClass);
        Converter converter = converterCache.get(keyPair);
        if (Objects.nonNull(converter)) {
            return converter != NO_MATCH ? converter : null;
        }

        //缓存中没有 则需要去已经注册的convert里面找
        converter = convertersRegistry.stream()
                .filter(t -> t.match(targetClass, sourceClass))
                .findFirst().orElse(null);
        if (Objects.nonNull(converter)){
            converterCache.put(keyPair,converter);
            return converter;
        }

        converterCache.put(keyPair,NO_MATCH);
        return null;
    }

    @Override
    public Object convertIfNecessary(Class sourceClass,Class targetClass, Object value ) throws ConvertException {

        Object obj = value;
        if(!Objects.equals(targetClass,sourceClass)){
            obj = convert(sourceClass,targetClass,value);
        }
        return obj;
    }

    @Override
    public Object convert(Class sourceClass,Class targetClass, Object value) throws ConvertException {

        Converter converter = null;
        targetClass = resolvePrimitiveClass(targetClass);
        if(canCanvert(sourceClass,targetClass)){
            converter = getConvert(sourceClass,targetClass);
        }else{
            throw new ConvertException("can't find convert");
        }
        Object obj = converter.convert(value,targetClass);
        return obj;

    }

    public DefaultConversion() {
        initConvertersRegistry();
        initPrimitiveObjectMap();
    }

    /**
     * 初始化基本类型和包装类型列表
     */
    private void initPrimitiveObjectMap() {
        primitiveToObjectMap.put(int.class,Integer.class);
        primitiveToObjectMap.put(float.class,Float.class);
        primitiveToObjectMap.put(double.class,Double.class);
        primitiveToObjectMap.put(char.class,Character.class);
        primitiveToObjectMap.put(long.class,Long.class);
        primitiveToObjectMap.put(short.class,Short.class);
        primitiveToObjectMap.put(byte.class,Byte.class);
        primitiveToObjectMap.put(boolean.class,Boolean.class);
    }


    /**
     * 初始化转化器列表
     */
    private void initConvertersRegistry() {

        /**
         * TODO 后续进行从配置中加载解析器，从而运行用户自己添加对应解析器，先写死
         */
        convertersRegistry.add(new StringToNumberConverter());
        convertersRegistry.add(new ArrayToArrayConverter(this));
        convertersRegistry.add(new ObjectToStringConverter());


    }


    private final static class NoMatchConvert implements Converter{

        @Override
        public Object convert(Object source, Class targetClass) throws ConvertException {
            return null;
        }

        @Override
        public boolean match(Class targetClass, Class sourceClass) {
            return false;
        }
    }

}
