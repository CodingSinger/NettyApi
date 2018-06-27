package com.zzc.test.Controller;

import com.zzc.nettyapi.annotation.ApiMapping;
import com.zzc.nettyapi.annotation.NameComponent;
import com.zzc.nettyapi.hotload.basic.Controller;

/**
 * @author zhengzechao
 * @date 2018/6/2
 * Email ooczzoo@gmail.com
 */

@NameComponent
public class AnnotationController implements Controller {

    @ApiMapping(value = "/test")
    public void test(){
        System.out.println("hello");
    }


    @ApiMapping(value = "/todo",method = ApiMapping.RequestMethod.Post)
    public void todo(){
        System.out.println("todo");
    }

}
