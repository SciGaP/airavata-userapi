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
import org.apache.airavata.userapi.server.utils.TokenEncryptionUtil;
import org.apache.axis2.AxisFault;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class LoginAdminServiceClient extends BaseServiceClient{

    public LoginAdminServiceClient(String backEndUrl) throws AxisFault {
        this.serviceName = "AuthenticationAdmin";
        this.endPoint = backEndUrl + "/services/" + serviceName;
        this.serviceStub = new AuthenticationAdminStub(endPoint);
    }

    public String authenticate(String userName, String password) throws Exception{
        String encryptedToken = null;

        TokenEncryptionUtil tokenEncryptionUtil = new TokenEncryptionUtil();

        if (((AuthenticationAdminStub)serviceStub).login(userName, password, "localhost")) {

            try {
                encryptedToken = tokenEncryptionUtil.encrypt(userName, password);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
                throw new Exception("Unable to authenticate Admin");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new Exception("Unable to authenticate Admin");
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                throw new Exception("Unable to authenticate Admin");
            } catch (BadPaddingException e) {
                e.printStackTrace();
                throw new Exception("Unable to authenticate Admin");
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
                throw new Exception("Unable to authenticate Admin");
            }
        }
        return encryptedToken;
    }

    public void logOut(String token) throws AuthorizationException, RemoteException, LogoutAuthenticationExceptionException {
        authenticateStubFromToken(token);
        ((AuthenticationAdminStub)serviceStub).logout();
    }
}
