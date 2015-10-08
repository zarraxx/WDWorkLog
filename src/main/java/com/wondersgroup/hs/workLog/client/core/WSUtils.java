package com.wondersgroup.hs.workLog.client.core;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zarra on 15/3/8.
 */
public class WSUtils {
    //public static String APPBaseURL = "http://10.1.10.26:8999/log/";

    public static String APPBaseURL = "https://log.wondersgroup.com/log/";
    public static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.151 Safari/535.19";

    public static CloseableHttpClient createHttpClient() throws Exception{
        return HttpClients
                .custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(
                                SSLContexts.custom()
                                        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                                        .build()
                        )
                )
                .setUserAgent(USER_AGENT)
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setCookieSpec(CookieSpecs.DEFAULT)
                                .build()).build();
    }

    public static HttpClientContext createClientContext(Cookie cookie) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        if (cookie != null)
            cookieStore.addCookie(cookie);
        HttpClientContext context = new HttpClientContext();
        context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        return context;
    }


    public static String getMethod(String url, Cookie cookie) throws Exception {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            String totalURL = APPBaseURL + url;

            HttpGet httpget = new HttpGet(totalURL);

            HttpClientContext context = createClientContext(cookie);

            try (CloseableHttpResponse response = httpClient.execute(httpget, context)) {
                HttpEntity entity = response.getEntity();
                String responseText = EntityUtils.toString(entity);
                return responseText;
            }
        }
    }

    public static String postMethod(String url, Map<String, String> formData, Cookie cookie) throws Exception {
        try(CloseableHttpClient httpClient = createHttpClient()) {
            try (CloseableHttpResponse response = postMethodForClient(httpClient,url,formData,cookie)){
                HttpEntity entity = response.getEntity();
                String responseText = EntityUtils.toString(entity);
                return responseText;
            }
        }
    }


    public static CloseableHttpResponse postMethodForClient(CloseableHttpClient httpClient, String url, Map<String, String> formData, Cookie cookie) throws IOException {

        String totalURL = APPBaseURL + url;

        HttpPost httpPost = new HttpPost(totalURL);

        HttpClientContext context = createClientContext(cookie);

        if (formData != null) {

            List<NameValuePair> formParams = new ArrayList<>();
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "GBK");

            httpPost.setEntity(uefEntity);
        }

        CloseableHttpResponse response = httpClient.execute(httpPost, context);

        return response;

    }
}
