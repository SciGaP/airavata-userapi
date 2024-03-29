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

package org.apache.airavata.userapi.server;

import org.apache.airavata.userapi.common.utils.Constants;

import java.io.*;
import java.util.Properties;

public class TestProperties {

    private Properties properties = null;
    private static TestProperties instance;

    public static final String SERVER_PROPERTY_FILE = "./conf/test.properties";
    public static final String DEFAULT_SERVER_PROPERTY_FILE = "conf/test.properties";

    private TestProperties(){
        try {
            InputStream fileInput;
            if (new File(SERVER_PROPERTY_FILE).exists()) {
                fileInput = new FileInputStream(SERVER_PROPERTY_FILE);
            } else {
                // try to load default parser.properties from class loader.
                fileInput = ClassLoader.getSystemResource(DEFAULT_SERVER_PROPERTY_FILE).openStream();
            }
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            this.properties = properties;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TestProperties getInstance(){
        if(TestProperties.instance == null){
            TestProperties.instance = new TestProperties();
        }

        return TestProperties.instance;
    }

    public String getProperty(String key, String defaultVal){
        String val = this.properties.getProperty(key);

        if(val.isEmpty() || val == ""){
            return defaultVal;
        }else{
            return val;
        }
    }
}
