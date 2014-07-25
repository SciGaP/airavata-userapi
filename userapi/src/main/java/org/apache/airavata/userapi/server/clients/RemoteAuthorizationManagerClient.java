package org.apache.airavata.userapi.server.clients;

import org.apache.airavata.userapi.common.utils.Constants;
import org.apache.airavata.userapi.common.utils.ServerProperties;
import org.apache.airavata.userapi.error.AuthorizationException;
import org.apache.airavata.userapi.models.APIPermissions;
import org.apache.airavata.userapi.server.utils.TokenEncryptionUtil;
import org.wso2.carbon.um.ws.api.stub.RemoteAuthorizationManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceStub;
import org.wso2.carbon.um.ws.api.stub.UserStoreExceptionException;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

public class RemoteAuthorizationManagerClient extends BaseServiceClient{

    private static final String PERMISSION_ROOT = "permission/applications/";
    private static final String PERMISSION_ACTION = "ui.execute";

    private static final String AIRAVATA_API_PERMISSIONS_ROOT = "/permission/applications/airavata-api/";
    private static final String AIRAVATA_APP_CATALOG_PERMISSIONS_ROOT = "/permission/applications/airavata-app-catalog-api/";

    private  byte[] key;

    public RemoteAuthorizationManagerClient(String backEndUrl) throws RemoteException, UserStoreExceptionException {
        String serviceName = "RemoteAuthorizationManagerService";
        this.endPoint = backEndUrl + "/services/" + serviceName;
        this.serviceStub = new RemoteAuthorizationManagerServiceStub(endPoint);

        ServerProperties properties = ServerProperties.getInstance();
        String aesKey = properties.getProperty("AES_KEY", "qndAwER4h#ns(owe");

        char[] charKeys = aesKey.toCharArray();
        key = new byte[16];
        for(int i=0;i<16;i++){
            key[i] = (byte)charKeys[i];
        }
    }

    public boolean checkPermission(String username, String permissionString, String token) throws AuthorizationException, RemoteException, UserStoreExceptionException {
        authenticateStubFromToken(token);
        return ((RemoteAuthorizationManagerServiceStub) serviceStub).isUserAuthorized(username, PERMISSION_ROOT + permissionString, PERMISSION_ACTION);
    }

    public APIPermissions getUserPermissions(String username, String token) throws Exception {
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

        try {
            String temp = toString(new HashMap<String,Boolean>(apiPermissions.getAiravataAPIPermissions()));
            temp = temp + "\n" + toString(new HashMap<String,Boolean>(apiPermissions.getAiravataAppCatalogPermissions()));
            temp = temp + "\n" + System.currentTimeMillis();

            String signature = encrypt(temp);
            apiPermissions.setSignature(signature);
            return apiPermissions;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Cannot create signature");
        }
    }

    /** Write the object to a Base64 string. */
    private String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return new String( Base64.encodeBase64(baos.toByteArray()) );
    }


    private String encrypt(String strToEncrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        final String encryptedString = new String(Base64.encodeBase64(cipher.doFinal(strToEncrypt.getBytes())));
        return encryptedString;
    }

    private HashMap<String, Boolean> getAPIPermissionsBase(){
        HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        String[] airavataAPIPermission = ServerProperties.getInstance().getProperty(
                Constants.AIRAVATA_API_PERMISSIONS,"").split(",");
        for(int i=0;i<airavataAPIPermission.length;i++){
            hashMap.put(airavataAPIPermission[i].trim(), false);
        }
        return hashMap;
    }


    private HashMap<String, Boolean> getAppCatalogPermissionsBase(){
        HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
        String[] appCatalogPermissions = ServerProperties.getInstance().getProperty(
                Constants.APP_CATALOG_PERMISSIONS,"").split(",");
        for(int i=0;i<appCatalogPermissions.length;i++){
            hashMap.put(appCatalogPermissions[i].trim(), false);
        }
        return hashMap;
    }
}
