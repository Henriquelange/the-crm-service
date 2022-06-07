package com.theagilemonkeys.crm.service;

import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.entity.User;
import com.theagilemonkeys.crm.exception.BusinessException;
import com.theagilemonkeys.crm.integration.IdentityProviderIntegration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

  private final IdentityProviderIntegration identityProviderIntegration;

  public void changeTemporaryPassword(String userName, String temporaryPassword, String password)
      throws BusinessException {
    this.identityProviderIntegration.changeTemporaryPassword(userName, temporaryPassword, password);
  }

  public Authentication authenticate(String userName, String password)
      throws BusinessException {
    return this.identityProviderIntegration.authenticate(userName, password);
  }

  public void createUser(User user) throws BusinessException {
    this.identityProviderIntegration.createUser(user);
  }

  public void deleteUser(User user) throws BusinessException {
    this.identityProviderIntegration.deleteUser(user.getEmail());
  }

}
