package com.zzc.nettyapi.apiutil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */


/**
 * 查看tomcat请求的request的是否被缓存
 */
public class RequestDetail implements Recyclable{
    private final static Logger logger = LoggerFactory.getLogger(RequestDetail.class);
    private HashMap<String,Object> paramters = new HashMap<>();
    private String method ;
    private String clientIp;
    private String url;
    private Date date;
    private HttpVersion httpVersion;
    private ChannelHandlerContext context;
    private List<Object> parametersLine;
    private HttpRequest httpRequest;

    public RequestDetail(ChannelHandlerContext context,HttpRequest request) {
        this.httpRequest = request;

        this.context = context;
        parseRequest();


    }

    private void parseRequest() {
        this.parametersLine = new LinkedList<>();
        StringBuilder builder = new StringBuilder();
        this.httpVersion = httpRequest.protocolVersion();
        String uri = httpRequest.uri();
        /**
         * TODO: 解析成resultful API形式。将uri格式和RequestDetail建立映射，以后根据用户uri直接得到对应的RequestDetail以及进行参数解析。
         */




        this.date = new Date();

        this.method = httpRequest.method().name();
        builder.append("date:");
        builder.append(date.toString());
        builder.append("---uri:");
        builder.append(uri);

        logger.info(builder.toString());

        LinkedList<ApiMethod> list = new LinkedList<>(ApiRegistry.urlRegistrys.values());


        for (ApiMethod m : list) {

            Pattern pattern = Pattern.compile("^" + m.getRegex() + "$");
            Matcher matcher = pattern.matcher(uri);
            if (matcher.find()) {
                this.url = m.getUrl();
                if (matcher.groupCount() > 0) {
                    for (int i = 0; i < matcher.groupCount(); i++) {
//                        this.paramters.put(m.getParameterNames().get(i),matcher.group(i+1));

                          this.parametersLine.add(matcher.group(i+1));
                    }
                }
                break;
            }
        }

    }


    public void reset(ChannelHandlerContext context,HttpRequest httpRequest,boolean reparse){
        this.context = context;
        this.httpRequest = httpRequest;

        if(reparse){
            parseRequest();
        }

    }


    @Override
    public void reset(){
        this.parametersLine = null;
        reset(null,null,false);

    }


    public List<Object> getParametersLine() {
        return parametersLine;
    }

    public void setParametersLine(List<Object> parametersLine) {
        this.parametersLine = parametersLine;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public HashMap<String, Object> getParamters() {
        return paramters;
    }

    public void setParamters(HashMap<String, Object> paramters) {
        this.paramters = paramters;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
