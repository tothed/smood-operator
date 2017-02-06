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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Set;

public class CloudantClientMgr {

    private static CloudantClient cloudant = null;
    private static Database db = null;


    private static String user = null;
    private static String password = null;

    private static void initClient() {
        if (cloudant == null) {
            synchronized (CloudantClientMgr.class) {
                if (cloudant != null) {
                    return;
                }
                cloudant = createClient();

            } // end synchronized
        }
    }

    private static CloudantClient createClient() {
        String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
        String serviceName = null;

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
            serviceName = (String) dbEntry.getKey();
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

        try {
            System.out.println("Connecting to Cloudant : " + user);
            CloudantClient client = ClientBuilder.account(user)
                    .interceptors(new BasicAuthInterceptor(user+":"+password))
                    .proxyURL(new URL("http://gate:8080"))
                    .build();
            return client;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unable to connect to repository", e);
        } catch (CouchDbException e) {
            throw new RuntimeException("Unable to connect to repository", e);
        }
    }

    public static Database getDB(String databaseName) {
        if (cloudant == null) {
            initClient();
        }
        try {
            return cloudant.database(databaseName, true);
        } catch (Exception e) {
            throw new RuntimeException("DB Not found", e);
        }
    }

    private CloudantClientMgr() {
    }

}
