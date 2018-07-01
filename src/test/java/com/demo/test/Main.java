package com.demo.test;

import sun.misc.Launcher;

import java.util.Arrays;

/**
 * @author zhengzechao
 * @date 2018/5/4
 * Email ooczzoo@gmail.com
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(Arrays.toString(Launcher.getBootstrapClassPath().getURLs()));
        ClassLoader.getSystemClassLoader().loadClass("com.zzc.test.Controller.AnnotationController");
        Object o = new Object();
        synchronized (o){

        }
        hello();
    }


    public synchronized static void hello(){

    }
}
