package com.smilebat.learntribe.learntribeinquisitve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * A Generic request mock
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(
    basePackages = {"com.smilebat.learntribe.learntribeclients", "com.smilebat.learntribe"})
@EnableFeignClients(basePackages = "com.smilebat.learntribe.learntribeclients")
@EnableSwagger2
public class LearntribeInquisitiveApplication {

  /**
   * Main method
   *
   * @param args the args
   */
  public static void main(String[] args) {
    SpringApplication.run(LearntribeInquisitiveApplication.class, args);
  }


}
