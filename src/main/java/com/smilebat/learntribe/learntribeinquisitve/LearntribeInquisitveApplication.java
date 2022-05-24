package com.smilebat.learntribe.learntribeinquisitve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = "com.smilebat.learntribe")
public class LearntribeInquisitveApplication {

  public static void main(String[] args) {
    SpringApplication.run(LearntribeInquisitveApplication.class, args);
  }
}
