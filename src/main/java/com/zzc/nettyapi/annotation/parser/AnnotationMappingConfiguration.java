package com.zzc.nettyapi.annotation.parser;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.zzc.nettyapi.Configuration;
import com.zzc.nettyapi.annotation.ApiMapping;
import com.zzc.nettyapi.annotation.NameComponent;
import com.zzc.nettyapi.apiutil.ApiMethod;
import com.zzc.nettyapi.hotload.core.classloader.NettyServerClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author zhengzechao
 * @date 2018/6/2
 * Email ooczzoo@gmail.com
 */

public class AnnotationMappingConfiguration extends Configuration {

    private NettyServerClassLoader classLoader;
    private static final Logger log = LoggerFactory.getLogger(AnnotationMappingConfiguration.class);
    /**
     *
     */
    private String scanPaths;

    public AnnotationMappingConfiguration() {
    }
    public AnnotationMappingConfiguration(NettyServerClassLoader classLoader, String scanPaths) {
        this.classLoader = classLoader;
        this.scanPaths = scanPaths;
    }
    public AnnotationMappingConfiguration(String scanPaths) {
        this(null,scanPaths);
    }


    public NettyServerClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(NettyServerClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    //TODO 考虑是否需要去掉stream +lambda 这么写性能好像比普通写慢
    public List<Class<?>> getScanPackageAllClass() {
        ImmutableSet<ClassPath.ClassInfo> classInfos = null;
        try {


            ClassPath classpath = ClassPath.from(AnnotationMappingConfiguration.class.getClassLoader());
            String[] paths = scanPaths.split(",");
            classInfos = Arrays.stream(paths)
                    .map(classpath::getTopLevelClasses)
                    .reduce((t, r) -> {
                        ArrayList<ClassPath.ClassInfo> arrayList = Lists.newArrayList((ClassPath.ClassInfo[]) t.toArray());
                        arrayList.addAll(Lists.newArrayList((ClassPath.ClassInfo[]) r.toArray()));
                        return ImmutableSet.copyOf(arrayList);
                    })
                    .orElse(ImmutableSet.of());


            ClassLoader classLoader = this.classLoader;
            if (classLoader == null){
                classLoader = ClassLoader.getSystemClassLoader();
            }
            List<Class<?>> list = new ArrayList<>();

            for (ClassPath.ClassInfo classInfo : classInfos) {
                String name = classInfo.getName();
                Class<?> aClass = classLoader.loadClass(name);
                list.add(aClass);
            }
            return list;

        } catch (Exception e) {
            log.error("load class error!cause:{}", Throwables.getStackTraceAsString(e));
            return null;
        }

    }


    public Map<String, ApiMethod> registerMapping() {
        List<Class<?>> classes = getScanPackageAllClass();
        Map<String, ApiMethod> annotationMappings = Maps.newHashMap();
        try {
            for (Class<?> aClass : classes) {
                //忽略继承的
                Annotation[] annotations = aClass.getAnnotations();

                Class<? extends Annotation> annotation = Arrays.stream(annotations)
                        .map(Annotation::annotationType)
                        .filter(NameComponent.class::equals)
                        .findFirst()
                        .orElse(null);
                //该类含有NameComponent注解

                if (Objects.nonNull(annotation)) {
                    //解析方法
                    Method[] methods = aClass.getDeclaredMethods();
                    List<Method> mappingMethod = Arrays.stream(methods)
                            .collect(Collectors.toMap(t -> t, Method::getDeclaredAnnotations))
                            .entrySet()
                            .stream()
                            .filter(t -> Arrays.stream(t.getValue()).map(Annotation::annotationType).collect(Collectors.toList()).contains(ApiMapping.class))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());


                    for (Method method : mappingMethod) {

                        ApiMethod apiMethod = new ApiMethod(); //组装ApiMethod
                        apiMethod.setHandleClass(aClass);
                        apiMethod.setClassName(aClass.getName());
                        apiMethod.setMethod(method);
                        apiMethod.setMethodName(method.getName());
                        ApiMapping apiMapping = method.getDeclaredAnnotation(ApiMapping.class);
                        apiMethod.getSupportMethods().add(apiMapping.method().getDesc());
                        String url = apiMapping.value();
                        apiMethod.setUrl(url);
                        apiMethod.setSupportMethods(Sets.newHashSet(apiMapping.method().getDesc()));
                        ApiMethod previous = annotationMappings.put(apiMapping.value(), apiMethod);
                        if (Objects.nonNull(previous)){
                            throw new IllegalAccessException("mapping method conflict on the url:{}"+url);
                        }
                        apiMethod.setHandler(aClass.newInstance());
                        log.info("mapping url:{} on the class:{} - method:{}",url,aClass.getName(),method.getName());
                    }


                }
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return annotationMappings;


    }


}
