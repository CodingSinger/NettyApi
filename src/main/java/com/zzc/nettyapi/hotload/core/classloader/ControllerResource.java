package com.zzc.nettyapi.hotload.core.classloader;

import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/6/27
 * Email ooczzoo@gmail.com
 */
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

    private Boolean isSingle;

    /**
     * 上次改变时间
     */
    private long lastmodify = -1;


    /**
     *类文件文件
     */

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class getClzz() {
        return clzz;
    }

    public void setClzz(Class clzz) {
        this.clzz = clzz;
    }

    public Boolean getSingle() {
        return isSingle;
    }

    public void setSingle(Boolean single) {
        isSingle = single;
    }

    public long getLastmodify() {
        return lastmodify;
    }

    public void setLastmodify(long lastmodify) {
        this.lastmodify = lastmodify;
    }


}
