package com.zzc.test.Controller;

import com.zzc.nettyapi.annotation.ApiMapping;
import com.zzc.nettyapi.annotation.NameComponent;
import com.zzc.nettyapi.hotload.basic.Controller;
import com.zzc.test.entity.Person;

import java.util.Arrays;

/**
 * @author zhengzechao
 * @date 2018/6/2
 * Email ooczzoo@gmail.com
 */

@NameComponent
public class AnnotationController implements Controller {

    @ApiMapping(value = "/test")
    public Person test(String[] ss,Integer[] is,Person person){
//        System.out.println(ss);
        System.out.println(person);
        System.out.println(Arrays.toString(is));
        System.out.println(Arrays.toString(ss));
        System.out.println("hello13");
        return person;
    }


    @ApiMapping(value = "/todo",method = ApiMapping.RequestMethod.Post)
    public void todo(){
        System.out.println("todo");
    }

}
