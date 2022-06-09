package com.theagilemonkeys.crm.entity;

import java.util.Date;
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

  private boolean admin;

  private String status;

  private Date createdDate;

  private Date lastModifiedDate;

}
