package org.example.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Dto 工具类
 */
public class DtoUtils {

    public static boolean hasIllegalParameter(Object dto, Class<?> dtoClass){
        Field[] fields = dtoClass.getDeclaredFields();
        for(Field field: fields){
            try {
                field.setAccessible(true);
                Object value = field.get(dto);
                if(Objects.isNull(value)){
                    // 字段值为null
                    return true;
                }
                if(value instanceof String string && !StringUtils.hasText(string)){
                    // 字段值为空字符串
                    return true;
                }
            } catch (IllegalAccessException e) {
                // 如果字段是私有的或受保护的，并且没有正确设置可访问性
                e.printStackTrace();
            }
        }
        // 所有字段都非空，返回false
        return false;
    }

}
