package com.theagilemonkeys.crm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAdminStatusDTO {

  @NotBlank(message = "email is mandatory")
  private String email;

  @NotNull(message = "admin is mandatory")
  private Boolean admin;

}
