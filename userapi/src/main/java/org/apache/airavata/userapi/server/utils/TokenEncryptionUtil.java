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

import org.apache.airavata.userapi.common.utils.ServerProperties;
import org.apache.airavata.userapi.error.AuthorizationException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class TokenEncryptionUtil
{
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String TIMESTAMP = "TIMESTAMP";

    private static byte[] key;

    public TokenEncryptionUtil() {
        ServerProperties properties = ServerProperties.getInstance();
        String aesKey = properties.getProperty("AES_KEY", "qndAwER4h#ns(owe");

        char[] charKeys = aesKey.toCharArray();
        key = new byte[16];
        for(int i=0;i<16;i++){
            key[i] = (byte)charKeys[i];
        }
    }

    public String encrypt(String username, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            String strToEncrypt = username+"\n"+password+"\n"+System.currentTimeMillis();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
    }

    public HashMap<String, String> decrypt(String strToDecrypt) throws AuthorizationException {
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));

            HashMap<String,String> results = new HashMap<String, String>();
            results.put(USERNAME, decryptedString.split("\n")[0]);
            results.put(PASSWORD, decryptedString.split("\n")[1]);
            results.put(TIMESTAMP, decryptedString.split("\n")[2]);

            return results;
        }catch (Exception ex){
            throw new AuthorizationException();
        }

    }
}