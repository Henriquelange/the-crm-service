package com.theagilemonkeys.crm.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

  private String email;

  private boolean admin;

  private String status;

  private Date createdDate;

  private Date lastModifiedDate;

}
