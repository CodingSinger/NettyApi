package com.zzc.test.Controller;

import com.zzc.nettyapi.annotation.ApiMapping;
import com.zzc.nettyapi.annotation.NameComponent;

/**
 * @author zhengzechao
 * @date 2018/6/2
 * Email ooczzoo@gmail.com
 */
@NameComponent
public class AnnotationController2 {
    @ApiMapping("/run")
    public void run(){
        System.out.println("run");
    }
}
