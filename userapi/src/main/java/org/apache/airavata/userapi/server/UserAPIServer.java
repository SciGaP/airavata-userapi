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

import org.apache.airavata.userapi.UserAPI;
import org.apache.airavata.userapi.common.utils.IServer;
import org.apache.airavata.userapi.error.UserAPIErrorType;
import org.apache.airavata.userapi.error.UserAPISystemException;
import org.apache.airavata.userapi.server.handler.UserAPIServerHandler;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;


public class UserAPIServer implements IServer{

    private final static Logger logger = LoggerFactory.getLogger(UserAPIServer.class);
    private static final String SERVER_NAME = "Airavata UserAPI Server";
    private static final String SERVER_VERSION = "1.0";

    private ServerStatus status;

    private TServer server;

    public UserAPIServer() {
        setStatus(ServerStatus.STOPPED);
    }

    public void startUserAPIServer(UserAPI.Processor<UserAPI.Iface> mockUserAPIServer) throws Exception {
        try {
            final int serverPort = 8939; //Integer.parseInt(ServerSettings.getSetting(Constants.API_SERVER_PORT,"8930"));
            final String serverHost = "localhost"; //ServerSettings.getSetting(Constants.API_SERVER_HOST, null);

            TServerTransport serverTransport;

            if(serverHost == null){
                serverTransport = new TServerSocket(serverPort);
            }else{
                InetSocketAddress inetSocketAddress = new InetSocketAddress(serverHost, serverPort);
                serverTransport = new TServerSocket(inetSocketAddress);
            }

            TThreadPoolServer.Args options = new TThreadPoolServer.Args(serverTransport);
            options.minWorkerThreads = 30; //Integer.parseInt(ServerSettings.getSetting(Constants.API_SERVER_MIN_THREADS, "30"));
            server = new TThreadPoolServer(options.processor(mockUserAPIServer));
            new Thread() {
                public void run() {
                    server.serve();
                    setStatus(ServerStatus.STOPPED);
                    logger.info("Airavata UserAPI Server Stopped.");
                }
            }.start();
            new Thread() {
                public void run() {
                    while(!server.isServing()){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    if (server.isServing()){
                        setStatus(ServerStatus.STARTED);
                        logger.info("Starting Airavata UserAPI Server on Port " + serverPort);
                        logger.info("Listening to Airavata UserAPI Clients ....");
                    }
                }
            }.start();
        } catch (TTransportException e) {
            logger.error(e.getMessage());
            setStatus(ServerStatus.FAILED);
            throw new UserAPISystemException(UserAPIErrorType.INTERNAL_ERROR);
        }
    }

    public static void main(String[] args) {
        try {
            UserAPIServer server = new UserAPIServer();
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start() throws Exception {
        setStatus(ServerStatus.STARTING);
        UserAPI.Processor<UserAPI.Iface> mockUserAPIServer =
                new UserAPI.Processor<UserAPI.Iface>(new UserAPIServerHandler("https://idp.scigap.org:7443"));
        startUserAPIServer(mockUserAPIServer);
    }

    @Override
    public void stop() throws Exception {
        if (server.isServing()){
            setStatus(ServerStatus.STOPING);
            server.stop();
        }

    }

    @Override
    public void restart() throws Exception {
        stop();
        start();
    }

    @Override
    public ServerStatus getStatus() throws Exception {
        return status;
    }

    private void setStatus(ServerStatus stat){
        status=stat;
        status.updateTime();
    }

    @Override
    public String getName() {
        return SERVER_NAME;
    }

    @Override
    public String getVersion() {
        return SERVER_VERSION;
    }

}