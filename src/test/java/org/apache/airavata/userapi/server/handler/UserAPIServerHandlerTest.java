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

public class UserAPIServerHandlerTest extends TestCase {
    private UserAPIServerHandler userAPIServerHandler;

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
        String temp = userAPIServerHandler.adminLogin("scigap_admin","sci9067@min");
        Assert.assertNotNull(temp);
    }

    public void testAdminLogout() throws Exception {

    }

    public void testCheckUsernameExists() throws Exception {

    }

    public void testCreateNewUser() throws Exception {

    }

    public void testRemoveUser() throws Exception {

    }

    public void testUpdateUserPassword() throws Exception {

    }

    public void testActivateUser() throws Exception {

    }

    public void testDeactivateUser() throws Exception {

    }

    public void testAuthenticateUser() throws Exception {

    }
}
