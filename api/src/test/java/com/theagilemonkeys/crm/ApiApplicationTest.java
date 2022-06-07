package com.theagilemonkeys.crm;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ApiApplicationTest {

  public static void main(String[] args) throws Exception {
    new SpringApplicationBuilder(ApiApplicationTest.class).run(args);
  }

}
