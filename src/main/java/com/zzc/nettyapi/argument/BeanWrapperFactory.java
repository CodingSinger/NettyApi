package com.zzc.nettyapi.argument;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/25
 * Email ooczzoo@gmail.com
 */
public class BeanWrapperFactory {




    private static final Map<Class, BeanWrapper> cacheBeanWrapper = Maps.newHashMap();

    public static BeanWrapper getDataFactory(Class goal) {
        BeanWrapper beanWrapper = cacheBeanWrapper.get(goal);
        if (Objects.isNull(beanWrapper)) {
            synchronized (cacheBeanWrapper) {
                if (!cacheBeanWrapper.containsKey(goal)) {
                    beanWrapper = new BeanWrapper(goal);
                    cacheBeanWrapper.put(goal, beanWrapper);
                }
            }
        }
        return beanWrapper;
    }
}
