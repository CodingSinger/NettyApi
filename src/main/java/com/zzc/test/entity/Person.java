package com.zzc.test.entity;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */
public class Person {
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
