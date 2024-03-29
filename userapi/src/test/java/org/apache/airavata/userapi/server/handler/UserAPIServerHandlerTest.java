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
import org.apache.airavata.userapi.models.APIPermissions;
import org.apache.airavata.userapi.models.AuthenticationResponse;
import org.apache.airavata.userapi.models.UserProfile;
import org.apache.airavata.userapi.server.TestConstants;
import org.apache.airavata.userapi.server.TestProperties;

import java.util.List;

public class UserAPIServerHandlerTest extends TestCase {
    private UserAPIServerHandler userAPIServerHandler;
    private String token;
    private TestProperties properties;

    public void setUp() throws Exception {
        super.setUp();
        properties = TestProperties.getInstance();
        this.userAPIServerHandler = new UserAPIServerHandler(properties.getProperty(TestConstants.WSO2_IS_URL,""));
    }

    public void tearDown() throws Exception {

    }

    public void testGetAPIVersion() throws Exception {
        Assert.assertEquals("0.12.0",userAPIServerHandler.getAPIVersion());
    }

    public void testAdminLogin() throws Exception {
        AuthenticationResponse authenticationResponse = userAPIServerHandler.authenticateGateway(properties.getProperty(TestConstants.TEST_ADMIN_USERNAME,"")
                ,properties.getProperty(TestConstants.TEST_ADMIN_PASSWORD,""));
        Assert.assertNotNull(authenticationResponse);
        this.token = authenticationResponse.accessToken;
    }

    public void testCheckUsernameExists() throws Exception {
        testAdminLogin();
        boolean temp = userAPIServerHandler.checkUsernameExists("admin", token);
        Assert.assertTrue(temp);
    }

    public void testCreateNewUser() throws Exception {

        testAdminLogin();
        if(userAPIServerHandler.checkUsernameExists("testUser", token)){
            userAPIServerHandler.removeUser("testUser", token);
        }
        boolean exceptionThrown = false;

        UserProfile userProfile = new UserProfile();
        userProfile.firstName = "testuser";
        userProfile.lastName = "testuser";
        userProfile.emailAddress = "test_user@scigap.org";
        userProfile.organization = "scigap";
        userProfile.address = "address";
        userProfile.country = "dfadf";
        userProfile.telephone = "232323";
        userProfile.mobile = "23232342";
        userProfile.im = "ssdASsSD";
        userProfile.url = "eefer.com";

        try{
            userAPIServerHandler.createNewUser("testUser","abc123", userProfile, token);
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
        userProfile.emailAddress = "test_user@phprg.scigap.org";

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
            userAPIServerHandler.removeUser("testUser", token);
        }catch (Exception ex){
            exceptionThrown = true;
        }

        Assert.assertFalse(exceptionThrown);
    }

    public void testUpdateUserPassword() throws Exception {
        testAdminLogin();
        boolean exceptionThrown = false;
        try{
            userAPIServerHandler.updateUserPassword("test_user", "test_user", "test_user", token);
        }catch (Exception ex){
            exceptionThrown = true;
        }
        Assert.assertFalse(exceptionThrown);
    }

    public void testAuthenticateUser() throws Exception {
        testAdminLogin();
        APIPermissions apiPermissions = userAPIServerHandler.authenticateUser("test_user","test_user",token);
        Assert.assertNotNull(apiPermissions);
    }

    public void testGetRoleNames() throws Exception{
        testAdminLogin();
        List<String> result = userAPIServerHandler.getAllRoleNames(token);
        Assert.assertNotNull(result);
    }

    public void testCheckPermissionString() throws Exception{
        testAdminLogin();
        boolean temp = userAPIServerHandler.checkPermission("test_user","airavata-api/create_project", token);
    }

    public void testGetUserPermissions() throws  Exception{
        testAdminLogin();
        APIPermissions apiPermissions = userAPIServerHandler.getUserPermissions("test_user", token);
        Assert.assertNotNull(apiPermissions);
    }

}
