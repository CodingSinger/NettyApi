package com.zzc.nettyapi.hotload.core.classloader;

import com.zzc.nettyapi.hotload.basic.Controller;
import org.apache.catalina.loader.ResourceEntry;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengzechao
 * @date 2018/6/27
 * Email ooczzoo@gmail.com
 */
public class NettyServerClassLoader extends URLClassLoader {

    protected final Map<String, Controller> resourceEntries =
            new ConcurrentHashMap<>();


    private ClassLoader parent = null;

    public NettyServerClassLoader(URL[] urls) {
        super(new URL[0]);

        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }
        this.parent = p;

    }


    public NettyServerClassLoader(ClassLoader parent){
        super(new URL[0],parent);
        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }
        this.parent = p;
    }




}
