package com.zzc.nettyapi.apiutil;

import com.alibaba.fastjson.JSONObject;
import com.zzc.nettyapi.Exception.HttpMethodNoSupportException;
import com.zzc.nettyapi.argument.HandleMethodArgumentParser;
import com.zzc.nettyapi.argument.MethodParameter;
import com.zzc.nettyapi.request.HttpRequestParser;
import com.zzc.nettyapi.request.RequestDetail;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ApiHandler {


    /**
     *     使用CAS操作的线程安全队列 高并发下建议使用Synchronized同步,因为CAS在高并发下会导致CAS操作失败过多而循环，而导致CPU工作大

     */
//    private ConcurrentLinkedQueue<RequestDetail> requestCache = new ConcurrentLinkedQueue<RequestDetail>();
    private Logger logger = LoggerFactory.getLogger(ApiHandler.class);


    /*参数包装器*/
    private HandleMethodArgumentParser argumentParser = new HandleMethodArgumentParser();

    /*请求解析器*/

    private HttpRequestParser requestParser = new HttpRequestParser();


    public byte[] handle(ChannelHandlerContext ctx, Object msg) {

        final HttpRequest httpRequest = (HttpRequest) msg;
        //将本次请求进行包装


        RequestDetail request = new RequestDetail(ctx,httpRequest);
        /**
         * TODO: 去掉request缓存模块或者增加相同资源的request缓存模块
         */


        requestParser.parse(request);

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
//
//            Executor executor = NettyServerBootStrap.getExecutor();
//            final RecycleTask recycleTask = new RecycleTask(request, requestCache);
//            if (executor == null) {
//                recycleTask.run();
//            } else {
//                executor.execute(recycleTask);
//
//            }

        }

        return encode(new Result(Constants.SUCCESS, result));

    }

    private Object invoke(RequestDetail request, ApiMethod api) throws HttpMethodNoSupportException, InvocationTargetException, IllegalAccessException {

        String methodName = request.getMethod();

        Method method = api.getMethod();
        Set<String> supportMethods = api.getSupportMethods();

        boolean supported   = supportMethods.stream().anyMatch(methodName::equals);
        if(!supported){
            throw new HttpMethodNoSupportException(api.getUrl()+" don't support this http method");
        }else{

            List<String> list = request.getParametersLine();

            MethodParameter[] methodParameters = api.getParameters();
            Object[] args = new Object[methodParameters.length];


            for (int i = 0; i < methodParameters.length; i++) {
                /**
                 * TODO: 转化器应该根据原始类型和目标类型进行匹配，并且还需要对输入参数进行验证是否有效性
                 *
                 */

//                args[i] =


//                args[i] = new SimpleConversion().convert(methodParameters[i], (String) list.get(i));

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
            Method method = Arrays.stream(clzz.getDeclaredMethods())
                    .filter((t)->t.getName().equals(apiMethod.getMethodName()))
                    .findFirst()
                    .get();



            apiMethod.setParameterNames(Stream.of(method.getParameters()).map(Parameter::getName).collect(Collectors.toList()));
            apiMethod.setParameterTypes(method.getParameterTypes());
            Object instance = clzz.newInstance();
            apiMethod.setMethod(method);
            apiMethod.setHandler(instance);
            MethodParameter[] methodParameters = argumentParser.parse(method);
            apiMethod.setParameters(methodParameters);



        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
        }


    }
}
