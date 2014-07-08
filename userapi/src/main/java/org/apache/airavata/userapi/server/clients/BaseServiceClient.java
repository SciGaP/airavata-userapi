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

import org.apache.airavata.userapi.common.utils.Constants;
import org.apache.airavata.userapi.common.utils.ServerProperties;
import org.apache.airavata.userapi.error.AuthorizationException;
import org.apache.airavata.userapi.server.utils.TokenEncryptionUtil;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;

import java.util.HashMap;

public class BaseServiceClient {
    protected String serviceName;
    protected Stub serviceStub;
    protected String endPoint;

    protected void authenticateStubFromToken(String token) throws AuthorizationException {
        ServiceClient serviceClient;
        Options options;
        TokenEncryptionUtil tokenEncryptionUtil = new TokenEncryptionUtil();

        serviceClient = serviceStub._getServiceClient();
        options = serviceClient.getOptions();

        HttpTransportProperties.Authenticator authenticator = new HttpTransportProperties.Authenticator();
        HashMap<String, String> credentials = tokenEncryptionUtil.decrypt(token);

        double issueTime = Long.parseLong(credentials.get(TokenEncryptionUtil.TIMESTAMP));
        ServerProperties properties = ServerProperties.getInstance();
        Long tokenLifeTime = Long.parseLong(properties.getProperty(Constants.TOKEN_LIFE_TIME,"999999999"));
        if(System.currentTimeMillis()-issueTime> tokenLifeTime){
            throw new AuthorizationException("Token Expired!");
        }
        authenticator.setUsername(credentials.get(TokenEncryptionUtil.USERNAME));
        authenticator.setPassword(credentials.get(TokenEncryptionUtil.PASSWORD));
        authenticator.setPreemptiveAuthentication(true);
        options.setProperty(HTTPConstants.AUTHENTICATE, authenticator);
        serviceClient.setOptions(options);
    }
}
