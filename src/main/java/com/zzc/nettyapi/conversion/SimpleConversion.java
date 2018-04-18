package com.zzc.nettyapi.conversion;

/**
 * @author zhengzechao
 * @date 2018/4/1
 */
public class SimpleConversion implements Conversion{

    @Override
    public Object convert(Class targetType, String value){

        String typeName = targetType.getName();
        if("java.lang.Integer".equals(targetType.getName())||"int".equals(targetType.getName())){
            return Integer.valueOf(value);
        }else if("java.lang.Double".equals(targetType.getName())||"double".equals(targetType.getName())){
            return Double.valueOf(value);
        }else if("java.lang.Float".equals(typeName)||"float".equals(typeName)){
            return Float.valueOf(value);

        }else if("java.lang.Short".equals(typeName) || "short".equals(typeName)){

            return Short.valueOf(value);
        }

        return null;
    }

}
