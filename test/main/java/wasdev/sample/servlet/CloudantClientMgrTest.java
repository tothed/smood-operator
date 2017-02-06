package wasdev.sample.servlet;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.views.AllDocsResponse;
import com.cloudant.http.Http;
import com.cloudant.http.HttpConnection;
import org.junit.Test;
import sun.net.www.http.HttpClient;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class CloudantClientMgrTest {
    @Test
    public void test() throws Exception {
//        System.setProperty("http.proxyHost", "http://gate-zrh.swissre.com");
//        System.setProperty("http.proxyPort", "8080");
//        System.setProperty("https.proxyHost", "https://gate-zrh.swissre.com");
//        System.setProperty("https.proxyPort", "9443");
        SSLContext ssl = SSLContext.getInstance("SSL");
        ssl.init(null, new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }},new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
        Database client = CloudantClientMgr.getDB("sample_nosql_db");
        client.remove(client.find(HashMap.class,"1234"));
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("_id","1234");
        hashMap.put("name","tada!!!!");
        client.save(hashMap);
        client.ensureFullCommit();
        AllDocsResponse resp = client.getAllDocsRequestBuilder().includeDocs(true).build().getResponse();
//

        for (Map map : resp.getDocsAs(Map.class)){
            System.out.println(map);
        }
    }
}