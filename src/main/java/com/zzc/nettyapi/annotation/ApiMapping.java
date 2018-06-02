package com.zzc.nettyapi.annotation;


import javax.persistence.Table;
import java.lang.annotation.*;

/**
 * @author zhengzechao
 * @date 2018/6/2
 * Email ooczzoo@gmail.com
 */

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMapping {

    enum RequestMethod{
        Get("GET"),
        Post("POST");

        public String getDesc() {
            return desc;
        }

        private String desc;

        RequestMethod(String desc) {
            this.desc = desc;
        }
    }

    String value() default "";
    RequestMethod method() default RequestMethod.Get;
}
