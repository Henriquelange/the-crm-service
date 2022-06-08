package com.theagilemonkeys.crm.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
@EnableDynamoDBRepositories(basePackages = "com.theagilemonkeys.crm.repository")
public class DynamoDBConfig {

  @Value("${amazon.dynamodb.endpoint:}")
  private String awsEndpoint;

  @Value("${amazon.region}")
  private String awsRegion;

  @Bean
  public DynamoDBMapperConfig dynamoDBMapperConfig() {
    return DynamoDBMapperConfig.DEFAULT;
  }

  @Bean("amazonDynamoDB")
  @Profile("local")
  public AmazonDynamoDB amazonDynamoDBDefault() {
    return AmazonDynamoDBClientBuilder
        .standard()
        .withEndpointConfiguration(
            new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion))
        .withCredentials(new DefaultAWSCredentialsProviderChain())
        .build();
  }

  @Bean("amazonDynamoDB")
  @Profile("!local")
  public AmazonDynamoDB amazonDynamoDB() {
    return AmazonDynamoDBClientBuilder
        .standard()
        .withRegion(awsRegion)
        .withCredentials(
            new DefaultAWSCredentialsProviderChain())
        .build();
  }
}
