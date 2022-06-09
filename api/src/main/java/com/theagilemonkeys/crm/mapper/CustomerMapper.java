package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.dto.CreateCustomerDTO;
import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.entity.ProfilePhoto;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    imports = UUID.class)
public interface CustomerMapper {

  @Mapping(target = "id", expression = "java(UUID.randomUUID())")
  @Mapping(source = "customerDTO.firstName", target = "firstName")
  @Mapping(source = "customerDTO.lastName", target = "lastName")
  @Mapping(source = "customerDTO.photoUrl", target = "photoUrl")
  @Mapping(source = "authenticatedUser.email", target = "lastModifiedBy")
  Customer createCustomerDTOToCustomerBusinessEntity(CreateCustomerDTO customerDTO,
                                                     AuthenticatedUserDTO authenticatedUser);

  @Mapping(source = "customerId", target = "customerId")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "file", target = "file")
  ProfilePhoto toProfilePhotoBusinessEntity(UUID customerId, String name, MultipartFile file);

}
