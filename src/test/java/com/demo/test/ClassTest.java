package com.demo.test;

/**
 * @author zhengzechao
 * @date 2018/6/28
 * Email ooczzoo@gmail.com
 */
public class ClassTest{


    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.demo.test.D"); //forName初始化
        ClassLoader.getSystemClassLoader().loadClass("com.demo.test.D"); //loadClass则不会初始化 因为都没有连接阶段完成

    }
}


class C{
    public C(){
        System.out.println("b");
    }
}

class D{
    static C b = new C();
    static{
        System.out.println("main");
    }

    public D(){
        System.out.println("a");
    }
}

