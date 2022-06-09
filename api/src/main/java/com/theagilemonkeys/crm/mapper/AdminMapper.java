package com.theagilemonkeys.crm.mapper;

import com.theagilemonkeys.crm.dto.AuthRequestDTO;
import com.theagilemonkeys.crm.dto.AuthTokensDTO;
import com.theagilemonkeys.crm.dto.UserDTO;
import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AdminMapper {

  AuthTokensDTO authenticationToAuthTokensDTO(Authentication authentication);

  User authRequestDTOToUserBusinessEntity(AuthRequestDTO authRequestDTO);

  UserDTO userBusinessToUserDTO(User user);

  List<UserDTO> userListToUserDTOList(List<User> userList);

}
