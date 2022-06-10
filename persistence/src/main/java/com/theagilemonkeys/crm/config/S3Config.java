package com.theagilemonkeys.crm.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class S3Config {

  @Value("${amazon.dynamodb.endpoint:}")
  private String awsEndpoint;

  @Value("${amazon.region}")
  private String awsRegion;

  @Bean("amazonS3")
  @Profile({"local", "test"})
  public AmazonS3 amazonS3Default() {
    return AmazonS3ClientBuilder
        .standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion))
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .build();

  }

  @Bean("amazonS3")
  @Profile("dev")
  public AmazonS3 amazonS3() {
    return AmazonS3ClientBuilder
        .standard()
        .withRegion(awsRegion)
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .build();

  }
}
