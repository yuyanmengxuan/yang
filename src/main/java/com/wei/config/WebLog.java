package com.wei.config;

import java.lang.annotation.*;

/**
 * 日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
    String description() default "";

}
