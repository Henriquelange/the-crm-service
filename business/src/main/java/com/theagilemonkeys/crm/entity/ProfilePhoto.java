package com.theagilemonkeys.crm.entity;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProfilePhoto {

  @NonNull
  private UUID customerId;

  @NonNull
  private String name;

  @NonNull
  private MultipartFile file;

}
