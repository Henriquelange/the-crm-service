package com.theagilemonkeys.crm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @NonNull
  private String email;

  private String password;

  private String phoneNumber;

}
