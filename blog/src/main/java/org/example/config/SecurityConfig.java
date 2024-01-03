package org.example.config;

import jakarta.annotation.Resource;
import org.example.filter.JwtAuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/*
前后台模块有不同的认证授权规则
 */
@Configuration
public class SecurityConfig{

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    AuthenticationManager authenticationManager() {
        // 创建 DaoAuthenticationProvider 实例
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // 设置用户详细信息服务，例如自定义的用户服务类实现 UserDetailsService 接口
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // 设置密码编码器，用于加密和验证密码。
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // 返回一个 ProviderManager，它是 AuthenticationManager 的一个实现
        return new ProviderManager(daoAuthenticationProvider);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //跨域配置 在父模块的WebConfig中配置过了,但还是会拦截带token的请求
                .cors(conf -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    //添加前端站点地址，这样就可以告诉浏览器信任了
                    cors.addAllowedOrigin("http://localhost:8080");
                    //虽然也可以像这样允许所有 cors.addAllowedOriginPattern("*");
                    //但是这样并不安全，我们应该只许可给我们信任的站点
                    cors.setAllowCredentials(true);  //允许跨域请求中携带Cookie
                    cors.addAllowedHeader("*");   //其他的也可以配置，为了方便这里就 * 了
                    cors.addAllowedMethod("*");
                    cors.addExposedHeader("*");
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);  //直接针对于所有地址生效
                    conf.configurationSource(source);
                }
                )
                //关闭csrf
                .csrf(AbstractHttpConfigurer::disable)
                //将Session管理创建策略改成无状态,不通过Session获取SecurityContext
                .sessionManagement( conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //请求权限设置
                .authorizeHttpRequests( auth -> auth
                        // 对于登录接口,只允许匿名访问
                        .requestMatchers("/login").anonymous()
                        // 测试用
                        //.requestMatchers("/category/getCategoryList").authenticated()
                        // 退出登录需要先登录
                        .requestMatchers("/logout").authenticated()
                        // 登录后才能评论
                        .requestMatchers("/comment").authenticated()
                        // 登录后才能查看个人信息
                        .requestMatchers(HttpMethod.GET,"/user/userinfo").authenticated()
                        // 上传头像需登录，但前端没写
                        //.requestMatchers("/upload").authenticated()
                        // 更新个人需要需登录
                        .requestMatchers(HttpMethod.PUT,"/user/userinfo").authenticated()
                        // 除上面外的所有请求全部不需要认证即可访问

                        .anyRequest().permitAll()
                )
                //添加过滤器到security过滤链中
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                //关闭security的默认退出登录处理
                .logout(AbstractHttpConfigurer::disable)
                .build();
    }

}
