package com.zzc.nettyapi.request;

import com.zzc.nettyapi.apiutil.ApiMethod;
import com.zzc.nettyapi.apiutil.Recyclable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */


/**
 * 查看tomcat请求的request的是否被缓存
 */
public class RequestDetail implements Recyclable {
    private final static Logger logger = LoggerFactory.getLogger(RequestDetail.class);
    private Map<String, List<String>> paramters;
    private String method;
    private String clientIp;
    private String url;
    private Date date;
    private HttpVersion httpVersion;
    private ChannelHandlerContext context;
    private List<String> parametersLine;
    private HttpRequest httpRequest;

    /* 请求参数键值对*/
    private HashMap<String, String> requestParameters;
    private ApiMethod api;

    public RequestDetail(ChannelHandlerContext context, HttpRequest request) {
        this.httpRequest = request;

        this.context = context;

    }


    public void reset(ChannelHandlerContext context, HttpRequest httpRequest, boolean reparse) {
        this.context = context;
        this.httpRequest = httpRequest;


    }


    @Override
    public void reset() {
        this.parametersLine = null;
        this.api = null;

        this.url = null;
        reset(null, null, false);


    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public ApiMethod getApi() {
        return api;
    }

    public void setApi(ApiMethod api) {
        this.api = api;
    }

    public List<String> getParametersLine() {
        return parametersLine;
    }

    public void setParametersLine(List<String> parametersLine) {
        this.parametersLine = parametersLine;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public Map<String, List<String>> getParamters() {
        return paramters;
    }

    public void setParamters(Map<String, List<String>> paramters) {
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
