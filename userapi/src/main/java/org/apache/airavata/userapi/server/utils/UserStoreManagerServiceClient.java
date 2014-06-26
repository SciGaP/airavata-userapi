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

import org.apache.airavata.userapi.models.UserProfile;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.carbon.um.ws.api.stub.ClaimValue;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.UserStoreExceptionException;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public class UserStoreManagerServiceClient {

    private final String serviceName = "RemoteUserStoreManagerService";
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

    public void createNewUser(String username, String password, UserProfile userProfile, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        remoteUserStoreManagerServiceStub.addUser(username,password,null,null,null,false);
    }

    public void updateUserProfile(String username, UserProfile userProfile, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        ClaimValue[] claims = new ClaimValue[4];

        claims[0] = new ClaimValue();
        claims[0].setClaimURI(UserProfileClaimUris.FIRST_NAME);
        claims[0].setValue(userProfile.firstName);

        claims[1] = new ClaimValue();
        claims[1].setClaimURI(UserProfileClaimUris.LAST_NAME);
        claims[1].setValue(userProfile.lastName);

        claims[2] = new ClaimValue();
        claims[2].setClaimURI(UserProfileClaimUris.EMAIL_ADDRESS);
        claims[2].setValue(userProfile.emailAddress);

        claims[3] = new ClaimValue();
        claims[3].setClaimURI(UserProfileClaimUris.ORGANIZATION);
        claims[3].setValue(userProfile.organization);

        remoteUserStoreManagerServiceStub.setUserClaimValues(username,claims, null);
    }

    public UserProfile getUserProfile(String username, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        String[] claims = new String[]{
                UserProfileClaimUris.FIRST_NAME,
                UserProfileClaimUris.LAST_NAME,
                UserProfileClaimUris.EMAIL_ADDRESS,
                UserProfileClaimUris.ORGANIZATION
        };
        ClaimValue[] claimValues = remoteUserStoreManagerServiceStub.getUserClaimValuesForClaims(username, claims, null);
        UserProfile userProfile = new UserProfile();

        for(int i=0;i<claimValues.length;i++){

            if(claimValues[i].getClaimURI().equals(UserProfileClaimUris.FIRST_NAME)){
                userProfile.firstName = claimValues[i].getValue();
            }else if(claimValues[i].getClaimURI().equals(UserProfileClaimUris.LAST_NAME)){
                userProfile.lastName = claimValues[i].getValue();
            }else if(claimValues[i].getClaimURI().equals(UserProfileClaimUris.EMAIL_ADDRESS)){
                userProfile.emailAddress = claimValues[i].getValue();
            }else if(claimValues[i].getClaimURI().equals(UserProfileClaimUris.ORGANIZATION)){
                userProfile.organization = claimValues[i].getValue();
            }
        }

        return userProfile;
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

    public void addUserToRole(String username, String roleName, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        remoteUserStoreManagerServiceStub.updateRoleListOfUser(username,null,new String[]{roleName});
    }

    public void removeUserFromRole(String username, String roleName, String token) throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        remoteUserStoreManagerServiceStub.updateRoleListOfUser(username,new String[]{roleName}, null);
    }

    public List<String> getUserListOfRole(String roleName, String token)throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        return Arrays.asList(remoteUserStoreManagerServiceStub.getUserListOfRole(roleName));
    }

    public List<String> getRoleListOfUser(String username, String token)throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        return Arrays.asList(remoteUserStoreManagerServiceStub.getRoleListOfUser(username));
    }

    public void addRole(String roleName, String token)throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        remoteUserStoreManagerServiceStub.addRole(roleName,null,null);
    }

    public void removeRole(String roleName, String token)throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        remoteUserStoreManagerServiceStub.deleteRole(roleName);
    }

    public List<String> getRoleNames(String token)throws RemoteException, UserStoreExceptionException {
        authenticateStubFromCookie(token);
        return Arrays.asList(remoteUserStoreManagerServiceStub.getRoleNames());
    }

    private void authenticateStubFromCookie(String token){
        ServiceClient serviceClient;
        Options option;

        serviceClient = remoteUserStoreManagerServiceStub._getServiceClient();
        option = serviceClient.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, token);
    }
}
