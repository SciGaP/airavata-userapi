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

package org.apache.airavata.userapi.common.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ServerProperties {

    private Properties properties = null;
    private static ServerProperties instance;

    public static final String SERVER_PROPERTY_FILE = "./conf/server.properties";
    public static final String DEFAULT_SERVER_PROPERTY_FILE = "conf/server.properties";

    private ServerProperties(){
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

    public static ServerProperties getInstance(){
        if(ServerProperties.instance == null){
            ServerProperties.instance = new ServerProperties();
        }

        return ServerProperties.instance;
    }

    public String getProperty(String key, String defaultVal){
        String val = this.properties.getProperty(key);

        if(key.equals(Constants.KEY_STORE_NAME)){
            if(val.isEmpty()){val = defaultVal;}
            if (new File("./security/"+val).exists()) {
                return "./security/"+val;
            } else {
                return ClassLoader.getSystemResource("security/"+val).getPath();
            }

        }

        if(val.isEmpty() || val == ""){
            return defaultVal;
        }else{
            return val;
        }
    }
}
