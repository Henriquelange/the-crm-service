package com.theagilemonkeys.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokensDTO {

  private String accessToken;

  private String refreshToken;

  private String idToken;

}
