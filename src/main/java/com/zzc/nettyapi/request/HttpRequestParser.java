package com.zzc.nettyapi.request;

import com.zzc.nettyapi.apiutil.ApiMethod;
import com.zzc.nettyapi.apiutil.ApiRegistry;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void parse(RequestDetail requestDetail){

        HttpRequest request = requestDetail.getHttpRequest();


        parseRequest(requestDetail,request);
        parseParameterByMethod(requestDetail,request);



    }




    /*根据request路径解析请求 包括url参数以及路径参数和请求体参数*/
    private void parseRequest(RequestDetail request,HttpRequest httpRequest) {
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


        for (ApiMethod m : list) {

            Pattern pattern = Pattern.compile("^" + m.getRegex() + "$");
            Matcher matcher = pattern.matcher(uri.substring(0,uri.indexOf("?")));

            if (matcher.find()) {
                request.setApi(m);
                request.setUrl(m.getUrl());
                //解析路径参数 需要截断"?"之后的
                if (matcher.groupCount() > 0) {

                    final List<String> parametersLine = request.getParametersLine();
                    for (int i = 0; i < matcher.groupCount(); i++) {



                        parametersLine.add(matcher.group(i + 1));


                    }
                }
                break;
            }
        }

    }

    public void parseParameterByMethod(RequestDetail request,HttpRequest httpRequest) {


        String method = request.getMethod();
        if ("GET".equals(method)) {


            QueryStringDecoder queryDecoder = new QueryStringDecoder(httpRequest.uri(), Charset.forName("UTF-8"));
            Map<String, List<String>> stringListMap = queryDecoder.parameters();
            request.setParamters(stringListMap);

        } else if ("POST".equals(method)) {

            HashMap<String,List<String>> parameters = new HashMap<>();

            String contentType = httpRequest.headers().get("Content-Type");

            if(Constant.FORM.equals(contentType)){

                try {
                    HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(httpRequest);
                    List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();

                    for (InterfaceHttpData data : datas) {
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                            Attribute attribute = (Attribute) data;
                            List<String> list = Arrays.asList(attribute.getValue());
                            parameters.put(attribute.getName(),list);


                        }
                    }
                } catch (IOException e) {

                    log.error("post request parse error,{}"+e.getMessage());
                }

            }else if(Constant.APPLICATION_JSON.equals(contentType)){



            }else if(Constant.MULTIPART.equals(contentType)){



            }



        }


    }


}
