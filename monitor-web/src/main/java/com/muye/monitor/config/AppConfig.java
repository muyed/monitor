package com.muye.monitor.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = { "com.muye.monitor" })
@ImportResource(value = { "classpath*:spring-access-config.xml" })
public class AppConfig {
    public static void main(String[] args) {
        new SpringApplication(AppConfig.class).run(args);
    }

    @PostConstruct
    public String projectName(){
        System.setProperty("project.name","monitor");
        return null;
    }
}
