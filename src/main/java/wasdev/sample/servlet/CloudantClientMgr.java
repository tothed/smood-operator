package wasdev.sample.servlet;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.CouchDbException;
import com.cloudant.http.interceptors.BasicAuthInterceptor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.util.Map.Entry;
import java.util.Set;

public class CloudantClientMgr {



    private static UserInfo getUserInfo() {
        String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
        String serviceName = null;
        String password;
        String user;

        if (VCAP_SERVICES != null) {
            // When running in Bluemix, the VCAP_SERVICES env var will have the credentials for all bound/connected services
            // Parse the VCAP JSON structure looking for cloudant.
            JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES);
            Entry<String, JsonElement> dbEntry = null;
            Set<Entry<String, JsonElement>> entries = obj.entrySet();
            // Look for the VCAP key that holds the cloudant no sql db information
            for (Entry<String, JsonElement> eachEntry : entries) {
                if (eachEntry.getKey().toLowerCase().contains("cloudant")) {
                    dbEntry = eachEntry;
                    break;
                }
            }
            if (dbEntry == null) {
                throw new RuntimeException("Could not find cloudantNoSQLDB key in VCAP_SERVICES env variable");
            }

            obj = (JsonObject) ((JsonArray) dbEntry.getValue()).get(0);
            serviceName = dbEntry.getKey();
            System.out.println("Service Name - " + serviceName);

            obj = (JsonObject) obj.get("credentials");

            user = obj.get("username").getAsString();
            password = obj.get("password").getAsString();

        } else {
            //If VCAP_SERVICES env var doesn't exist: running locally.
            //Replace these values with your Cloudant credentials
            //https://b14ac298-f589-4ccc-8a34-3c7e5cad27e4-bluemix:b26ca57bc3a5fe2efc310c96934018231ed085dd0c959063bcdee3bbc026dfe8@b14ac298-f589-4ccc-8a34-3c7e5cad27e4-bluemix.cloudant.com
            user = "b14ac298-f589-4ccc-8a34-3c7e5cad27e4-bluemix";
            password = "b26ca57bc3a5fe2efc310c96934018231ed085dd0c959063bcdee3bbc026dfe8";
        }
        return new UserInfo(user,password);
    }

    public static Database getDB(String databaseName) {
        try {

            try {
                final UserInfo userInfo= getUserInfo();
                System.out.println("Connecting to Cloudant : " + userInfo.getUsername());
                CloudantClient client = ClientBuilder.account(userInfo.getUsername())
                        .interceptors(new BasicAuthInterceptor(userInfo.getUsername()+":"+userInfo.getPassword()))
                        .build();
                return client.database(databaseName, true);
            } catch (CouchDbException e) {
                throw new RuntimeException("Unable to connect to repository", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB Not found", e);
        }
    }


    public static Database getDB(String databaseName, String proxy) {
        try {

            try {
                final UserInfo userInfo= getUserInfo();
                System.out.println("Connecting to Cloudant : " + userInfo.getUsername());
                CloudantClient client = ClientBuilder.account(userInfo.getUsername())
                        .interceptors(new BasicAuthInterceptor(userInfo.getUsername()+":"+userInfo.getPassword()))
                        .proxyURL(new URL(proxy)).build();
                return client.database(databaseName, true);
            } catch (CouchDbException e) {
                throw new RuntimeException("Unable to connect to repository", e);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB Not found", e);
        }
    }

    private CloudantClientMgr() {
    }

}
