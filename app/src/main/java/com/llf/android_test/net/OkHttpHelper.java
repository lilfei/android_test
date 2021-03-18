package com.llf.android_test.net;

import android.content.Context;
import android.text.TextUtils;

import com.llf.android_test.util.Logg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.reactivex.annotations.Nullable;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

import static com.llf.android_test.net.SSLSocketTest.getSSLCertifcation;

public class OkHttpHelper {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType URLENCODED = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static OkHttpClient mOkHttpClient;

    public static void initOkHttpClient(Context context) {
        File httpCacheDirectory = new File("/sdcard", "cache_xx");
        Cache cache = new Cache(httpCacheDirectory, 10240 * 1024 * 10); //10M
        OkHttpClient okHttpClient = new OkHttpClient();

        mOkHttpClient = okHttpClient
                .newBuilder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {

                        Logg.d("[initOkHttpClient]================进入拦截器");

                        long t1 = System.nanoTime();
                        Request request = chain.request();

                        Logg.d("[initOkHttpClient] 新请求 =request==" + request.toString());
                        Logg.d(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

                        Response response = chain.proceed(request); //===========
                        long t2 = System.nanoTime();
                        Response response_build;
                        int maxStale = 60 * 60 * 24 * 28; // // 无网络缓存保存四周
                        response_build = response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .build();

                        Logg.d(String.format("Received response %s in %.1fms%n%s", request.url(), (t2 - t1) / 1e6d, request.headers()));

                        return response_build;
                    }
                })
                .authenticator(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(null, null);
                        return response.request().newBuilder().addHeader("Authorization", credential).build();
                    }
                })
                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketTest.getSSLCertifcation(context))
//                .sslSocketFactory(SSLSocketTest1.getSSLCertifcation(context))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
//        mOkHttpClient = okHttpClient;
    }

    /**
     * @param url
     * @param requestBody
     * @param callback
     */
    public static void syncPostRequest(String url, RequestBody requestBody, Callback callback) {
        OkHttpClient client = mOkHttpClient;
        if (client == null) {
            try {
                throw new InstantiationException("未初始化mOkHttpClient");
            } catch (InstantiationException e) {
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.cookieJar().loadForRequest(request.url());
        client.newCall(request).enqueue(callback);
    }

    public static Response executePostRequest(String url, RequestBody requestBody, BasicTask.SimpleRequestListener simpleRequestListener) {
        OkHttpClient client = mOkHttpClient;
        if (client == null) {
            try {
                if (simpleRequestListener != null) {
                    simpleRequestListener.onFailed("未初始化mOkHttpClient");
                }
                throw new InstantiationException("未初始化mOkHttpClient");
            } catch (InstantiationException e) {
            }
        }

        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.cookieJar().loadForRequest(request.url());
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            if (simpleRequestListener != null) {
                simpleRequestListener.onFailed(e.getMessage());
            }
            e.printStackTrace();
        }
        return response;
    }

    public static RequestBody structurePostJsonBody(HashMap<String, Object> hashMap) {
        if (hashMap == null) {
            return RequestBody.create(JSON, "");
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Logg.d("structurePostBody:" + "jsonObject:" + jsonObject.toString());
        RequestBody requestBody = RequestBody.create(JSON, jsonObject.toString());
        return requestBody;
    }

    public static RequestBody structurePostFormBody(HashMap<String, Object> hashMap) {
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            formBody.add(entry.getKey(), (String) entry.getValue());
        }
        return formBody.build();
    }

    public static void doRequest(Request request, Callback callback) {
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

}
