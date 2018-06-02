package com.zzc.nettyapi.annotation;

import java.lang.annotation.*;

/**
 * @author zhengzechao
 * @date 2018/6/2
 * Email ooczzoo@gmail.com
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NameComponent {

     String value() default "";


}
