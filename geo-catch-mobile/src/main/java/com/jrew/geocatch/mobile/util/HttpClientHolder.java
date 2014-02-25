package com.jrew.geocatch.mobile.util;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/24/14
 * Time: 3:32 PM
 */
public class HttpClientHolder {

    /** **/
    private final static String HTTP_PROTOCOL = "http";

    /** **/
    private final static String HTTPS_PROTOCOL = "https";

    /** **/
    private static final int CONNECTION_TIMEOUT = 7000;

    /** **/
    private static HttpClient httpClient = null;

    /**
     *
     * @return
     */
    public static HttpClient getHttpClient() {

        if (httpClient == null) {

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme(HTTP_PROTOCOL, PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme(HTTPS_PROTOCOL, SSLSocketFactory.getSocketFactory(), 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

           httpClient = new DefaultHttpClient(ccm, params);
        }

        return httpClient;
    }

    /**
     *
     */
    public static void release() {
        if (httpClient != null) {
//            try {
//               // ((DefaultHttpClient) httpClient).clo;
//            } catch(IOException exc) {}
//            httpClient = null;
        }
    }
}
