/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.userapi.server.handler;

import org.apache.airavata.userapi.UserAPI;
import org.apache.airavata.userapi.UserAPIConstants;
import org.apache.airavata.userapi.common.utils.Constants;
import org.apache.airavata.userapi.common.utils.ServerProperties;
import org.apache.airavata.userapi.error.*;
import org.apache.airavata.userapi.models.APIPermissions;
import org.apache.airavata.userapi.models.AuthenticationResponse;
import org.apache.airavata.userapi.models.UserProfile;
import org.apache.airavata.userapi.server.clients.LoginAdminServiceClient;
import org.apache.airavata.userapi.server.clients.RemoteAuthorizationManagerClient;
import org.apache.airavata.userapi.server.clients.UserStoreManagerServiceClient;
import org.apache.thrift.TException;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.um.ws.api.stub.UserStoreExceptionException;

import java.rmi.RemoteException;
import java.util.List;

public class UserAPIServerHandler implements UserAPI.Iface{

    private String backendUrl;

    private LoginAdminServiceClient loginAdminServiceClient;
    private UserStoreManagerServiceClient userStoreManagerServiceClient;
    private RemoteAuthorizationManagerClient remoteAuthorizationManagerClient;

    public UserAPIServerHandler(String url) throws RemoteException, UserStoreExceptionException {
        this.backendUrl = url;
        this.loginAdminServiceClient = new LoginAdminServiceClient(backendUrl);
        this.userStoreManagerServiceClient = new UserStoreManagerServiceClient(backendUrl);
        this.remoteAuthorizationManagerClient = new RemoteAuthorizationManagerClient(backendUrl);
    }

    @Override
    public String getAPIVersion() throws InvalidRequestException, UserAPISystemException, TException {
        return UserAPIConstants.USER_API_VERSION;
    }

    @Override
    public AuthenticationResponse authenticateGateway(String username, String password) throws InvalidRequestException, UserAPISystemException, AuthenticationException, TException {
        String token = null;
        try {
            token = loginAdminServiceClient.authenticate(username,password);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
            throw new AuthenticationException("invalid credentials for the admin user");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(token);
        authenticationResponse.setExpiresIn(Integer.parseInt(
                ServerProperties.getInstance().getProperty(Constants.TOKEN_LIFE_TIME,"3600")));

        return authenticationResponse;
    }

    @Override
    public boolean checkUsernameExists(String username, String token) throws InvalidRequestException,
            AuthorizationException, UserAPISystemException, TException {
        boolean isExistingUser = false;
        try {
            isExistingUser = userStoreManagerServiceClient.isExistingUser(username,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
        return isExistingUser;
    }

    @Override
    public void createNewUser(String userName, String password, UserProfile userProfile, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            userStoreManagerServiceClient.createNewUser(userName,password, userProfile, token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public void updateUserProfile(String userName, UserProfile userProfile, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            userStoreManagerServiceClient.updateUserProfile(userName,userProfile, token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public UserProfile getUserProfile(String userName, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            return userStoreManagerServiceClient.getUserProfile(userName, token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public void removeUser(String userName, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            userStoreManagerServiceClient.removeUser(userName,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public void updateUserPassword(String userName, String newPassword, String oldPassword, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            userStoreManagerServiceClient.changeUserPassword(userName,newPassword,oldPassword,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public APIPermissions authenticateUser(String userName, String password, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, AuthenticationException, TException {
        boolean isAuthentic = false;
        try {
            isAuthentic = userStoreManagerServiceClient.authenticateUser(userName,password,token);
            if(isAuthentic) {
                APIPermissions apiPermissions = remoteAuthorizationManagerClient.getUserPermissions(userName, token);
                return apiPermissions;
            }
            throw new AuthenticationException("Invalid username or password");
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public void addUserToRole(String userName, String roleName, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            userStoreManagerServiceClient.addUserToRole(userName,roleName,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public void removeUserFromRole(String userName, String roleName, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            userStoreManagerServiceClient.removeUserFromRole(userName,roleName,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public List<String> getUserListOfRole(String roleName, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            return userStoreManagerServiceClient.getUserListOfRole(roleName,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public List<String> getRoleListOfUser(String username, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            return userStoreManagerServiceClient.getRoleListOfUser(username,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public List<String> getAllRoleNames(String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            return userStoreManagerServiceClient.getRoleNames(token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public APIPermissions getUserPermissions(String username, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            return remoteAuthorizationManagerClient.getUserPermissions(username,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (Exception e) {
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    @Override
    public boolean checkPermission(String username, String permissionString, String token) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        try {
            return remoteAuthorizationManagerClient.checkPermission(username,permissionString,token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        } catch (UserStoreExceptionException e) {
            e.printStackTrace();
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }
}
