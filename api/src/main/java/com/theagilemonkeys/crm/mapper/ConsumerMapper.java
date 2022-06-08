package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.dto.AuthenticatedUserDTO;
import com.theagilemonkeys.crm.dto.CreateConsumerDTO;
import com.theagilemonkeys.crm.entity.Consumer;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    imports = {UUID.class, ZonedDateTime.class})
public interface ConsumerMapper {

  @Mapping(target = "id", expression = "java(UUID.randomUUID())")
  @Mapping(source = "consumerDTO.firstName", target = "firstName")
  @Mapping(source = "consumerDTO.lastName", target = "lastName")
  @Mapping(source = "consumerDTO.photoUrl", target = "photoUrl")
  @Mapping(source = "authenticatedUser.email", target = "lastModifiedBy")
  Consumer createConsumerDTOToConsumerBusinessEntity(CreateConsumerDTO consumerDTO, AuthenticatedUserDTO authenticatedUser);

}
