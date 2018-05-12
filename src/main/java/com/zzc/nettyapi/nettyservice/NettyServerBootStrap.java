package com.zzc.nettyapi.nettyservice;

import com.zzc.nettyapi.apiutil.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class NettyServerBootStrap {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerBootStrap.class);
    private static Executor executor;
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        EventLoopGroup mainLoop = new NioEventLoopGroup();

        EventLoopGroup workLoop = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(mainLoop,workLoop).channel(NioServerSocketChannel.class).childHandler(new ServerInitializer());

        initComponent();


        int port = Integer.parseInt(ServerConfigLoader.getValue("port"));
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("Server bind successfully");
            // Wait until the server socket is closed.
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("Server start error"+e.getMessage());
            e.printStackTrace();
        }finally {
            mainLoop.shutdownGracefully();
            workLoop.shutdownGracefully();

        }

    }

    private static void initComponent() throws IllegalAccessException, InstantiationException {
        //读入配置文件
        ServerConfigLoader.init();

        ServerHandler.handler = new ApiHandler();
        //解析api
        ApiRegistry.init();
        //初始化缓存线程池
        int corePoolSize = ServerConfigLoader.getInt("corePoolSize");
        int maximumPoolSize = ServerConfigLoader.getInt("maximumPoolSize");
        int keepAliveTime = ServerConfigLoader.getInt("keepAliveTime");

        executor = new RecycleThreadExecutor(corePoolSize,
                                            maximumPoolSize,
                                            keepAliveTime,
                                            TimeUnit.SECONDS,
                                            new LinkedBlockingQueue<Runnable>(),
                                            new RecycleThreadFactory("RecycleThread"));





    }

    public static Executor getExecutor() {
        return executor;
    }
}
