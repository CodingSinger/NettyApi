package com.zzc.nettyapi.nettyservice;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ServerInitializer extends ChannelInitializer {





    @Override
    protected void initChannel(Channel channel) throws Exception {
        final ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("encoder",new HttpResponseEncoder());
        pipeline.addLast("decoder",new HttpRequestDecoder());
        pipeline.addLast("compressor", new HttpContentCompressor());
        pipeline.addLast("aggregator", new HttpObjectAggregator(10*1024*1024));
        pipeline.addLast(new ServerHandler(NettyServerBootStrap.handleAsync,NettyServerBootStrap.getWorkExecutor()));
    }
}
