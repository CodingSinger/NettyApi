package com.zzc.nettyapi.argument.conversion.convert;

import com.zzc.nettyapi.exception.ConvertException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author zhengzechao
 * @date 2018/4/27
 * Email ooczzoo@gmail.com
 */
public class StringToNumberConverter implements Converter<String,Number> {

    @Override
    public Number convert(String source,Class targetClass) throws ConvertException {

        String trimmed = source.trim();

        if (Byte.class == targetClass) {
            return Byte.valueOf(trimmed);
        }
        else if (Short.class == targetClass) {
            return Short.valueOf(trimmed);
        }
        else if (Integer.class == targetClass) {
            return Integer.valueOf(trimmed);
        }
        else if (Long.class == targetClass) {
            return Long.valueOf(trimmed);
        }
        else if (BigInteger.class == targetClass) {
            return new BigInteger(trimmed);
        }
        else if (Float.class == targetClass) {
            return  Float.valueOf(trimmed);
        }
        else if (Double.class == targetClass) {
            return Double.valueOf(trimmed);
        }
        else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return new BigDecimal(trimmed);
        }
        else {
            throw new ConvertException(
                    "Cannot convert String [" +source + "] to target class [" + targetClass.getName() + "]");
        }

    }
    @Override
    public boolean match(Class targetClass,Class sourceClass) {
        return Number.class.isAssignableFrom(targetClass)
                && CharSequence.class.isAssignableFrom(sourceClass);

    }
}
