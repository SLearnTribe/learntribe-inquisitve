package com.smilebat.learntribe.learntribeinquisitve.configuration;

import java.io.IOException;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Overrided Configuration for HTTPS in tomcat
 *
 * <p>Key store generation for executable jars.
 *
 * <p>Copyright &copy; 2023 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Configuration
public class TomcatConfig {

  /**
   * Servlet container.
   *
   * @return the {@link TomcatServletWebServerFactory}
   * @throws IOException on exception.
   */
  @Bean
  public TomcatServletWebServerFactory servletContainer() throws IOException {
    final int port = 8443;
    final String keystoreFile = new ClassPathResource("security.crt").getFile().getAbsolutePath();
    final String keystorePass = "changeit";
    final String keystoreType = "pkcs12";
    final String keystoreProvider = "SunJSSE";
    final String keystoreAlias = "smilebat";

    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(port);
    factory.addConnectorCustomizers(
        con -> {
          Http11NioProtocol proto = (Http11NioProtocol) con.getProtocolHandler();
          proto.setSSLEnabled(true);
          con.setScheme("https");
          con.setSecure(true);
          proto.setKeystoreFile(keystoreFile);
          proto.setKeystorePass(keystorePass);
          proto.setKeystoreType(keystoreType);
          proto.setProperty("keystoreProvider", keystoreProvider);
          proto.setKeyAlias(keystoreAlias);
        });
    return factory;
  }
}
