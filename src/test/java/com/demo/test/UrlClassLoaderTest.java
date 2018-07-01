package com.demo.test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * @author zhengzechao
 * @date 2018/6/28
 * Email ooczzoo@gmail.com
 */
public class UrlClassLoaderTest {

    public static Exception j(){
        return new Exception("sas");
    }
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {


        File file = new File("/Users/zhengzechao/Downloads");
        URLClassLoader loader = new URLClassLoader(new URL[]{ file.toURL()});
        final Class<?> aClass = loader.loadClass("mytest.A.Lambda$$Lambda$2");
        Arrays.stream(aClass.getDeclaredMethods()).map(Method::getName).forEach(System.out::println);
        Object obj = aClass.newInstance();
        System.out.println(aClass);
    }
}
