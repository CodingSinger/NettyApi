package com.zzc.test.entity;

import java.util.Arrays;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */
public class Person {
    private String name;

    private Cat cat;

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    private String[] strs;
    public String getName() {
        return name;
    }

    public void setStrs(String[] strs) {
        this.strs = strs;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", cat=" + cat +
                ", strs=" + Arrays.toString(strs) +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }
}
