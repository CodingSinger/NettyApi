package com.zzc.nettyapi.nettyservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */
public class ServerConfigLoader {

    public static final Logger logger = LoggerFactory.getLogger(ServerConfigLoader.class);
    //禁止继承和实例化
    private ServerConfigLoader(){
    }
    private static String config = "/config.properties";
    private static Properties properties;
    public static void init(){
        properties = new Properties();
        try {
            properties.load(ServerConfigLoader.class.getResourceAsStream(config));
            logger.info("load server config from "+config+" successfully!");
        } catch (IOException e) {
            logger.error("load server config from "+config+" error!");
            e.printStackTrace();
        }
    }
    public static int getInt(String key,int defaultValue){
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value):defaultValue;
    }
    public static String getValue(String key,String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

}
