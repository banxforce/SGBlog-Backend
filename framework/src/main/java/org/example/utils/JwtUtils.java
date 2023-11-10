package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

public class JwtUtils {

    private static final String JWT_KEY = "MyFirstBlog";//Jwt密钥
    private static final Integer JWT_TTL = 60 * 60 * 24; //一天

    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据
     * @return 根据subject和默认失效时间设置的jwt令牌
     */
    public static String creatJwt(String subject){
        return jwtBuild(subject,null);
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据
     * @param ttlSeconds 过期时间,秒
     * @return 根据subject和默认失效时间设置的jwt令牌
     */
    public static String creatJwt(String subject,Integer ttlSeconds){
        return jwtBuild(subject, ttlSeconds);
    }

    private static String jwtBuild(String subject,Integer ttlSeconds){
        //加密key
        Algorithm algorithm =  getAlgorithm();
        //获取日历实例
        Calendar instance = Calendar.getInstance();
        //获取目前时间
        Date now = instance.getTime();
        //计算过期时间，如果参数为null,就设定为默认值
        if(Objects.isNull(ttlSeconds)){
            ttlSeconds = JWT_TTL;
        }
        instance.add(Calendar.SECOND,ttlSeconds);
        //创建jwt令牌
        return JWT.create()
                .withJWTId(getUUID()) //唯一的ID
                .withSubject(subject) //主体内容
                .withExpiresAt(instance.getTime()) //过期时间
                .withIssuedAt(now) //签发时间
                .withIssuer("bx") //签发者
                .sign(algorithm); //使用HS256对称加密算法签名

    }

    /**
     * 加密算法,使用HMAC256加密key
     */
    private static Algorithm getAlgorithm(){
        return Algorithm.HMAC256(JWT_KEY);
    }

    /**
     * 解析Jwt
     * @return 经过验证和解码的JWT
     */

    public static DecodedJWT parseJwt(String jwt) {
        //加密key
        Algorithm algorithm = getAlgorithm();
        //获取解析器
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        //解析
        DecodedJWT verify = jwtVerifier.verify(jwt);
        //返回
        return verify;
    }

}
