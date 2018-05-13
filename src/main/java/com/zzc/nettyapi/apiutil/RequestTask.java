package com.zzc.nettyapi.apiutil;

import com.zzc.nettyapi.request.RequestDetail;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.TRANSFER_ENCODING;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author zhengzechao
 * @date 2018/5/13
 * Email ooczzoo@gmail.com
 */
public class RequestTask implements Runnable,Recyclable{


    public static final Logger log = LoggerFactory.getLogger(RequestTask.class);

    public ChannelHandlerContext context;
    public FullHttpRequest request;
    public ApiHandler apiHandler;


    public RequestTask(ChannelHandlerContext context, FullHttpRequest request, ApiHandler apiHandler) {

        this.context = context;
        this.request = request;
        this.apiHandler = apiHandler;
    }

    @Override
    public void run() {
        byte[] result = apiHandler.handle(context,request);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result));
        response.headers().set(CONTENT_TYPE,new AsciiString("application/json; charset=utf-8"));
        response.headers().set(TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (!keepAlive) {
            context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            context.writeAndFlush(response);
        }
    }

    @Override
    public void reset() {
        context = null;
        request = null;
    }

    public void reset(ChannelHandlerContext context,FullHttpRequest request){
        this.context = context;
        this.request = request;
    }
}
