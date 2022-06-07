package com.theagilemonkeys.crm.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSCognitoIdentityProviderConfig {

  @Value("${amazon.region}")
  private String awsRegion;

  @Bean
  public AWSCognitoIdentityProvider configureCognitoIdentityProvider() {
    return AWSCognitoIdentityProviderClientBuilder
        .standard().withCredentials(new DefaultAWSCredentialsProviderChain())
        .withRegion(awsRegion).build();
  }

}
