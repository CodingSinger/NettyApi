package com.zzc.nettyapi.argument.conversion.util;

/**
 * @author zhengzechao
 * @date 2018/4/27
 * Email ooczzoo@gmail.com
 */
public class ConvertKeyPair {
    private Class sourceClass;
    private Class targetClass;

    public ConvertKeyPair(Class sourceClass, Class targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConvertKeyPair that = (ConvertKeyPair) o;

        if (sourceClass != null ? !sourceClass.equals(that.sourceClass) : that.sourceClass != null) return false;
        return targetClass != null ? targetClass.equals(that.targetClass) : that.targetClass == null;
    }

    @Override
    public int hashCode() {
        int result = sourceClass != null ? sourceClass.hashCode() : 0;
        result = 31 * result + (targetClass != null ? targetClass.hashCode() : 0);
        return result;
    }

    public ConvertKeyPair() {
    }
}
