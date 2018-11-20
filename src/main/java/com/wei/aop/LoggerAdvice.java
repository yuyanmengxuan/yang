package com.wei.aop;

import ch.qos.logback.classic.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
public class LoggerAdvice {

    @Before("within(com.wei.aop..*) && @annotation(loggerManage)")
    public void addBeforeLogger(JoinPoint joinPoint, LoggerManage loggerManage) {
        LocalDateTime now = LocalDateTime.now();

        System.out.println(now.toString()+"执行[" + loggerManage.logDescription() + "]开始");
        System.out.println(joinPoint.getSignature().toString());

        System.out.println(parseParames(joinPoint.getArgs()));

    }

    @AfterReturning("within(com.wei.aop..*) && @annotation(loggerManage)")
    public void addAfterReturningLogger(JoinPoint joinPoint, LoggerManage loggerManage) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now.toString()+"执行 [" + loggerManage.logDescription() + "] 结束");
    }

    @AfterThrowing(pointcut = "within(com.wei.aop..*) && @annotation(loggerManage)", throwing = "ex")
    public void addAfterThrowingLogger(JoinPoint joinPoint, LoggerManage loggerManage, Exception ex) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("执行 [" + loggerManage.logDescription() + "] 异常");
    }

    private String parseParames(Object[] parames) {

        if (null == parames || parames.length <= 0) {
            return "";

        }
        StringBuffer param = new StringBuffer("传入参数 # 个:[ ");
        int i =0;
        for (Object obj : parames) {
            i++;
            if (i==1){
                param.append(obj.toString());
                continue;
            }
            param.append(" ,").append(obj.toString());
        }
        return param.append(" ]").toString().replace("#",String.valueOf(i));
    }


}
