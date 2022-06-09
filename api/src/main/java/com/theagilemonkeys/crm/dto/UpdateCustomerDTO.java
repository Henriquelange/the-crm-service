package com.theagilemonkeys.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerDTO {

  private String firstName;

  private String lastName;

  private String photoUrl;
}
