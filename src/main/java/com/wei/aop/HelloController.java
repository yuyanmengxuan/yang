package com.wei.aop;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试注解结合切面做日志的打印
 */
@RestController
public class HelloController {


    @RequestMapping("/aophello")
    @LoggerManage(logDescription = "hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/login/{username}/{password}")
    @LoggerManage(logDescription = "登陆")
    public String login(@PathVariable("username") String username,
                        @PathVariable("password") String password ) {
        return "登陆成功";


    }


}
