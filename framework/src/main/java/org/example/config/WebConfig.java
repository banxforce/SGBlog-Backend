package org.example.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 拦截器阶段：WebMvcConfigurer 中的某些方法确实是在拦截器阶段生效的，比如 addInterceptors 方法用于注册和配置拦截器。
 *
 * 消息转换器阶段：WebMvcConfigurer 接口中的 configureMessageConverters 方法用于配置消息转换器，这个阶段发生在请求被处理之前。
 *
 * 资源处理器阶段：addResourceHandlers 方法用于配置静态资源的处理器，可以在该阶段配置静态资源的访问路径、存放位置等。
 *
 * 视图解析器阶段：configureViewResolvers 方法允许配置视图解析器，这会影响到视图的解析和渲染。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 这个方法是在拦截器阶段生效的，security的过滤链由filter组成。
     * 如果引入了security，这个配置就失效了，需要在security中再进行配置
     */
    //解决cors跨域问题，与security的cors配置类似
    @Override
    public void addCorsMappings(CorsRegistry registry) {
            //允许跨域的路径
        registry.addMapping("/**")
                //允许跨域的域名源,配合addMapping() = http://localhost:8080/**
                // 前台和后台
                //.allowedOrigins("http://localhost:8080","127.0.0.1")
                //这个方法相对于allowedOrigins来说更灵活:可以指定路径,使用通配符
                .allowedOriginPatterns("http://localhost:8080","http://localhost:81")
                //允许携带cookie
                .allowCredentials(true)
                //允许所有请求头
                .allowedHeaders("*")
                //允许请求的方法
                .allowedMethods("GET","POST","DELETE","PUT")
                //公开的请求头，默认客户端只能访问标准的响应头字段，自定义的访问头字段不会被客户端看到
                //.exposedHeaders("*")
                //缓存时间,缓存一些跨域配置,实际的请求(GET,POST...)不受影响
                .maxAge(3600);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建 FastJsonHttpMessageConverter 对象
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 创建 FastJson 的配置对象
        FastJsonConfig config = this.getFastJsonConfig();

        // 将配置应用到 FastJsonHttpMessageConverter
        converter.setFastJsonConfig(config);
        // 设置默认字符集为 UTF-8
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        // 设置支持的媒体类型为 APPLICATION_JSON
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        // 将 FastJsonHttpMessageConverter 添加到 converters 列表的首位
        converters.add(0, converter);
    }

    private  FastJsonConfig getFastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        /*
        * 设置日期格式为 "yyyy-MM-dd HH:mm:ss"。日期类型的字段会按照指定的格式转换为对应的字符串格式
        * 这里设置了输出的日期格式，日期类型的字段会按照指定的格式转换为对应的字符串格式
        * 同样的，在反序列时前端传过来的日期数据也要符合"yyyy-MM-dd HH:mm:ss" ，不然无法解析成Date对象或直接抛出异常*/
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // 设置 JSONReader 特性，这里使用 FieldBased 和 SupportArrayToBean
        config.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);
        // 设置 JSONWriter 特性，这里使用 PrettyFormat :输出的 JSON 字符串将进行格式化，添加缩进和换行符，使其更易读。
        config.setWriterFeatures(JSONWriter.Feature.PrettyFormat,
                // 将Long类型作为String序列化
                JSONWriter.Feature.WriteLongAsString);
        return config;
    }

}
