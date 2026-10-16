package com.yin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yin
 * @date 2024-12-10
 * @Description
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.yin.mapper")
@EnableAsync
public class Application {
    public static void main (String[] args) {
        SpringApplication.run(Application.class);
    }
}
