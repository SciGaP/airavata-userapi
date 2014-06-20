/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

/*
* This file describes the definitions of the Error Messages that can occur
*  when invoking Apache Airavata UserAPI Services through the API. In addition Thrift provides
*  built in funcationality to raise TApplicationException for all internal server errors.
*/

namespace java org.apache.airavata.userapi.error
namespace php Airavata.UserAPI.Error
namespace cpp airavata.userapi.error
namespace perl AiravataUserAPIError
namespace py airavata.userapi.error
namespace js AiravataUserAPIError

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

enum UserAPIErrorType {
  UNKNOWN,
  PERMISSION_DENIED,
  INTERNAL_ERROR,
  AUTHENTICATION_FAILURE,
  INVALID_AUTHORIZATION,
  AUTHORIZATION_EXPIRED,
  UNSUPPORTED_OPERATION
}


/**
* This exception is thrown for invalid requests that occur from any reasons like required input parameters are missing,
*  or a parameter is malformed.
*
*  message: contains the associated error message.
*/
exception InvalidRequestException {
    1: required string message
}


/**
*  This exception is thrown when RPC timeout gets exceeded.
*/
exception TimedOutException {
}

/**
* This exception is thrown for invalid authentication requests.
*
*  message: contains the cause of the authorization failure.
*/
exception AuthenticationException {
    1: required string message
}

/**
* This exception is thrown for invalid authorization requests such user does not have acces
*
*  message: contains the authorization failure message
*/
exception AuthorizationException {
    1: required string message
}


/**
 * This exception is thrown by Airavata UserAPI Services when a call fails as a result of
 * a problem in the service that could not be changed through client's action.
 *
 * airavataErrorType:  The message type indicating the error that occurred.
 *   must be one of the values of UserAPIErrorType.
 *
 * message:  This may contain additional information about the error
 *
 */
exception UserAPISystemException {
  1:  required  UserAPIErrorType userAPIErrorType,
  2:  optional  string message,
}