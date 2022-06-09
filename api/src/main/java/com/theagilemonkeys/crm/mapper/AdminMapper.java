package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.dto.AuthRequestDTO;
import com.theagilemonkeys.crm.dto.AuthTokensDTO;
import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminMapper {

  AuthTokensDTO authenticationToAuthTokensDTO(Authentication authentication);

  User authRequestDTOToUserBusinessEntity(AuthRequestDTO authRequestDTO);

}
