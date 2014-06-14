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

package org.apache.airavata.userapi.server.utils;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.UserStoreExceptionException;

import java.rmi.RemoteException;

public class UserStoreManagerServiceClient {

    private final String serviceName = "RemoteUserStoreManagerService";
    private final String activeUserGroup = "active";
    private boolean userGroupsCreated = false;
    private RemoteUserStoreManagerServiceStub remoteUserStoreManagerServiceStub;
    private String endPoint;

    public UserStoreManagerServiceClient(String backEndUrl) throws RemoteException, UserStoreExceptionException {
        this.endPoint = backEndUrl + "/services/" + serviceName;
        remoteUserStoreManagerServiceStub = new RemoteUserStoreManagerServiceStub(endPoint);
    }

    public boolean isExistingUser(String username, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        boolean isExistingUser = remoteUserStoreManagerServiceStub.isExistingUser(username);
        return isExistingUser;
    }

    public void createNewUser(String username, String password, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);

        if(!userGroupsCreated){
            setupUserGroups();
            userGroupsCreated = true;
        }
        //by default a user is created as an active user
        remoteUserStoreManagerServiceStub.addUser(username,password,new String[]{activeUserGroup},null,null,false);
    }

    public void removeUser(String username, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        remoteUserStoreManagerServiceStub.deleteUser(username);
    }

    public boolean authenticateUser(String username, String password, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        boolean isAuthentic = remoteUserStoreManagerServiceStub.authenticate(username,password);
        return isAuthentic;
    }

    public void changeUserPassword(String username, String newPassword, String oldPassword, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        remoteUserStoreManagerServiceStub.updateCredential(username, newPassword, oldPassword);
    }

    public void activateUser(String username, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        String[] roleList = remoteUserStoreManagerServiceStub.getRoleListOfUser(username);
        for(int i=0;i<roleList.length;i++){
            if(roleList[i].equals(activeUserGroup)){
                return;
            }
        }
        remoteUserStoreManagerServiceStub.updateRoleListOfUser(username,null,new String[]{activeUserGroup});
    }

    public void deactivateUser(String username, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        String[] roleList = remoteUserStoreManagerServiceStub.getRoleListOfUser(username);
        for(int i=0;i<roleList.length;i++){
            if(roleList[i].equals(activeUserGroup)){
                remoteUserStoreManagerServiceStub.updateRoleListOfUser(username,new String[]{activeUserGroup}, null);
                return;
            }
        }
    }

    public boolean isActiveUser(String username, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        String[] roleList = remoteUserStoreManagerServiceStub.getRoleListOfUser(username);
        for(int i=0;i<roleList.length;i++){
            if(roleList[i].equals(activeUserGroup)){
                return true;
            }
        }
        return false;
    }

    private void authenticateStubFromCookie(String token){
        ServiceClient serviceClient;
        Options option;

        serviceClient = remoteUserStoreManagerServiceStub._getServiceClient();
        option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, token);
    }

    private void setupUserGroups() throws RemoteException, UserStoreExceptionException {
        if(!remoteUserStoreManagerServiceStub.isExistingRole(activeUserGroup)){
            remoteUserStoreManagerServiceStub.addRole(activeUserGroup,null,null);
        }
    }

}
