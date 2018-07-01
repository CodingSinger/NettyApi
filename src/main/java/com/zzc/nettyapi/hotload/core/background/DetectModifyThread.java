package com.zzc.nettyapi.hotload.core.background;

import com.zzc.nettyapi.apiutil.ApiHandler;
import com.zzc.nettyapi.apiutil.ApiRegistry;
import com.zzc.nettyapi.hotload.core.classloader.ControllerResource;
import com.zzc.nettyapi.hotload.core.classloader.NettyServerClassLoader;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.Map;
/**
 * @author zhengzechao
 * @date 2018/6/28
 * Email ooczzoo@gmail.com
 */
public class DetectModifyThread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DetectModifyThread.class);
    private ApiHandler apiHandler = null;
    public DetectModifyThread(NettyServerClassLoader classLoader, ApiHandler apiHandler) {
        this.classLoader = classLoader;
        this.apiHandler = apiHandler;
    }

    private NettyServerClassLoader classLoader = null;

    private volatile Boolean stop = false;
    @Override
    public void run() {
        while (!stop){
            try {
                Thread.sleep(1000);
                List<Map.Entry<String, ControllerResource>> list = modified();
                if (list.isEmpty()){
                    continue;
                }
                if(log.isDebugEnabled()){
                    log.debug("resource reload");
                }
                //如果文件改变了
                System.out.println("reload");
                apiHandler.setClassLoader(new NettyServerClassLoader(ClassLoader.getSystemClassLoader()));
                for (Map.Entry<String, ControllerResource> resourceEntry : list) {
                    ControllerResource value = resourceEntry.getValue();
                    //设置重新加载类
                    ApiRegistry.reloadClass.put(value.getClassName(),true);

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    public List<Map.Entry<String,ControllerResource>> modified(){
        ConcurrentHashMap<String,ControllerResource> map = (ConcurrentHashMap) classLoader.getResourceEntries();
        final List<Map.Entry<String, ControllerResource>> collect = map.entrySet().stream()
                .filter(entry -> {
                            ControllerResource controllerResource = entry.getValue();
                            final long lastModified = controllerResource.getFile().lastModified();
                            boolean modified = lastModified != controllerResource.getLastmodify();
                            controllerResource.setLastmodify(lastModified);
                            return modified;

                        }

                ).collect(Collectors.toList());

        return collect;


    }
}
