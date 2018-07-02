package com.zzc.nettyapi.nettyservice;

import com.google.common.base.Throwables;
import com.zzc.nettyapi.annotation.parser.AnnotationMappingConfiguration;
import com.zzc.nettyapi.apiutil.*;
import com.zzc.nettyapi.hotload.core.classloader.NettyServerClassLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class NettyServerBootStrap {

    public static Boolean handleAsync = false;
    private static final Logger logger = LoggerFactory.getLogger(NettyServerBootStrap.class);
    private static Executor recyleExecutor;
    private static ThreadPoolExecutor workExecutor;

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        EventLoopGroup mainLoop = new NioEventLoopGroup();
        EventLoopGroup workLoop = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(mainLoop, workLoop).channel(NioServerSocketChannel.class).childHandler(new ServerInitializer());

        AnnotationMappingConfiguration configuration = new AnnotationMappingConfiguration(new NettyServerClassLoader(ClassLoader.getSystemClassLoader()), "com.zzc.test.Controller");
        try {
            if (Objects.isNull(configuration)) {
                initComponent(false, null);
            } else {
                initComponent(true, configuration);
            }

            int port = Integer.parseInt(ServerConfigLoader.getValue("port", "8080"));
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("Server bind successfully");
            // Wait until the server socket is closed.
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("Server start error" + e.getMessage());
            e.printStackTrace();
        } finally {
            mainLoop.shutdownGracefully();
            workLoop.shutdownGracefully();

        }

    }

    private static void initComponent(boolean scan, AnnotationMappingConfiguration configuration) {

        try {
            //读入配置文件
            ServerConfigLoader.init();


            if (configuration != null){
                ServerHandler.handler = new ApiHandler(configuration.getClassLoader());
            }else{
                ServerHandler.handler = new ApiHandler(new NettyServerClassLoader(ClassLoader.getSystemClassLoader()));
            }

            //解析api
            ApiRegistry.init(scan, configuration);
            //初始化缓存线程池
            int corePoolSize = ServerConfigLoader.getInt("corePoolSize", 5);
            int maximumPoolSize = ServerConfigLoader.getInt("maximumPoolSize", 20);
            int keepAliveTime = ServerConfigLoader.getInt("keepAliveTime", 20000);
            recyleExecutor = new RecycleThreadExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new RecycleThreadFactory("RecycleThread"));
            handleAsync = Boolean.parseBoolean(ServerConfigLoader.getValue("handleAnsyc", "false"));
            if (handleAsync) { //如果允许异步
                int coreWorkPoolSize = ServerConfigLoader.getInt("coreWorkPoolSize", 10);
                int maximunWorkPoolSize = ServerConfigLoader.getInt("maximunWorkPoolSize", 200);
                workExecutor = new ThreadPoolExecutor(coreWorkPoolSize, maximunWorkPoolSize,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        new HandleRequestThreadFactory("handleThread"));
            }
        } catch (Exception e) {
            logger.error("init component error! cause:{}", Throwables.getStackTraceAsString(e));
            throw new RuntimeException(e.getMessage());
        }
    }


    public static Executor getExecutor() {
        return recyleExecutor;
    }

    public static ThreadPoolExecutor getWorkExecutor() {
        return workExecutor;
    }
}
