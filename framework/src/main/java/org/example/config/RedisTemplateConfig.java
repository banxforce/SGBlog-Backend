package org.example.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(){
        // 创建一个RedisTemplate
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        // 创建自定义的FastJSON2配配置，用于序列化value,传入Object.class表示应用于所有类
        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);
        // 用于序列化key
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 将redisTemplate连接到工厂
        template.setConnectionFactory(this.redisConnectionFactory);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值,用FastJson2RedisSerializer来序列化和反序列化redis的value值
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(fastJson2RedisSerializer);

        // Hash的key也采用StringRedisSerializer的序列化方式,用FastJson2RedisSerializer来序列化和反序列化redis的value值
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(fastJson2RedisSerializer);

        // 优先级在配置文件之后
        template.afterPropertiesSet();

        return template;
    }

}
