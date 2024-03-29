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
 * Application Programming Interface definition for Apache Airavata User Management Services.
 */

include "userAPIErrors.thrift"
include "userAPIModels.thrift"

namespace java org.apache.airavata.userapi
namespace php Airavata.UserAPI
namespace cpp airavata.userapi
namespace perl AiravataUserAPI
namespace py airavata.userapi
namespace js AiravataUserAPI

/*
 * UserAPI Interface Versions depend upon this Thrift Interface File. When Making changes, please edit the
 * Version Constants according to Semantic Versioning Specification (SemVer) http://semver.org.
 *
 * Note: The Airavata UserAPI version may be different from the Airavata software release versions.
 *
 * The Airavata UserAPI version is composed as a dot delimited string with major, minor, and patch level components.
 *
 *  - Major: Incremented for backward incompatible changes. An example would be changes to interfaces.
 *  - Minor: Incremented for backward compatible changes. An example would be the addition of a new optional methods.
 *  - Patch: Incremented for bug fixes. The patch level should be increased for every edit that doesn't result
 *              in a change to major/minor version numbers.
*/
const string USER_API_VERSION = "0.12.0"

service UserAPI {

/*
 * Apache Airavata UserAPI Service Methods.
*/

  /** Query UserAPI to fetch the API version */
  string getAPIVersion()
        throws (1: userAPIErrors.InvalidRequestException ire,
                2: userAPIErrors.UserAPISystemException ase)

  /**
   * Authenticate Gateway
   *
  */
  userAPIModels.AuthenticationResponse authenticateGateway (1: required string username,
                     2: required string password)
      throws (1: userAPIErrors.InvalidRequestException ire,
              2: userAPIErrors.UserAPISystemException ase,
              3: userAPIErrors.AuthenticationException are)

/**
   * Check username exists
   *
  */
  bool checkUsernameExists (1: required string username,
                            2: required string token)
        throws (1: userAPIErrors.InvalidRequestException ire,
                2: userAPIErrors.AuthorizationException are,
                3: userAPIErrors.UserAPISystemException ase)

/**
   * Add new user
   *
  */
  void createNewUser (1: required string userName,
                      2: required string password,
                      3: required userAPIModels.UserProfile userProfile,
                      4: required string token)
        throws (1: userAPIErrors.InvalidRequestException ire,
                2: userAPIErrors.AuthorizationException are,
                3: userAPIErrors.UserAPISystemException ase)

/**
   * Update user profile
   *
  */
  void updateUserProfile (1: required string userName,
                          2: required userAPIModels.UserProfile userProfile,
                          3: required string token)
        throws (1: userAPIErrors.InvalidRequestException ire,
                2: userAPIErrors.AuthorizationException are,
                3: userAPIErrors.UserAPISystemException ase)

/**
   * Get user profile
   *
  */
  userAPIModels.UserProfile getUserProfile (1: required string userName,
                          2: required string token)
        throws (1: userAPIErrors.InvalidRequestException ire,
                2: userAPIErrors.AuthorizationException are,
                3: userAPIErrors.UserAPISystemException ase)


  /**
     * Remove user
     *
    */
  void removeUser (1: required string userName,
                   2: required string token)
          throws (1: userAPIErrors.InvalidRequestException ire,
                  2: userAPIErrors.AuthorizationException are,
                  3: userAPIErrors.UserAPISystemException ase)

  /**
    * Update user password
    *
  */
  void updateUserPassword (1: required string userName,
                           2: required string newPassword,
                           3: required string oldPassword,
                           4: required string token)
            throws (1: userAPIErrors.InvalidRequestException ire,
                    2: userAPIErrors.AuthorizationException are,
                    3: userAPIErrors.UserAPISystemException ase)

  /**
    * Authenticate user
    *
  */
  userAPIModels.APIPermissions authenticateUser (1: required string userName,
                         2: required string password,
                         3: required string token)
            throws (1: userAPIErrors.InvalidRequestException ire,
                    2: userAPIErrors.AuthorizationException are,
                    3: userAPIErrors.UserAPISystemException ase,
                    4: userAPIErrors.AuthenticationException ate)

  /**
    * Add user to a role
    *
  */
  void addUserToRole (1: required string userName,
                      2: required string roleName,
                      3: required string token)
            throws (1: userAPIErrors.InvalidRequestException ire,
                    2: userAPIErrors.AuthorizationException are,
                    3: userAPIErrors.UserAPISystemException ase)
  /**
    * Remove user from role
    *
  */
  void removeUserFromRole (1: required string userName,
                           2: required string roleName,
                           3: required string token)
            throws (1: userAPIErrors.InvalidRequestException ire,
                    2: userAPIErrors.AuthorizationException are,
                    3: userAPIErrors.UserAPISystemException ase)

  /**
    * Get user list of a particular role
    *
  */
  list<string> getUserListOfRole (1: required string roleName,
                                 2: required string token)
            throws (1: userAPIErrors.InvalidRequestException ire,
                    2: userAPIErrors.AuthorizationException are,
                    3: userAPIErrors.UserAPISystemException ase)

  /**
    * Get role list of a particular user
    *
  */
  list<string> getRoleListOfUser (1: required string username,
                                 2: required string token)
            throws (1: userAPIErrors.InvalidRequestException ire,
                    2: userAPIErrors.AuthorizationException are,
                    3: userAPIErrors.UserAPISystemException ase)

  /**
    * Get list of all roles
    *
  */
  list<string> getAllRoleNames (1: required string token)
            throws (1: userAPIErrors.InvalidRequestException ire,
                    2: userAPIErrors.AuthorizationException are,
                    3: userAPIErrors.UserAPISystemException ase)

  /**
      * Get permission for user
      *
    */
    userAPIModels.APIPermissions getUserPermissions (1: required string username, 2: required string token)
              throws (1: userAPIErrors.InvalidRequestException ire,
                      2: userAPIErrors.AuthorizationException are,
                      3: userAPIErrors.UserAPISystemException ase)


  /**
        * Check permission for permission string
        *
      */
      bool checkPermission (1: required string username, 2: required string permissionString, 3: required string token)
                throws (1: userAPIErrors.InvalidRequestException ire,
                        2: userAPIErrors.AuthorizationException are,
                        3: userAPIErrors.UserAPISystemException ase)
}