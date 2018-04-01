package com.zzc.nettyapi.apiutil;

import com.alibaba.fastjson.JSONObject;
import com.zzc.nettyapi.Exception.HttpMethodNoSupportException;
import com.zzc.nettyapi.conversion.SimpleConversion;
import com.zzc.nettyapi.nettyservice.NettyServerBootStrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ApiHandler {


    /**
     *     使用CAS操作的线程安全队列 高并发下建议使用Synchronized同步,因为CAS在高并发下会导致CAS操作失败过多而循环，而导致CPU工作大

     */
    private ConcurrentLinkedQueue<RequestDetail> requestCache = new ConcurrentLinkedQueue<RequestDetail>();
    private Logger logger = LoggerFactory.getLogger(ApiHandler.class);

    public byte[] handle(ChannelHandlerContext ctx, Object msg) {

        final HttpRequest httpRequest = (HttpRequest) msg;
        //将本次请求进行包装


        /**
         * TODO:RequetsCache应该是个映射关系的容器 能根据请求url得到相应的RequestDetail
         */
        RequestDetail request = requestCache.poll();
        //为空则重新new
        if (request == null) {
            request = new RequestDetail(ctx, httpRequest);

        } else {
            request.reset(ctx, httpRequest, true);
        }
        ApiMethod api = ApiRegistry.urlRegistrys.get(request.getUrl());
        if (api == null) {
            return encode(new Result(Constants.NOT_FOUND, null));
        }
        if (api.getMethod() == null) {
            //第一次访问
            synchronized (api) {
                //再次确认是否为空 有可能刚好别的线程实例化完成
                if (api.getMethod() == null) {
                    loadMethodAndClass(api);
                }
            }

        }

        Method method = api.getMethod();
        Object result = null;
        try {

            result = invoke(request, api);

        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
        } catch (HttpMethodNoSupportException e) {
            result = new Result(Constants.METHOD_NOT_SUPPORT,null);
            logger.error(e.getMessage());
        } finally {
            /**
             * TODO:可以尝试另开线程进行提交到缓存 避免线程争用影响服务器响应时间
             */

            Executor executor = NettyServerBootStrap.getExecutor();
            final RecycleTask recycleTask = new RecycleTask(request, requestCache);
            if (executor == null) {
                recycleTask.run();
            } else {
                executor.execute(recycleTask);

            }

        }

        return encode(new Result(Constants.SUCCESS, result));

    }

    private Object invoke(RequestDetail request, ApiMethod api) throws HttpMethodNoSupportException, InvocationTargetException, IllegalAccessException {

        String methodName = request.getMethod();

        Set<String> supportMethods = api.getSupportMethods();

        boolean supported   = supportMethods.stream().anyMatch(methodName::equals);
        if(!supported){
            throw new HttpMethodNoSupportException(api.getUrl()+" don't support this http method");
        }else{

            List<Object> list = request.getParametersLine();

            Method method = api.getMethod();

            Class[] parameterTypes = method.getParameterTypes();

            Object[] args = new Object[parameterTypes.length];


            for (int i = 0; i < parameterTypes.length; i++) {
                /**
                 * TODO: 转化器应该根据原始类型和目标类型进行匹配获得
                 */
                args[i] = new SimpleConversion().convert(parameterTypes[i], (String) list.get(i));

            }

            return method.invoke(api.getHandler(),args);
        }



    }

    private byte[] encode(Object result) {
        String json = JSONObject.toJSONString(result);
        return json.getBytes();

    }


    private void loadMethodAndClass(ApiMethod apiMethod) {

        try {
            Class clzz = Class.forName(apiMethod.getClassName());
            Method method = Arrays.stream(clzz.getDeclaredMethods()).filter((t)->t.getName().equals(apiMethod.getMethodName())).findFirst().get();
            apiMethod.setParameterTypes(method.getParameterTypes());
            Object instance = clzz.newInstance();
            apiMethod.setMethod(method);
            apiMethod.setHandler(instance);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
        }


    }
}
