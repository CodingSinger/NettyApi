package com.zzc.nettyapi.hotload.core.classloader;

import com.zzc.nettyapi.apiutil.ApiRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengzechao
 * @date 2018/6/27
 * Email ooczzoo@gmail.com
 */

public class NettyServerClassLoader extends URLClassLoader {

    private static final Logger log = LoggerFactory.getLogger(NettyServerClassLoader.class);
    private final Map<String, ControllerResource> resourceEntries =
            new ConcurrentHashMap<>();
    private static final String CLASS_SUFFIX = ".class";

    private ClassLoader parent = null;

    public NettyServerClassLoader(URL[] urls) {
        super(new URL[0]);

        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }
        this.parent = p;

    }


    public NettyServerClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }
        this.parent = p;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return (loadClass(name, false));
    }


    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        synchronized (getClassLoadingLock(name)) {
            if (log.isDebugEnabled()) {
                log.debug("loadClass(" + name + ", " + resolve + ")");
                Class<?> clazz = null;
                //从本地已经加载的Class资源搜索  ，相当于是一级缓存  一级缓存中没有去二级缓存JVM缓存中拿
                clazz = findLoadedClass0(name);
                if (clazz != null) {
                    if (resolve) {
                        resolveClass(clazz);
                    }
                    return (clazz);
                }
                //从JVM缓存的Class中搜索
                clazz = findLoadedClass(name); //去虚拟机缓存中查找是否有已经加载过的类 ，虽然在这里是用NettyServerClassLoader加载的，但是
                //自己底层还是通过defineClass加载的，所以还是会在虚拟机缓存中有缓存(类名,类加载器实例) -> 类型
                if (clazz != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("  Returning class from cache");
                    }
                    if (resolve) { //是否进行链接
                        resolveClass(clazz);
                    }
                    return (clazz);
                } else {

                    if (filter(name)){
                        clazz = findClass(name);
                        return clazz;
                    }else{//无条件地从父类AppClassLoader加载类
                        return parent.loadClass(name);
                    }

                }


            }

        }

        return null;
    }


    @Override
    public Class<?> findClass(String name) {
        //重新加载类进来
        Class<?> aClass = null;
        String pathName = classNameToPath(name);
        URL classUrl = ApiRegistry.class.getResource(pathName);
        File file = new File(classUrl.getFile());
        try {
            InputStream inputStream = new FileInputStream(file);
            FileChannel channel = ((FileInputStream) inputStream).getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(Math.toIntExact(channel.size()));
            channel.read(buffer);
            final byte[] array = buffer.array();
            aClass = super.defineClass(name, array, 0, array.length);
            if (aClass != null) {
                ControllerResource resource = new ControllerResource();
                resource.setClassName(name);
                resource.setClzz(aClass);
                resource.setAbsolutePath(file.getAbsolutePath());
                resource.setFile(file);
                resource.setLastmodify(file.lastModified());
                resourceEntries.put(name, resource);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return aClass;
    }


    public boolean filter(String name){
        if (name.endsWith("Controller")){
            return true;
        }else{
            return false;
        }
    }

    public Class findLoadedClass0(String name) {
        ControllerResource controller = resourceEntries.get(name);
        if (Objects.isNull(controller)) {
            return null;
        }
        return controller.getClzz();
    }

    public Map<String, ControllerResource> getResourceEntries() {
        return resourceEntries;
    }

    public static String classNameToPath(String className) {
        className = className.replace(".","/");
        StringBuilder builder = new StringBuilder(className.length() + 7); // "/" + ..+ ".class"
        builder.append("/");
        builder.append(className);
        builder.append(CLASS_SUFFIX);
        return builder.toString();
    }

}
