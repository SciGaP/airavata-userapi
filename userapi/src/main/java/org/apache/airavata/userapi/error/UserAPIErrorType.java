/**
 * Autogenerated by Thrift Compiler (1.0.0-dev)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.airavata.userapi.error;


/**
 * A list of Airavata API Error Message Types
 * 
 *  UNKNOWN: No information available about the error
 * 
 *  PERMISSION_DENIED: Not permitted to perform action
 * 
 *  INTERNAL_ERROR: Unexpected problem with the service
 * 
 *  AUTHENTICATION_FAILURE: The client failed to authenticate.
 * 
 *  INVALID_AUTHORIZATION: Security Token and/or Username and/or password is incorrect
 * 
 *  AUTHORIZATION_EXPIRED: Authentication token expired
 * 
 *  UNSUPPORTED_OPERATION: Operation denied because it is currently unsupported.
 */
public enum UserAPIErrorType implements org.apache.thrift.TEnum {
  UNKNOWN(0),
  PERMISSION_DENIED(1),
  INTERNAL_ERROR(2),
  AUTHENTICATION_FAILURE(3),
  INVALID_AUTHORIZATION(4),
  AUTHORIZATION_EXPIRED(5),
  UNSUPPORTED_OPERATION(6);

  private final int value;

  private UserAPIErrorType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static UserAPIErrorType findByValue(int value) { 
    switch (value) {
      case 0:
        return UNKNOWN;
      case 1:
        return PERMISSION_DENIED;
      case 2:
        return INTERNAL_ERROR;
      case 3:
        return AUTHENTICATION_FAILURE;
      case 4:
        return INVALID_AUTHORIZATION;
      case 5:
        return AUTHORIZATION_EXPIRED;
      case 6:
        return UNSUPPORTED_OPERATION;
      default:
        return null;
    }
  }
}
