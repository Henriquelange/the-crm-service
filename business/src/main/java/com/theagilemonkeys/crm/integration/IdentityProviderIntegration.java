package com.theagilemonkeys.crm.integration;

import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.entity.User;
import com.theagilemonkeys.crm.exception.BusinessException;
import java.util.List;

public interface IdentityProviderIntegration {

  void createUser(User user) throws BusinessException;

  void changeTemporaryPassword(String userName, String temporaryPassword, String finalPassword)
      throws BusinessException;

  Authentication authenticate(String userName, String password) throws BusinessException;

  void deleteUser(String userName) throws BusinessException;

  List<User> listUsers() throws BusinessException;

  void setAdminStatus(final String userName, final boolean isAdmin) throws BusinessException;

}
