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

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.airavata.userapi.models.UserProfile;

import java.util.List;

public class UserAPIServerHandlerTest extends TestCase {
    private UserAPIServerHandler userAPIServerHandler;
    private String token;

    public void setUp() throws Exception {
        super.setUp();
        this.userAPIServerHandler = new UserAPIServerHandler("https://idp.scigap.org:7443");
    }

    public void tearDown() throws Exception {

    }

    public void testGetAPIVersion() throws Exception {
        Assert.assertEquals("0.12.0",userAPIServerHandler.getAPIVersion());
    }

    public void testAdminLogin() throws Exception {
        String temp = userAPIServerHandler.adminLogin("admin@phprg.scigap.org","phprg9067@min");
        Assert.assertNotNull(temp);
        this.token = temp;
    }


    public void testAdminLogout() throws Exception {
        testAdminLogin();
        boolean exceptionThrown = false;
        try{
            userAPIServerHandler.adminLogout(token);
        }catch (Exception ex){
            exceptionThrown = true;
        }

        Assert.assertFalse(exceptionThrown);
    }

    public void testCheckUsernameExists() throws Exception {
        testAdminLogin();
        boolean temp = userAPIServerHandler.checkUsernameExists("admin", token);
        Assert.assertTrue(temp);
    }

    public void testCreateNewUser() throws Exception {
        testAdminLogin();
        boolean exceptionThrown = false;
        UserProfile userProfile = new UserProfile();
        userProfile.firstName = "test_user";
        userProfile.lastName = "test_user";
        userProfile.emailAddress = "test_user@scigap.org";
        userProfile.organization = "scigap";

        try{
            userAPIServerHandler.createNewUser("test_user","abc123", userProfile, token);
        }catch (Exception ex){
            exceptionThrown = true;
        }

        Assert.assertFalse(exceptionThrown);
    }

    public void testUpdateUserProfile() throws Exception{
        testAdminLogin();
        boolean exceptionThrown = false;
        UserProfile userProfile = new UserProfile();
        userProfile.firstName = "test_user";
        userProfile.lastName = "test_user";
        userProfile.emailAddress = "test_user@scigap.org";
        userProfile.organization = "scigap";

        try{
            userAPIServerHandler.updateUserProfile("test_user", userProfile, token);
        }catch (Exception ex){
            exceptionThrown = true;
        }

        Assert.assertFalse(exceptionThrown);
    }

    public void testGetUserProfile() throws Exception{
        testAdminLogin();
        UserProfile profile = userAPIServerHandler.getUserProfile("test_user", token);
        Assert.assertNotNull(profile);
    }

    public void testRemoveUser() throws Exception {
        testAdminLogin();
        boolean exceptionThrown = false;
        try{
            userAPIServerHandler.removeUser("test_user", token);
        }catch (Exception ex){
            exceptionThrown = true;
        }

        Assert.assertFalse(exceptionThrown);
    }

    public void testUpdateUserPassword() throws Exception {
        testAdminLogin();
        testCreateNewUser();
        boolean exceptionThrown = false;
        try{
            userAPIServerHandler.updateUserPassword("test_user", "abc456", "abc123", token);
            testRemoveUser();
        }catch (Exception ex){
            exceptionThrown = true;
        }
        Assert.assertFalse(exceptionThrown);
    }

    public void testAuthenticateUser() throws Exception {
        testAdminLogin();
        boolean isAuthentic = userAPIServerHandler.authenticateUser("scigap_admin","sci9067@min",token);
        Assert.assertTrue(isAuthentic);
    }

    public void testGetRoleNames() throws Exception{
        testAdminLogin();
        List<String> result = userAPIServerHandler.getRoleNames(token);
        Assert.assertNotNull(result);
    }

    public void testRoles() throws Exception{
        testAdminLogin();
        userAPIServerHandler.addRole("New_Role", token);
        List<String> result = userAPIServerHandler.getRoleNames(token);
        boolean contains = false;
        for(int i=0;i<result.size();i++){
            if(result.get(i).equals("New_Role")){
                contains = true;
                break;
            }
        }
        Assert.assertTrue(contains);

        userAPIServerHandler.addUserToRole("admin","New_Role", token);
        result = userAPIServerHandler.getRoleListOfUser("admin", token);
        contains = false;
        for(int i=0;i<result.size();i++){
            if(result.get(i).equals("New_Role")){
                contains = true;
                break;
            }
        }
        Assert.assertTrue(contains);

        userAPIServerHandler.removeUserFromRole("admin", "New_Role", token);
        result = userAPIServerHandler.getRoleListOfUser("admin", token);
        contains = false;
        for(int i=0;i<result.size();i++){
            if(result.get(i).equals("New_Role")){
                contains = true;
                break;
            }
        }
        Assert.assertTrue(!contains);


        userAPIServerHandler.removeRole("New_Role", token);
        result = userAPIServerHandler.getRoleNames(token);
        contains = false;
        for(int i=0;i<result.size();i++){
            if(result.get(i).equals("New_Role")){
                contains = true;
                break;
            }
        }

        Assert.assertTrue(!contains);
    }
}
