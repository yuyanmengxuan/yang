package com.wei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.LinkedList;

@SpringBootApplication
@MapperScan("com.wei.mapper")
public class YangApplication {

	public static void main(String[] args) {
		System.out.println("1");
		SpringApplication.run(YangApplication.class, args);
		System.out.println("2");
	}
}
