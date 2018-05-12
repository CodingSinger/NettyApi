package com.zzc.test.filter;

import com.zzc.nettyapi.filter.MethodFilter;
import com.zzc.nettyapi.request.RequestDetail;

/**
 * @author zhengzechao
 * @date 2018/5/12
 * Email ooczzoo@gmail.com
 */
public class FirstMethodFilter implements MethodFilter{

    @Override
    public Object after(Object result) {
        System.out.println("do after");
        return result;
    }

    @Override
    public Boolean before(RequestDetail requestDetail) {
        System.out.println("do berore");
        return true;
    }
}
