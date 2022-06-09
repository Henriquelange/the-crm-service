package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.dto.CreateCustomerDTO;
import com.theagilemonkeys.crm.dto.UpdateCustomerDTO;
import com.theagilemonkeys.crm.entity.Customer;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    imports = {UUID.class, ZonedDateTime.class})
public interface CustomerMapper {

  @Mapping(target = "id", expression = "java(UUID.randomUUID())")
  @Mapping(source = "customerDTO.firstName", target = "firstName")
  @Mapping(source = "customerDTO.lastName", target = "lastName")
  @Mapping(source = "customerDTO.photoUrl", target = "photoUrl")
  @Mapping(source = "authenticatedUser.email", target = "lastModifiedBy")
  Customer createCustomerDTOToCustomerBusinessEntity(CreateCustomerDTO customerDTO,
                                                     AuthenticatedUserDTO authenticatedUser);

  Customer updateCustomerDTOToExistingCustomerBusinessEntity(UpdateCustomerDTO customerDTO,
                                                     AuthenticatedUserDTO authenticatedUser);

}
