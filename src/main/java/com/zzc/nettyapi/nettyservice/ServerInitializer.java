package com.zzc.nettyapi.nettyservice;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ServerInitializer extends ChannelInitializer {


    @Override
    protected void initChannel(Channel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ServerHandler());
    }
}
