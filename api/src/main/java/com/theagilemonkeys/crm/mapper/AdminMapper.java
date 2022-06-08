package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.dto.AuthTokensDTO;
import com.theagilemonkeys.crm.entity.Authentication;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminMapper {

  AuthTokensDTO authenticationToAuthTokensDTO(Authentication authentication);

}
