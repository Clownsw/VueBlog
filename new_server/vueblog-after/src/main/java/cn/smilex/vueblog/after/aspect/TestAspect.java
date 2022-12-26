package cn.smilex.vueblog.after.aspect;

import cn.smilex.vueblog.common.annotation.cache.Cache;
import cn.smilex.vueblog.common.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.stereotype.Component;

/**
 * @author smilex
 * @date 2022/12/26/21:11
 */
@Slf4j
@Aspect
@Component
public class TestAspect {

    @Before("execution(* cn.smilex.vueblog.common.service.LoginService.login(..))")
    public void before(JoinPoint joinPoint) {
        try {
            log.info("=====================");
            ProxyMethodInvocation methodInvocationByJoinPoint = CommonUtil.getMethodInvocationByJoinPoint(joinPoint);
            Class<?> clazz = CommonUtil.getClazzByMethod(methodInvocationByJoinPoint.getMethod());
            Class<?>[] interfaces = clazz.getInterfaces();
            Cache cacheAnnotation = CommonUtil.getAnnotationByMethodNameAndInterfaces(
                    Cache.class,
                    methodInvocationByJoinPoint.getMethod().getName(),
                    methodInvocationByJoinPoint.getMethod().getParameterTypes(),
                    interfaces
            );
            log.info("{}", cacheAnnotation);
            log.info("=====================");
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
