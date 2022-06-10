package com.theagilemonkeys.crm.service;

import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.entity.User;
import com.theagilemonkeys.crm.exception.BusinessException;
import com.theagilemonkeys.crm.integration.IdentityProviderIntegration;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IdentityService {

  private final IdentityProviderIntegration identityProviderIntegration;

  public void changeTemporaryPassword(final String userName, final String temporaryPassword,
                                      final String password)
      throws BusinessException {
    this.identityProviderIntegration.changeTemporaryPassword(userName, temporaryPassword, password);
  }

  public Authentication authenticate(final User user)
      throws BusinessException {
    return this.identityProviderIntegration.authenticate(user.getEmail(),
        user.getPassword());
  }

  public void createUser(final User user) throws BusinessException {
    this.identityProviderIntegration.createUser(user);
  }

  public void deleteUser(final String username) throws BusinessException {
    this.identityProviderIntegration.deleteUser(username);
  }

  public List<User> listUsers() throws BusinessException {
    return this.identityProviderIntegration.listUsers();
  }

  public void setAdminStatus(final String username, final boolean isAdmin)
      throws BusinessException {
    this.identityProviderIntegration.setAdminStatus(username, isAdmin);
  }

}
