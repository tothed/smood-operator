package am;

import am.hackathon.weather.Function1;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 */
public class Constant {

    public static final Function1<String> PROXY = new Function1<String>() {
        @Override
        public String exec() {
            return "http://gate:8080";
        }
    };
    static {
        SSLContext ssl = null;
        try {
            ssl = SSLContext.getInstance("SSL");

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
