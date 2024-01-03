package org.example.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Bean 属性复制工具类
 */
public class BeanCopyUtils {

    private BeanCopyUtils() {
        // 私有构造函数，防止实例化该工具类
    }

    /**
     * 将源对象的属性复制到目标对象中
     *
     * @param source 源对象，要复制属性的对象
     * @param clazz  目标类的 Class 对象，复制属性到该类的对象中
     * @param <V>    目标对象的类型参数
     * @return 复制属性后的目标对象
     */
    public static <V> V copyBean(Object source, Class<V> clazz) {
        // 创建目标对象
        V result = null;
        try {
            result = clazz.getConstructor().newInstance();
            // 实现属性复制
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            // 如果复制属性时发生异常，将其转换为 RuntimeException 抛出
            throw new RuntimeException(e);
        }
        // 返回复制属性后的目标对象
        return result;
    }

    /**
     * 将源对象列表的属性复制到目标类的对象列表中
     *
     * @param list  源对象列表
     * @param clazz 目标类的 Class 对象
     * @param <O>   源对象的类型参数
     * @param <V>   目标对象的类型参数
     * @return 复制属性后的目标对象列表
     */
    public static <O, V> List<V> copyBeanList(List<O> list, Class<V> clazz) {
        return list.stream()
                .map(o -> copyBean(o, clazz)) // 对源对象列表中的每个对象执行属性复制操作
                .toList(); // 将结果转换为列表并返回
    }
}

