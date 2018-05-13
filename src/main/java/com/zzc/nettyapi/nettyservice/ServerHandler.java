package com.zzc.nettyapi.nettyservice;

import com.zzc.nettyapi.apiutil.ApiHandler;
import com.zzc.nettyapi.apiutil.RequestTask;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    public final Boolean handleAsync;
    public final ThreadPoolExecutor workExecutor ;
    public ServerHandler(Boolean handleAsync,ThreadPoolExecutor workExecutor) {
        this.handleAsync = handleAsync;
        this.workExecutor = workExecutor;
    }

    public static ApiHandler handler  ;
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            if (HttpUtil.is100ContinueExpected(httpRequest)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }
            //TODO :requestTask缓存
            RequestTask requestTask = new RequestTask(ctx,httpRequest,handler);

            if (handleAsync){
                workExecutor.execute(requestTask);
            }else{
                requestTask.run();
            }

        }
    }
}
