package com.wei.aop;

import com.google.gson.Gson;
import com.wei.config.WebLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
@Aspect
@Component
//@Profile({"dev", "test"})
public class WebLogAspect {
    /** 换行符 */
    private static final String LINE_SEPARATOR = System.lineSeparator();
    /**
     * 切点
     */

    @Pointcut("@annotation(com.wei.config.WebLog)")
    public void WebLog(){

    }
/**
 * 环绕
 */
@Around("WebLog()")
public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
  long starttime=System.currentTimeMillis();
  Object result= proceedingJoinPoint.proceed();//执行切点
    System.out.println("response args:"+new Gson().toJson(result));
    System.out.println("time-consuming:"+(System.currentTimeMillis()-starttime));
    return result;
}
@Before("WebLog()")
public void doBefore(JoinPoint joinPoint) throws Throwable{
    // 开始打印请求日志
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();

    // 获取 @WebLog 注解的描述信息
    String methodDescription = getAspectLogDescription(joinPoint);

    // 打印请求相关参数
    System.out.println("========================================== Start ==========================================");
    // 打印请求 url
    System.out.println("URL            : {}"+request.getRequestURL().toString());
    // 打印描述信息
    System.out.println("Description    : {}"+methodDescription);
    // 打印 Http method
    System.out.println("HTTP Method    : {}"+request.getMethod());
    // 打印调用 controller 的全路径以及执行方法
    System.out.println("Class Method   : {}.{}"+joinPoint.getSignature().getDeclaringTypeName()+joinPoint.getSignature().getName());
    // 打印请求的 IP
    System.out.println("IP             : {}"+request.getRemoteAddr());
    // 打印请求入参
    System.out.println("Request Args   : {}"+new Gson().toJson(joinPoint.getArgs()));
}

     /*
     * 在切点之后织入
     * @throws Throwable
     */
    @After("WebLog()")
    public void doAfter() throws Throwable {
        // 接口结束后换行，方便分割查看
        System.out.println("=========================================== End ===========================================" + LINE_SEPARATOR);
    }
    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @throws Exception
     */
    public String getAspectLogDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder("");
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description.append(method.getAnnotation(WebLog.class).description());
                    break;
                }
            }
        }
        return description.toString();
    }
}
