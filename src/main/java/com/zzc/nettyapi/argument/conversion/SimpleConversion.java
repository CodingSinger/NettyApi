package com.zzc.nettyapi.argument.conversion;

import com.zzc.nettyapi.Exception.ConvertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/4/1
 */
public class SimpleConversion implements Conversion{

    private Logger log = LoggerFactory.getLogger(SimpleConversion.class);

    @Override
    public Object convert(Class targetType, String value) throws ConvertException {

        try {
            String typeName = targetType.getName();
            if("java.lang.Integer".equals(targetType.getName())||"int".equals(targetType.getName())){
                return Integer.valueOf(value);
            }else if("java.lang.Double".equals(targetType.getName())||"double".equals(targetType.getName())){
                return Double.valueOf(value);
            }else if("java.lang.Float".equals(typeName)||"float".equals(typeName)){
                return Float.valueOf(value);

            }else if("java.lang.Short".equals(typeName) || "short".equals(typeName)){

                return Short.valueOf(value);
            }else if(Objects.equals(String.class,targetType)){
                return value;
            }else{

                throw new ConvertException("Covert Exception");

            }

        } catch (Exception e) {
            throw e;
        }


    }

}
