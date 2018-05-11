package com.zzc.nettyapi.filter;

import com.zzc.nettyapi.request.RequestDetail;

/**
 * @author zhengzechao
 * @date 2018/5/11
 * Email ooczzoo@gmail.com
 */
public interface MethodFilter {

    Object after(Object result);

    Boolean before(RequestDetail requestDetail);

}
