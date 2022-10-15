// package com.smilebat.learntribe.learntribeinquisitve.configuration;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
// import springfox.documentation.builders.PathSelectors;
// import springfox.documentation.builders.RequestHandlerSelectors;
// import springfox.documentation.spi.DocumentationType;
// import springfox.documentation.spring.web.plugins.Docket;
// import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
//
// @Configuration
// @EnableWebMvc
// @EnableSwagger2WebMvc
// public class SwaggerConfig extends WebMvcConfigurerAdapter {
//
//  //    private ApiInfo apiInfo() {
//  //        return new ApiInfo("MyApp Rest APIs",
//  //                "APIs for MyApp.",
//  //                "1.0",
//  //                "Terms of service",
//  //                new Contact("test", "www.org.com", "test@emaildomain.com"),
//  //                "License of API",
//  //                "API license URL",
//  //                Collections.emptyList());
//  //    }
//
//  /**
//   * Activates the swagger documentation.
//   *
//   * @return void
//   */
//  @Bean
//  public Docket api() {
//    return new Docket(DocumentationType.OAS_30)
//        // .apiInfo(apiInfo())
//        .select()
//        .apis(RequestHandlerSelectors.any())
//        .paths(PathSelectors.any())
//        .build();
//  }
//
//  /** Function to add resource handler */
//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry
//        .addResourceHandler("swagger-ui.html")
//        .addResourceLocations("classpath:/META-INF/resources/");
//
//    registry
//        .addResourceHandler("/webjars/**")
//        .addResourceLocations("classpath:/META-INF/resources/webjars/");
//  }
// }
