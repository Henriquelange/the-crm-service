package com.theagilemonkeys.crm.entity.enums;

import lombok.Getter;

@Getter
public enum CognitoAttributeFieldEnum {
  USERNAME("cognito:username", ""),
  EMAIL("email", ""),
  EMAIL_VERIFIED("email_verified", "false"),
  GROUPS("cognito:groups", "");

  private final String name;
  private final String defaultValue;

  CognitoAttributeFieldEnum(String name, String defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }
}
