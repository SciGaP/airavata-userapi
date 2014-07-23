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

package org.apache.airavata.userapi.server.clients;

import org.apache.airavata.userapi.error.AuthorizationException;
import org.apache.airavata.userapi.models.UserProfile;
import org.apache.airavata.userapi.server.utils.UserProfileClaimUris;
import org.wso2.carbon.um.ws.api.stub.ClaimValue;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.UserStoreExceptionException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserStoreManagerServiceClient extends BaseServiceClient {

    private static final String ADMIN_ROLE = "admin";
    private static final String INTERNAL_ROLE_PREFIX = "Internal";

    public UserStoreManagerServiceClient(String backEndUrl) throws RemoteException, UserStoreExceptionException {
        String serviceName = "RemoteUserStoreManagerService";
        this.endPoint = backEndUrl + "/services/" + serviceName;
        this.serviceStub = new RemoteUserStoreManagerServiceStub(endPoint);
    }

    public boolean isExistingUser(String username, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        boolean isExistingUser = ((RemoteUserStoreManagerServiceStub) serviceStub).isExistingUser(username);
        return isExistingUser;
    }

    public void createNewUser(String username, String password, UserProfile userProfile, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ArrayList<ClaimValue> claimValueList = new ArrayList<ClaimValue>();
        ClaimValue temp;
        int nonEmptyValCount = 0;
        if (userProfile.firstName != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.FIRST_NAME);
            temp.setValue(userProfile.firstName);
            claimValueList.add(temp);
        }

        if (userProfile.lastName != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.LAST_NAME);
            temp.setValue(userProfile.lastName);
            claimValueList.add(temp);
        }

        if (userProfile.emailAddress != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.EMAIL_ADDRESS);
            temp.setValue(userProfile.emailAddress);
            claimValueList.add(temp);
        }


        if (userProfile.organization != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.ORGANIZATION);
            temp.setValue(userProfile.organization);
            claimValueList.add(temp);
        }


        if (userProfile.address != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.ADDRESS);
            temp.setValue(userProfile.address);
            claimValueList.add(temp);
        }

        if (userProfile.country != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.COUNTRY);
            temp.setValue(userProfile.country);
            claimValueList.add(temp);
        }

        if (userProfile.telephone != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.TELEPHONE);
            temp.setValue(userProfile.telephone);
            claimValueList.add(temp);
        }

        if (userProfile.mobile != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.MOBILE);
            temp.setValue(userProfile.mobile);
            claimValueList.add(temp);
        }

        if (userProfile.im != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.IM);
            temp.setValue(userProfile.im);
            claimValueList.add(temp);
        }

        if (userProfile.url != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.URL);
            temp.setValue(userProfile.url);
            claimValueList.add(temp);
        }
        ((RemoteUserStoreManagerServiceStub) serviceStub).addUser(username, password, null, claimValueList.toArray(new ClaimValue[nonEmptyValCount]), null, false);
    }

    public void updateUserProfile(String username, UserProfile userProfile, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ArrayList<ClaimValue> claimValueList = new ArrayList<ClaimValue>();
        ClaimValue temp;
        int nonEmptyValCount = 0;
        if (userProfile.firstName != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.FIRST_NAME);
            temp.setValue(userProfile.firstName);
            claimValueList.add(temp);
        }

        if (userProfile.lastName != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.LAST_NAME);
            temp.setValue(userProfile.lastName);
            claimValueList.add(temp);
        }

        if (userProfile.emailAddress != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.EMAIL_ADDRESS);
            temp.setValue(userProfile.emailAddress);
            claimValueList.add(temp);
        }


        if (userProfile.organization != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.ORGANIZATION);
            temp.setValue(userProfile.organization);
            claimValueList.add(temp);
        }


        if (userProfile.address != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.ADDRESS);
            temp.setValue(userProfile.address);
            claimValueList.add(temp);
        }

        if (userProfile.country != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.COUNTRY);
            temp.setValue(userProfile.country);
            claimValueList.add(temp);
        }

        if (userProfile.telephone != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.TELEPHONE);
            temp.setValue(userProfile.telephone);
            claimValueList.add(temp);
        }

        if (userProfile.mobile != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.MOBILE);
            temp.setValue(userProfile.mobile);
            claimValueList.add(temp);
        }

        if (userProfile.im != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.IM);
            temp.setValue(userProfile.im);
            claimValueList.add(temp);
        }

        if (userProfile.url != null) {
            nonEmptyValCount++;
            temp = new ClaimValue();
            temp.setClaimURI(UserProfileClaimUris.URL);
            temp.setValue(userProfile.url);
            claimValueList.add(temp);
        }

        ((RemoteUserStoreManagerServiceStub) serviceStub).setUserClaimValues(username, claimValueList.toArray(new ClaimValue[nonEmptyValCount]), null);
    }

    public UserProfile getUserProfile(String username, String token) throws RemoteException, UserStoreExceptionException, AuthorizationException {
        authenticateStubFromToken(token);
        String[] claims = new String[]{
                UserProfileClaimUris.FIRST_NAME,
                UserProfileClaimUris.LAST_NAME,
                UserProfileClaimUris.EMAIL_ADDRESS,
                UserProfileClaimUris.ORGANIZATION,
                UserProfileClaimUris.ADDRESS,
                UserProfileClaimUris.COUNTRY,
                UserProfileClaimUris.TELEPHONE,
                UserProfileClaimUris.MOBILE,
                UserProfileClaimUris.IM,
                UserProfileClaimUris.URL
        };
        ClaimValue[] claimValues = ((RemoteUserStoreManagerServiceStub) serviceStub).getUserClaimValuesForClaims(username, claims, null);
        UserProfile userProfile = new UserProfile();

        for (int i = 0; i < claimValues.length; i++) {
            if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.FIRST_NAME)) {
                userProfile.firstName = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.LAST_NAME)) {
                userProfile.lastName = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.EMAIL_ADDRESS)) {
                userProfile.emailAddress = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.ORGANIZATION)) {
                userProfile.organization = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.ADDRESS)) {
                userProfile.address = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.COUNTRY)) {
                userProfile.country = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.TELEPHONE)) {
                userProfile.telephone = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.MOBILE)) {
                userProfile.mobile = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.IM)) {
                userProfile.im = claimValues[i].getValue();
            } else if (claimValues[i].getClaimURI().equals(UserProfileClaimUris.URL)) {
                userProfile.url = claimValues[i].getValue();
            }
        }
        return userProfile;
    }

    public void removeUser(String username, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ((RemoteUserStoreManagerServiceStub) serviceStub).deleteUser(username);
    }

    public boolean authenticateUser(String username, String password, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        boolean isAuthentic = ((RemoteUserStoreManagerServiceStub) serviceStub).authenticate(username, password);
        return isAuthentic;
    }

    public void changeUserPassword(String username, String newPassword, String oldPassword, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ((RemoteUserStoreManagerServiceStub) serviceStub).updateCredential(username, newPassword, oldPassword);
    }

    public void addUserToRole(String username, String roleName, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ((RemoteUserStoreManagerServiceStub) serviceStub).updateRoleListOfUser(username, null, new String[]{roleName});
    }

    public void removeUserFromRole(String username, String roleName, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ((RemoteUserStoreManagerServiceStub) serviceStub).updateRoleListOfUser(username, new String[]{roleName}, null);
    }

    public List<String> getUserListOfRole(String roleName, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        return Arrays.asList(((RemoteUserStoreManagerServiceStub) serviceStub).getUserListOfRole(roleName));
    }

    public List<String> getRoleListOfUser(String username, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        return Arrays.asList(((RemoteUserStoreManagerServiceStub) serviceStub).getRoleListOfUser(username));
    }

    public void addRole(String roleName, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ((RemoteUserStoreManagerServiceStub) serviceStub).addRole(roleName, null, null);
    }

    public void removeRole(String roleName, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        ((RemoteUserStoreManagerServiceStub) serviceStub).deleteRole(roleName);
    }

    public List<String> getRoleNames(String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        List<String> temp = Arrays.asList(((RemoteUserStoreManagerServiceStub) serviceStub).getRoleNames());
        List<String> result = new ArrayList<String>();
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).equals(ADMIN_ROLE) || temp.get(i).startsWith(INTERNAL_ROLE_PREFIX)){
                continue;
            }
            result.add(temp.get(i));
        }

        return result;
    }
}
