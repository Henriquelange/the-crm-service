package com.theagilemonkeys.crm.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTProcessorConfig {


  @Value("${security.jwt.jwkSetConnectionTimeout}")
  private int jwkSetConnectionTimeout;

  @Value("${security.jwt.jwkSetReadTimeout}")
  private int jwkSetReadTimeout;

  @Value("${security.jwt.jwkSetURL}")
  private String jwkSetURL;

  @Value("${security.jwt.jwkSetAlgorithm}")
  private String jwkSetAlgorithm;

  @Bean
  public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor()
      throws MalformedURLException {
    ResourceRetriever resourceRetriever = new DefaultResourceRetriever(jwkSetConnectionTimeout,
                                                                       jwkSetReadTimeout);
    URL jwksURL = new URL(jwkSetURL);
    RemoteJWKSet<SecurityContext> keySource = new RemoteJWKSet<>(jwksURL, resourceRetriever);
    ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
    JWSAlgorithm jwsAlgorithm = new JWSAlgorithm(jwkSetAlgorithm);
    JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(jwsAlgorithm,
                                                                                   keySource);
    jwtProcessor.setJWSKeySelector(keySelector);
    return jwtProcessor;
  }

}
