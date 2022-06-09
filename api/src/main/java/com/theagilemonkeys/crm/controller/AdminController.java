package com.theagilemonkeys.crm.controller;

import com.theagilemonkeys.crm.dto.AuthRequestDTO;
import com.theagilemonkeys.crm.dto.AuthTokensDTO;
import com.theagilemonkeys.crm.dto.DeleteUserDTO;
import com.theagilemonkeys.crm.dto.UpdateAdminStatusDTO;
import com.theagilemonkeys.crm.dto.UserDTO;
import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.entity.User;
import com.theagilemonkeys.crm.exception.BusinessException;
import com.theagilemonkeys.crm.mapper.AdminMapper;
import com.theagilemonkeys.crm.service.IdentityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin")
@RestController
@RequestMapping("/api/crm")
@Slf4j
public class AdminController {

  @Autowired
  private AdminMapper adminMapper;

  @Autowired
  private IdentityService identityService;

  @PostMapping("/login")
  public ResponseEntity<AuthTokensDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO)
      throws BusinessException {
    Authentication authenticationResponse = identityService.authenticate(
        adminMapper.authRequestDTOToUserBusinessEntity(authRequestDTO));

    AuthTokensDTO getTokenResponse =
        adminMapper.authenticationToAuthTokensDTO(authenticationResponse);
    return ResponseEntity.of(Optional.of(getTokenResponse));
  }

  @PostMapping("/admin/user")
  public ResponseEntity createUser(@Valid @RequestBody AuthRequestDTO authRequestDTO)
      throws BusinessException {
    identityService.createUser(adminMapper.authRequestDTOToUserBusinessEntity(authRequestDTO));

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/admin/users")
  public ResponseEntity<List<UserDTO>> listUsers()
      throws BusinessException {
    List<User> users = identityService.listUsers();

    return ResponseEntity.status(HttpStatus.OK).body(adminMapper.userListToUserDTOList(users));
  }

  @DeleteMapping("/admin/user")
  public ResponseEntity deleteUser(@Valid @RequestBody DeleteUserDTO deleteUserDTO)
      throws BusinessException {
    identityService.deleteUser(deleteUserDTO.getEmail());

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PutMapping("/admin/users/set-admin-status")
  public ResponseEntity setAdminStatus(
      @Valid @RequestBody UpdateAdminStatusDTO updateAdminStatusDTO)
      throws BusinessException {
    identityService.setAdminStatus(updateAdminStatusDTO.getEmail(),
        updateAdminStatusDTO.getAdmin());

    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
