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
  * This file describes various data models which are used in the User API
  */

namespace java org.apache.airavata.userapi.models
namespace php Airavata.UserAPI.Models
namespace cpp airavata.userapi.models
namespace perl AiravataUserAPIModels
namespace py airavata.userapi.models
namespace js AiravataUserAPIModels

struct UserProfile {
    1: required string firstName,
    2: required string lastName,
    3: required string emailAddress,
    4: optional string organization,
    5: optional string address,
    6: optional string country,
    7: optional string telephone,
    8: optional string mobile,
    9: optional string im,
    10: optional string url
}

struct AuthenticationResponse{
    1: required i32 expiresIn,
    2: required string accessToken
}

struct APIPermissions {
    1: required map<string, bool> airavataAPIPermissions,
    2: required map<string, bool> airavataAppCatalogPermissions,
    3: required string signature
}