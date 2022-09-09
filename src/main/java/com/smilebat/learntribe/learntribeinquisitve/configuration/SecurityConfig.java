package com.smilebat.learntribe.learntribeinquisitve.configuration;

import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Slf4j
@SuppressFBWarnings(justification = "Generated code")
public class SecurityConfig {

  @Bean
  protected SecurityFilterChain configure(HttpSecurity security) throws Exception {

    security
        .cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    return security.build();
  }

  /**
   * Custom cors configuration.
   *
   * @return the {@link CorsConfigurationSource}
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(ImmutableList.of("http://localhost:3000")); // www - obligatory
    //        configuration.setAllowedOrigins(ImmutableList.of("*"));  //set access from all domains
    configuration.setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "DELETE"));
    // configuration.setAllowCredentials(true);
    // configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control",
    // "Content-Type"));
    configuration.setAllowedHeaders(ImmutableList.of("*"));

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
