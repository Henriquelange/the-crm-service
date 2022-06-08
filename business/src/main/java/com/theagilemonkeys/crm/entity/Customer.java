package com.theagilemonkeys.crm.entity;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

  @NonNull
  private UUID id;

  @NonNull
  private String firstName;

  @NonNull
  private String lastName;

  private String photoUrl;

  private ZonedDateTime createdDate;

  private ZonedDateTime lastUpdatedDate;

  private String lastModifiedBy;

}
