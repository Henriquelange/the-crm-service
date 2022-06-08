package com.theagilemonkeys.crm.controller;

import com.theagilemonkeys.crm.dto.AuthTokensDTO;
import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.exception.BusinessException;
import com.theagilemonkeys.crm.mapper.AdminMapper;
import com.theagilemonkeys.crm.service.IdentityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin")
@RestController
@RequestMapping("/api/crm/admin")
@Slf4j
public class AdminController {

  @Autowired
  private AdminMapper adminMapper;

  @Autowired
  private IdentityService identityService;

  @GetMapping("/login")
  public ResponseEntity<AuthTokensDTO> login(@RequestParam String userName,
                                             @RequestParam String password)
      throws BusinessException {
    Authentication authenticationResponse = identityService.authenticate(userName, password);
    log.info("User {} authenticated", userName);
    AuthTokensDTO getTokenResponse = adminMapper.authenticationToAuthTokensDTO(authenticationResponse);
    return ResponseEntity.of(Optional.of(getTokenResponse));
  }

}
