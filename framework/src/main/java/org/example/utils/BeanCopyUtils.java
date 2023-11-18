package org.example.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BeanCopyUtils {

    private BeanCopyUtils(){}

    public static <V> V copyBean(Object source,Class<V> clazz){
        //创建目标对象
        V result = null;
        try {
            result = clazz.getConstructor().newInstance();
            //实现copy
            BeanUtils.copyProperties(source,result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //返回结果
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .toList();
    }

}
