package com.theagilemonkeys.crm.controller;

import com.theagilemonkeys.crm.dto.AuthRequestDTO;
import com.theagilemonkeys.crm.dto.AuthTokensDTO;
import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.exception.BusinessException;
import com.theagilemonkeys.crm.mapper.AdminMapper;
import com.theagilemonkeys.crm.service.IdentityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ResponseEntity<AuthTokensDTO> createUser(@Valid @RequestBody AuthRequestDTO authRequestDTO)
      throws BusinessException {
    identityService.createUser(adminMapper.authRequestDTOToUserBusinessEntity(authRequestDTO));

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
