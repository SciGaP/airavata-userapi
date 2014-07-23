package org.apache.airavata.userapi.server.clients;

import org.apache.airavata.userapi.error.AuthorizationException;
import org.apache.airavata.userapi.models.APIPermissions;
import org.wso2.carbon.um.ws.api.stub.RemoteAuthorizationManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.UserStoreExceptionException;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;

public class RemoteAuthorizationManagerClient extends BaseServiceClient{

    private static final String PERMISSION_ROOT = "permission/applications/";
    private static final String PERMISSION_ACTION = "ui.execute";

    private static final String AIRAVATA_API_PERMISSIONS_ROOT = "/permission/applications/airavata-api/";
    private static final String AIRAVATA_APP_CATALOG_PERMISSIONS_ROOT = "/permission/applications/airavata-app-catalog-api/";

    private static final String[] AIRAVATA_API_PERMISSIONS = new String[]{"get_api_version", "create_project"};
    private static final String[] AIRAVATA_APP_CATALOG_PERMISSIONS = new String[]{"get_api_version"};

    public RemoteAuthorizationManagerClient(String backEndUrl) throws RemoteException, UserStoreExceptionException {
        String serviceName = "RemoteAuthorizationManagerService";
        this.endPoint = backEndUrl + "/services/" + serviceName;
        this.serviceStub = new RemoteAuthorizationManagerServiceStub(endPoint);
    }

    public boolean checkPermission(String username, String permissionString, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        return ((RemoteAuthorizationManagerServiceStub) serviceStub).isUserAuthorized(username, PERMISSION_ROOT + permissionString, PERMISSION_ACTION);
    }

    public APIPermissions getUserPermissions(String username, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        String[] airavataAPIPermissions = ((RemoteAuthorizationManagerServiceStub) serviceStub).getAllowedUIResourcesForUser(username,AIRAVATA_API_PERMISSIONS_ROOT);
        String[] appCatalogAPIPermissions = ((RemoteAuthorizationManagerServiceStub) serviceStub).getAllowedUIResourcesForUser(username,AIRAVATA_APP_CATALOG_PERMISSIONS_ROOT);

        APIPermissions apiPermissions = new APIPermissions();
        HashMap<String, Boolean> hashMap = getAPIPermissionsBase();
        if(airavataAPIPermissions != null){
            for(int i=0;i<airavataAPIPermissions.length;i++){
                hashMap.put(airavataAPIPermissions[i].replaceAll(
                        AIRAVATA_API_PERMISSIONS_ROOT,""), true);
            }
        }
        apiPermissions.setAiravataAPIPermissions(hashMap);

        hashMap = getAppCatalogPermissionsBase();
        if(appCatalogAPIPermissions != null) {
            for(int i=0;i<appCatalogAPIPermissions.length;i++){
                hashMap.put(appCatalogAPIPermissions[i].replaceAll(
                        AIRAVATA_APP_CATALOG_PERMISSIONS_ROOT,""), true);
            }
        }
        apiPermissions.setAiravataAppCatalogPermissions(hashMap);

        //@Todo
        //put the signature

        return apiPermissions;
    }

    private HashMap<String, Boolean> getAPIPermissionsBase(){
        HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        for(int i=0;i<AIRAVATA_API_PERMISSIONS.length;i++){
            hashMap.put(AIRAVATA_API_PERMISSIONS[i], false);
        }
        return hashMap;
    }


    private HashMap<String, Boolean> getAppCatalogPermissionsBase(){
        HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        for(int i=0;i<AIRAVATA_APP_CATALOG_PERMISSIONS.length;i++){
            hashMap.put(AIRAVATA_APP_CATALOG_PERMISSIONS[i], false);
        }
        return hashMap;
    }
}
