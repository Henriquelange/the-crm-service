package com.theagilemonkeys.crm.adapter;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminRemoveUserFromGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminRespondToAuthChallengeRequest;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.ChallengeNameType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.ListUsersInGroupRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersInGroupResult;
import com.amazonaws.services.cognitoidp.model.ListUsersRequest;
import com.amazonaws.services.cognitoidp.model.ListUsersResult;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.amazonaws.services.cognitoidp.model.UserType;
import com.theagilemonkeys.crm.entity.Authentication;
import com.theagilemonkeys.crm.entity.User;
import com.theagilemonkeys.crm.entity.enums.CognitoApiFieldsEnum;
import com.theagilemonkeys.crm.entity.enums.CognitoAttributeFieldEnum;
import com.theagilemonkeys.crm.exception.BusinessException;
import com.theagilemonkeys.crm.exception.BusinessExceptionEnum;
import com.theagilemonkeys.crm.integration.IdentityProviderIntegration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IdentityProviderCognitoAdapter implements IdentityProviderIntegration {

  private static final List<String> USER_LIST_ATTRIBUTES =
      List.of(CognitoAttributeFieldEnum.EMAIL.getName());

  @Value("${amazon.cognito.userPoolId}")
  private String userPoolId;

  @Value("${amazon.cognito.clientId}")
  private String clientId;

  @Autowired
  private AWSCognitoIdentityProvider awsCognitoIdentityProvider;

  @Override
  public void createUser(final User user) throws BusinessException {
    try {

      AttributeType emailAttr = new AttributeType().withName(
              CognitoAttributeFieldEnum.EMAIL.getName())
          .withValue(user.getEmail());

      AttributeType emailVerifiedAttr = new AttributeType().withName(
              CognitoAttributeFieldEnum.EMAIL_VERIFIED.getName())
          .withValue(CognitoAttributeFieldEnum.EMAIL_VERIFIED.getDefaultValue());

      AdminCreateUserRequest createUserRequest =
          new AdminCreateUserRequest().withUserPoolId(userPoolId).withUsername(user.getEmail())
              .withUserAttributes(emailAttr, emailVerifiedAttr)
              .withMessageAction(MessageActionType.SUPPRESS)
              .withTemporaryPassword(user.getPassword());

      awsCognitoIdentityProvider.adminCreateUser(createUserRequest);
      log.debug("Created Cognito user: {}", user.getEmail());

      AdminSetUserPasswordRequest adminSetUserPasswordRequest =
          new AdminSetUserPasswordRequest().withUsername(user.getEmail())
              .withUserPoolId(userPoolId).withPassword(user.getPassword())
              .withPermanent(Boolean.TRUE);

      awsCognitoIdentityProvider.adminSetUserPassword(adminSetUserPasswordRequest);
      log.debug("Updated temporary user password to permanent - User: {}", user.getEmail());

    } catch (AWSCognitoIdentityProviderException ex) {
      log.error("Error while creating Cognito user - Error:", ex);
      throw new BusinessException(ex.getStatusCode(), ex.getErrorMessage());
    }
  }

  @Override
  public void changeTemporaryPassword(final String userName, final String temporaryPassword,
                                      final String finalPassword) throws BusinessException {
    try {

      final Map<String, String> authParams = new HashMap<>();
      authParams.put(CognitoApiFieldsEnum.USERNAME.name(), userName);
      authParams.put(CognitoApiFieldsEnum.PASSWORD.name(), temporaryPassword);

      final AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest();
      authRequest.withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
          .withClientId(clientId)
          .withUserPoolId(userPoolId)
          .withAuthParameters(authParams);

      log.debug(
          "Calling authenticate api - User:{}",
          userName);

      AdminInitiateAuthResult result = awsCognitoIdentityProvider.adminInitiateAuth(authRequest);

      final Map<String, String> challengeResponses = new HashMap<>();
      challengeResponses.put(CognitoApiFieldsEnum.USERNAME.name(), userName);
      challengeResponses.put(CognitoApiFieldsEnum.PASSWORD.name(), temporaryPassword);
      challengeResponses.put(CognitoApiFieldsEnum.NEW_PASSWORD.name(), finalPassword);

      final AdminRespondToAuthChallengeRequest request =
          new AdminRespondToAuthChallengeRequest().withChallengeName(
                  ChallengeNameType.NEW_PASSWORD_REQUIRED)
              .withChallengeResponses(challengeResponses)
              .withClientId(clientId)
              .withSession(result.getSession())
              .withUserPoolId(userPoolId);

      log.debug(
          "Calling authenticate challenge api - User:{}",
          userName);

      awsCognitoIdentityProvider.adminRespondToAuthChallenge(request);

      log.info(
          "Password changed - User:{}",
          userName);

    } catch (RuntimeException ex) {
      log.error("Runtime exception while changing password for user {} - Error:", userName, ex);
      throw BusinessExceptionEnum.APPLICATION_ERROR.exception();
    }

  }

  public Authentication authenticate(final String userName, final String password)
      throws BusinessException {
    try {
      final Map<String,
          String> authParams = new HashMap<>();
      authParams.put(CognitoApiFieldsEnum.USERNAME.name(), userName);
      authParams.put(CognitoApiFieldsEnum.PASSWORD.name(), password);

      final InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest().withClientId(
              clientId).withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
          .withAuthParameters(authParams);

      final InitiateAuthResult result = awsCognitoIdentityProvider.initiateAuth(
          initiateAuthRequest);

      log.info("User {} authenticated", userName);

      return Authentication.builder()
          .idToken(result.getAuthenticationResult().getIdToken())
          .refreshToken(result.getAuthenticationResult().getRefreshToken())
          .accessToken(result.getAuthenticationResult().getAccessToken())
          .build();
    } catch (RuntimeException ex) {
      log.error("Error while getting user tokens - Error:", ex);
      throw BusinessExceptionEnum.APPLICATION_ERROR.exception();
    }

  }

  @Override
  public void deleteUser(String userName) throws BusinessException {
    try {
      AdminDeleteUserRequest req = new AdminDeleteUserRequest();
      req.setUsername(userName);
      req.setUserPoolId(userPoolId);
      awsCognitoIdentityProvider.adminDeleteUser(req);
      log.info("User {} deleted by admin", userName);
    } catch (RuntimeException ex) {
      log.error("Error while deleting Cognito user - Error:", ex);
      throw BusinessExceptionEnum.APPLICATION_ERROR.exception();
    }
  }

  @Override
  public List<User> listUsers() throws BusinessException {
    try {
      ListUsersRequest req = new ListUsersRequest();
      req.setUserPoolId(userPoolId);
      req.setAttributesToGet(USER_LIST_ATTRIBUTES);
      ListUsersResult listUsersResult = awsCognitoIdentityProvider.listUsers(req);

      List<String> cognitoAdminUsers = fetchAdminUsers();

      List<User> users = new ArrayList<>();
      for (UserType cognitoUser : listUsersResult.getUsers()) {
        users.add(User.builder()
            .email(cognitoUser.getUsername())
            .status(cognitoUser.getUserStatus())
            .createdDate(cognitoUser.getUserCreateDate())
            .lastModifiedDate(cognitoUser.getUserLastModifiedDate())
            .admin(cognitoAdminUsers.contains(cognitoUser.getUsername()) ? true : false)
            .build());
      }

      return users;

    } catch (RuntimeException ex) {
      log.error("Error while listing Cognito users - Error:", ex);
      throw BusinessExceptionEnum.APPLICATION_ERROR.exception();
    }
  }

  @Override
  public void setAdminStatus(final String username, final boolean isAdmin)
      throws BusinessException {
    try {
      if (isAdmin) {
        AdminAddUserToGroupRequest request = new AdminAddUserToGroupRequest();
        request.setUserPoolId(userPoolId);
        request.setUsername(username);
        request.setGroupName("admin");
        awsCognitoIdentityProvider.adminAddUserToGroup(request);
      } else {
        AdminRemoveUserFromGroupRequest request = new AdminRemoveUserFromGroupRequest();
        request.setUserPoolId(userPoolId);
        request.setUsername(username);
        request.setGroupName("admin");
        awsCognitoIdentityProvider.adminRemoveUserFromGroup(request);
      }
      log.info("Updated admin status to {} for user {}", isAdmin, username);
    } catch (RuntimeException ex) {
      log.error("Error while changing admin status for user {} - Error:", username, ex);
      throw BusinessExceptionEnum.APPLICATION_ERROR.exception();
    }
  }

  private List<String> fetchAdminUsers() throws BusinessException {
    try {
      ListUsersInGroupRequest request = new ListUsersInGroupRequest();
      request.setUserPoolId(userPoolId);
      request.setGroupName("admin");
      List<String> adminUsers = new ArrayList<>();
      for (UserType cognitoAdminUser : awsCognitoIdentityProvider.listUsersInGroup(request)
          .getUsers()) {
        adminUsers.add(cognitoAdminUser.getUsername());
      }
      return adminUsers;
    } catch (RuntimeException ex) {
      log.error("Error while fetching admin users in Cognito - Error:", ex);
      throw BusinessExceptionEnum.APPLICATION_ERROR.exception();
    }
  }

}
