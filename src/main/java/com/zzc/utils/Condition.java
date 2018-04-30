package com.zzc.utils;

import java.util.Arrays;

/**
 * @author zhengzechao
 * @date 2018/4/29
 * Email ooczzoo@gmail.com
 */
public class Condition {
    public static Boolean and(Boolean... conditions) {

        return Arrays.stream(conditions).allMatch(Boolean.TRUE::equals);
    }

    public static Boolean or(Boolean... conditions) {
        return Arrays.stream(conditions).anyMatch(Boolean.TRUE::equals);
    }

}
