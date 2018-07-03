package com.demo.test;

import com.zzc.nettyapi.argument.utils.ReflectionTool;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/7/2
 * Email ooczzoo@gmail.com
 */
public class TestArrayToArrayConvert {


    @Test
    public void testPrimitive() {
        int[] i = new int[]{1,2};


        System.out.println(i.getClass().getComponentType());

        int j = 1;
        Object o = j; //这样可以
        Object o1 = i; //这样也可以 因为i是数组，是引用类型
//        Object[] obj = i;   //无法强制 因为当i为原始类型的数组时，只有原始类型的数组才可以进行赋值
        Integer[] is = (Integer[]) ReflectionTool .toObjectArray(i); //toObjectArray将原始类型进行包装成Object
        System.out.println(is);
    }


    @Test
    public void testArrayReflect() {
//        final Object[] o1 = (Object[]) Array.newInstance(int.class, 10);  //报错无法转换 原因上面讲过
        final int[] o = (int[]) Array.newInstance(int.class, 10);
        o[1] =1;
        System.out.println(Arrays.toString(o));

    }
}
