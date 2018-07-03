package com.demo.test;

import com.zzc.nettyapi.hotload.core.classloader.NettyServerClassLoader;
import org.junit.Test;

/**
 * @author zhengzechao
 * @date 2018/6/29
 * Email ooczzoo@gmail.com
 */
public class TestClassLoader {
    @Test
    public void testload() throws ClassNotFoundException {
        NettyServerClassLoader loader = new NettyServerClassLoader(TestClassLoader.class.getClassLoader());
        Class c = loader.loadClass("com.demo.test.TestRegistry");
        System.out.println(c.getClassLoader());
        final Class<?> aClass = loader.loadClass("com.zzc.test.Controller.UserController");
        final Class<?> aClass1 = loader.loadClass("com.zzc.test.Controller.UserController");
        System.out.println(aClass == aClass1);
        NettyServerClassLoader loader1 = new NettyServerClassLoader(TestClassLoader.class.getClassLoader());
        final Class<?> aClass2 = loader1.loadClass("com.zzc.test.Controller.UserController");
        System.out.println(aClass2 == aClass);
        System.out.println(aClass.getClassLoader());
        System.out.println(c);

    }
}