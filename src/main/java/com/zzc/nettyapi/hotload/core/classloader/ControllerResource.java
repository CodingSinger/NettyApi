package com.zzc.nettyapi.hotload.core.classloader;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/6/27
 * Email ooczzoo@gmail.com
 */
@Data
public class ControllerResource implements Serializable {

    /**
     * 绝对路径
     */
    private String absolutePath;
    /**
     * 相对Classpath的路径
     */
    private String relativePath;
    /**
     * 全限定名
     */
    private String className;

    /**
     * 当前类
     */
    private volatile Class clzz;

    /**
     * 是否单例
     */

    private String isSingle;

    /**
     * 上次改变时间
     */
    private long lastmodify = -1;



}
