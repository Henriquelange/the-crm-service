package com.theagilemonkeys.crm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authentication {

  private String accessToken;

  private String refreshToken;

  private String idToken;

}
