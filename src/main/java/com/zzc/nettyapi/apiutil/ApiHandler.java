package com.zzc.nettyapi.apiutil;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zzc.nettyapi.exception.HttpMethodNoSupportException;
import com.zzc.nettyapi.argument.utils.HandleMethodArgumentParser;
import com.zzc.nettyapi.argument.utils.MethodParameter;
import com.zzc.nettyapi.argument.resolver.ArgumentResolver;
import com.zzc.nettyapi.filter.MethodFilter;
import com.zzc.nettyapi.hotload.core.background.DetectModifyThread;
import com.zzc.nettyapi.hotload.core.classloader.NettyServerClassLoader;
import com.zzc.nettyapi.request.HttpRequestParser;
import com.zzc.nettyapi.request.RequestDetail;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ApiHandler {
    /**
     * 使用CAS操作的线程安全队列 高并发下建议使用Synchronized同步,因为CAS在高并发下会导致CAS操作失败过多而循环，而导致CPU工作大
     */
//    private ConcurrentLinkedQueue<RequestDetail> requestCache = new ConcurrentLinkedQueue<RequestDetail>();
    private Logger logger = LoggerFactory.getLogger(ApiHandler.class);

    private volatile NettyServerClassLoader classLoader;
    /*参数包装器*/
    private HandleMethodArgumentParser argumentParser = new HandleMethodArgumentParser();
    /*请求解析器*/
    private HttpRequestParser requestParser = new HttpRequestParser();
    private List<MethodFilter> filters;
    private CustomerConfiguration customerConfiguration = new CustomerConfiguration();

    public ApiHandler() throws InstantiationException, IllegalAccessException {
        classLoader = new NettyServerClassLoader(ClassLoader.getSystemClassLoader());
        init();
    }

    public void setClassLoader(NettyServerClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void init() throws IllegalAccessException, InstantiationException {
        customerConfiguration.load();
        new Thread(new DetectModifyThread(classLoader,this)).start();
        filters = new LinkedList<MethodFilter>();
        List<Class> filterClass = customerConfiguration.getMethodFilter();
        for (Class aClass : filterClass) {
            filters.add((MethodFilter) aClass.newInstance());
        }
    }

    public byte[] handle(ChannelHandlerContext ctx, Object msg) {
        final HttpRequest httpRequest = (HttpRequest) msg;
        //将本次请求进行包装
        RequestDetail request = new RequestDetail(ctx, httpRequest);
        /**
         * TODO: 去掉request缓存模块或者增加相同资源的request缓存模块
         */
        requestParser.parse(request);
        ApiMethod api = ApiRegistry.urlRegistrys.get(request.getUrl());
        if (api == null) {
            return encode(new Result(Constants.NOT_FOUND, null));
        }
        Boolean reload = ApiRegistry.reloadClass.get(api.getClassName());
        if (!api.getValid()||reload){
            //第一次访问
            synchronized (api) {
                //再次确认是否为空 有可能刚好别的线程实例化完成
                if (!api.getValid()||reload) {
                    loadMethodAndClass(api,reload);
                }
            }
        }
        Object result = null;
        try {
            result = invoke(request, api);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
        } catch (HttpMethodNoSupportException e) {
            result = new Result(Constants.METHOD_NOT_SUPPORT, null);
            logger.error(e.getMessage());
        } finally {
            /**
             * TODO:可以尝试另开线程进行提交到缓存 避免线程争用影响服务器响应时间
             */
///
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
        //执行MethodFilter的before操作
        Boolean before = doBefore(request, api);
        if (Objects.equals(before, Boolean.FALSE)) {
            return null;
        }
        String methodName = request.getMethod();
        Method method = api.getMethod();
        Set<String> supportMethods = api.getSupportMethods();
        boolean supported = supportMethods.stream().anyMatch(methodName::equals);
        if (!supported) {
            throw new HttpMethodNoSupportException(api.getUrl() + " don't support this http method");
        } else {
            List<String> list = request.getParametersLine();
            MethodParameter[] methodParameters = api.getParameters();
            Object[] args = new Object[methodParameters.length];


            try {
                for (int i = 0; i < methodParameters.length; i++) {
                    /**
                     * TODO: 转化器应该根据原始类型和目标类型进行匹配，并且还需要对输入参数进行验证是否有效性
                     *
                     */
                    MethodParameter methodParameter = methodParameters[i];
                    if (ArgumentResolver.supportMethodParameter(ArgumentResolver.argumentResolvers, methodParameter)) {
                        args[i] = ArgumentResolver.resolveMethodParameter(methodParameter, request);
                    }
                }
            } catch (Exception e) {
                logger.error("resolve method crash ,cause:{}", Throwables.getStackTraceAsString(e));
            }
            //方法正式执行
            Object result = null;
            //进行方法前拦截器的执行
            result = method.invoke(api.getHandler(), args);
            //进行方法之后拦截器的执行 对结果进行处理
            result = this.doAfter(result);
            return result;
        }
    }

    private Boolean doBefore(RequestDetail requestDetail, ApiMethod apiMethod) {
        return filters.stream()
                .map(methodFilter -> methodFilter.before(requestDetail))
                .anyMatch(Boolean.FALSE::equals) == false;
    }

    private Object doAfter(Object result) {
        for (MethodFilter filter : filters) {
            result = filter.after(result);
        }
        return result;
    }

    private byte[] encode(Object result) {
        String json = JSONObject.toJSONString(result);
        return json.getBytes();

    }

    private void loadMethodAndClass(ApiMethod apiMethod, Boolean reload) {

        try {
            if (Objects.isNull(apiMethod.getMethod())||reload){ //非注解加载的方式method会为空


                //加载class

//                Class clzz = Class.forName(apiMethod.getClassName());
                Class clzz = classLoader.loadClass(apiMethod.getClassName());
                System.out.println("重新加载");
                apiMethod.setHandleClass(clzz);
                Method method = Arrays.stream(clzz.getDeclaredMethods())
                        .filter((t) -> t.getName().equals(apiMethod.getMethodName()))
                        .findFirst()
                        .get();
                apiMethod.setMethod(method);
                Object instance = clzz.newInstance();
                apiMethod.setHandler(instance);
            }
            Method method = apiMethod.getMethod();
            apiMethod.setParameterNames(Stream.of(method.getParameters()).map(Parameter::getName).collect(Collectors.toList()));
            apiMethod.setParameterTypes(method.getParameterTypes());
            MethodParameter[] methodParameters = argumentParser.parse(method);
            apiMethod.setParameters(methodParameters);
            apiMethod.setValid(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
        }


    }


}
