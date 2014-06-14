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
import org.apache.airavata.userapi.error.AuthenticationException;
import org.apache.airavata.userapi.error.AuthorizationException;
import org.apache.airavata.userapi.error.InvalidRequestException;
import org.apache.airavata.userapi.error.UserAPISystemException;
import org.apache.airavata.userapi.server.utils.LoginAdminServiceClient;
import org.apache.axis2.AxisFault;
import org.apache.thrift.TException;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;

import java.rmi.RemoteException;

public class UserAPIServerHandler implements UserAPI.Iface{

    private String backendUrl;

    private LoginAdminServiceClient loginAdminServiceClient;

    public UserAPIServerHandler(String url) throws AxisFault {
        this.backendUrl = url;
        this.loginAdminServiceClient = new LoginAdminServiceClient(backendUrl);
    }

    @Override
    public String getAPIVersion() throws InvalidRequestException, UserAPISystemException, TException {
        return UserAPIConstants.USER_API_VERSION;
    }

    @Override
    public String adminLogin(String username, String password) throws InvalidRequestException, UserAPISystemException, AuthenticationException, TException {
        if(username.isEmpty()|| password.isEmpty() || username.contains(" ")|| password.contains(" ")){
            throw new InvalidRequestException();
        }

        String sessionCookie = null;
        try {
            sessionCookie = loginAdminServiceClient.authenticate(username,password);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new UserAPISystemException();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
            throw new AuthenticationException();
        }

        return sessionCookie;
    }

    @Override
    public void adminLogout() throws InvalidRequestException, UserAPISystemException, AuthenticationException, TException {
    }

    @Override
    public boolean checkUsernameExists(String username) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {
        return false;
    }

    @Override
    public void createNewUser(String userName, String password) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {

    }

    @Override
    public void removeUser(String userName) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {

    }

    @Override
    public void updateUserPassword(String userName, String newPassword) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {

    }

    @Override
    public void activateUser(String userName) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {

    }

    @Override
    public void deactivateUser(String userName) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {

    }

    @Override
    public void authenticateUser(String userName, String password) throws InvalidRequestException, AuthorizationException, UserAPISystemException, TException {

    }
}
