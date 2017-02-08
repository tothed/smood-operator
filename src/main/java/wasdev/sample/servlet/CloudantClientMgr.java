package wasdev.sample.servlet;

import am.hackathon.weather.Function1;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.CouchDbException;
import com.cloudant.http.interceptors.BasicAuthInterceptor;

import java.net.URL;

public class CloudantClientMgr {


    private static String databaseName="smood";

    private static UserInfo getUserInfo() {
        String password;
        String user;
        user = "f03359af-cd23-42e4-85f7-f9409c713894-bluemix";
        password = "a4b51e0c3c68d23c8fa559c32b15f1d8aca2792e038378da0dc767f41b45da66";
        return new UserInfo(user, password);
    }

    public static Database getDB(Function1<String> proxy) {
        try {

            try {
                final UserInfo userInfo = getUserInfo();
                System.out.println("Connecting to Cloudant : " + userInfo.getUsername());
                ClientBuilder clientBuilder = ClientBuilder.account(userInfo.getUsername())
                        .interceptors(new BasicAuthInterceptor(userInfo.getUsername() + ":" + userInfo.getPassword()));
                if (proxy!=null)
                        clientBuilder = clientBuilder.proxyURL(new URL(proxy.exec()));
                return clientBuilder.build().database(databaseName, false);
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
