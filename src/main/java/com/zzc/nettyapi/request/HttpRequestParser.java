package com.zzc.nettyapi.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zzc.nettyapi.apiutil.ApiMethod;
import com.zzc.nettyapi.apiutil.ApiRegistry;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhengzechao
 * @date 2018/4/18
 * Email ooczzoo@gmail.com
 */
public class HttpRequestParser {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);

    public void parse(RequestDetail requestDetail) {
        HttpRequest request = requestDetail.getHttpRequest();
        parseRequest(requestDetail, request);
        parseParameterByMethod(requestDetail, (FullHttpRequest)request);
    }

    /*根据request路径解析请求 包括url参数以及路径参数和请求体参数*/
    private void parseRequest(RequestDetail request, HttpRequest httpRequest) {
        request.setParametersLine(new LinkedList<>());
        StringBuilder builder = new StringBuilder();
        request.setHttpVersion(httpRequest.protocolVersion());
        String uri = httpRequest.uri();
        /**
         * TODO: 解析成resultful API形式。将uri格式和RequestDetail建立映射，以后根据用户uri直接得到对应的RequestDetail以及进行参数解析。
         */

        Date date = new Date();
        request.setDate(date);
        request.setMethod(httpRequest.method().name());
        builder.append("date:");
        builder.append(date.toString());
        builder.append("---uri:");
        builder.append(uri);

        log.info(builder.toString());

        LinkedList<ApiMethod> list = new LinkedList<>(ApiRegistry.urlRegistrys.values());
        int index = uri.indexOf("?");
        String temp = uri;
        if (index != -1) {
            temp = uri.substring(0, uri.indexOf("?"));
        }
        for (ApiMethod m : list) {

            Pattern pattern = Pattern.compile("^" + m.getRegex() + "$");

            Matcher matcher = pattern.matcher(temp);

            if (matcher.find()) {
                request.setApi(m);
                request.setUrl(m.getUrl());
                //解析路径参数 需要截断"?"之后的
                if (matcher.groupCount() > 0) {


                    /**
                     * 将数据放到Map的映射关系
                     */
                    final List<String> parametersLine = request.getParametersLine();
                    List<String> key = m.getParameterNames();
                    HashMap<String, List<String>> parameterMap = Maps.newHashMap();
                    for (int i = 0; i < matcher.groupCount(); i++) {
                        String value = matcher.group(i + 1);
                        parametersLine.add(value);
                        List<String> values = Lists.newArrayList();
                        values.add(value);
                        parameterMap.put(key.get(i), values);


                    }
                    request.setParamters(parameterMap);
                }
                break;
            }
        }

    }

    public void parseParameterByMethod(RequestDetail request, FullHttpRequest httpRequest) {
        Map<String, List<String>> parameterMap = request.getParamters();
        if (Objects.isNull(parameterMap)) {
            parameterMap = Maps.newHashMap();
        }
        String method = request.getMethod();
        if ("GET".equals(method)) {
            QueryStringDecoder queryDecoder = new QueryStringDecoder(httpRequest.uri(), Charset.forName("UTF-8"));
            Map<String, List<String>> stringListMap = queryDecoder.parameters();
            parameterMap.putAll(stringListMap);
        } else if ("POST".equals(method)) {
            String contentType = httpRequest.headers().get("Content-Type");
            if (Constant.FORM.equals(contentType)) {
                try {
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(httpRequest);
                    List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
                    for (InterfaceHttpData data : datas) {
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                            Attribute attribute = (Attribute) data;
                            List<String> list = Arrays.asList(attribute.getValue());
                            parameterMap.put(attribute.getName(), list);
                        }
                    }
                } catch (IOException e) {
                    log.error("post request parse error,{}" + e.getMessage());
                }
            } else if (Constant.APPLICATION_JSON.equals(contentType)) {

                /**
                 * TODO
                 */
                String jsonStr = httpRequest.content().toString(CharsetUtil.UTF_8);
                JSONObject obj = JSON.parseObject(jsonStr);
                for (Map.Entry<String, Object> item : obj.entrySet()) {
                    System.out.println(item.getKey() + "=" + item.getValue().toString());
                }
            } else if (Constant.MULTIPART.equals(contentType)) {
                HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MAXSIZE); //16384L
                DiskFileUpload.baseDirectory = "/Users/zhengzechao/Desktop";
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, httpRequest);
                List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
                try {
                    for (InterfaceHttpData data : datas) {
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                            FileUpload fileUpload = (FileUpload) data;
                            String fileName = fileUpload.getFilename();
                            if (fileUpload.isCompleted()) {
                                //保存到磁盘
                                StringBuffer fileNameBuf = new StringBuffer();
                                fileNameBuf.append(DiskFileUpload.baseDirectory).append(fileName);
                                fileUpload.renameTo(new File(fileNameBuf.toString()));
                            }
                        }
                    }
                } catch (IOException e) {
                    log.error("file.upload.fail,cause:{}", Throwables.getStackTraceAsString(e));
                }
            }
        }

        request.setParamters(parameterMap);
    }


}
