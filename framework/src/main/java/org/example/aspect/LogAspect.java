package org.example.aspect;



import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.annotation.SystemLog;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(org.example.annotation.SystemLog)")
    public void pt(){}

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            handlerBefore(joinPoint);
            result = joinPoint.proceed();
            handleAfter(result);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return result;
    }

    private void handlerBefore(ProceedingJoinPoint joinPoint) {
        // 拿到RequestAttributes并强转成子类
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 拿到封装的request
        HttpServletRequest request = requestAttributes.getRequest();
        // 拿到方法上的@SystemLog注解
        SystemLog systemLog = getSystemLog(joinPoint);


        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}",systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}",request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        // 通过反射拿到方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 拿到方法上的注解
        return method.getAnnotation(SystemLog.class);
    }

    private void handleAfter(Object result) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(result));
    }

}
