package com.theagilemonkeys.crm;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ApiApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ApiApplication.class).run(args);
  }
}
