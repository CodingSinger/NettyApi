package com.demo.test;

/**
 * @author zhengzechao
 * @date 2018/6/28
 * Email ooczzoo@gmail.com
 */
public class ClassTest{


    public static void main(String[] args) throws ClassNotFoundException {
//        Class.forName("com.demo.test.D"); //forName初始化   1
//        Class.forName("com.demo.test.D",false,ClassLoader.getSystemClassLoader());  //2
        ClassLoader.getSystemClassLoader().loadClass("com.demo.test.D"); //loadClass则不会初始化 因为都没有连接阶段完成   3
        //https://stackoverflow.com/questions/8100376/class-forname-vs-classloader-loadclass-which-to-use-for-dynamic-loading
        //https://stackoverflow.com/questions/6638959/whats-the-difference-between-classloader-loadname-and-class-fornamename


    }



}

/**
 * 输出：
 * 语句1：
 *  b
 * main
 * 语句2和语句3：
 * 输出为空
 */

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

