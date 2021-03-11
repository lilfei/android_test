package com.zzx.okhttptest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zzx.okhttptest.mvc.view.LoginActivity;
import com.zzx.okhttptest.net.SSLSocketTest;
import com.zzx.okhttptest.net.mSocketFactory;
import com.zzx.okhttptest.net.ssl.SSLSocketTest1;
import com.zzx.okhttptest.util.Logg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.zzx.okhttptest.Contant.HomeIndexPageUrl;
import static com.zzx.okhttptest.util.Constant.BASEURL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private String TAG = "react";
    private String url = "http://www.imooc.com";

    private String baseGsjnUrl = "https://rm.gsjn.gov.cn";

    private OkHttpClient client;

    private Button BtnDoGet;
    private Button BtnDoPost;
    private TextView TvResponse;

    private static Long curTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TvResponse = findViewById(R.id.tv_response);

        findViewById(R.id.btn_on_login).setOnClickListener(this);
        findViewById(R.id.btn_get_result).setOnClickListener(this);
        findViewById(R.id.btn_do_get).setOnClickListener(this);
        findViewById(R.id.btn_do_post).setOnClickListener(this);
        findViewById(R.id.btn_do_post_string).setOnClickListener(this);
        findViewById(R.id.btn_post_gsjn_index).setOnClickListener(this);

//        SSLSocketTest.trustAllHosts();

        /*========================================================================================*/

//        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            final Certificate ca;
//            // uwca.crt 打包在 asset 中，该证书可以从https://itconnect.uw.edu/security/securing-computer/install/safari-os-x/下载
//            InputStream caInput = new BufferedInputStream(getAssets().open("client.cer"));
//
//            ca = cf.generateCertificate(caInput);
//            Logg.d("ca=" + ((X509Certificate) ca).getSubjectDN());
//            Logg.d("key=" + ((X509Certificate) ca).getPublicKey());
//            Logg.d("key=" + Arrays.toString(((X509Certificate) ca).getSigAlgParams()));
//
//            caInput.close();
//
//            client = new OkHttpClient
//                    .Builder()
//                    .sslSocketFactory(SSLSocketTest.getSSLCertifcation(getApplicationContext()), new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                        }
//
//                        @Override
//                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                            for (X509Certificate cert : chain) {
//
//                                // Make sure that it hasn't expired.
//                                cert.checkValidity();
//
//                                // Verify the certificate's public key chain.
//                                try {
//                                    cert.verify(((X509Certificate) ca).getPublicKey());
//                                } catch (NoSuchAlgorithmException e) {
//                                    e.printStackTrace();
//                                } catch (InvalidKeyException e) {
//                                    e.printStackTrace();
//                                } catch (NoSuchProviderException e) {
//                                    e.printStackTrace();
//                                } catch (SignatureException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[0];
//                        }
//                    })
//                    .hostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    })
//                    .build();
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        /*========================================================================================*/
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, new TrustManager[]{new SSLSocketTest.TrustAllManager()}, new SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        client = new OkHttpClient
                .Builder()
                .hostnameVerifier(new SSLSocketTest.AllHostnameVerifier())
                .sslSocketFactory(sc.getSocketFactory(), new SSLSocketTest.TrustAllManager())
//                .sslSocketFactory(SSLSocketTest1.getSSLCertifcation(getApplicationContext()))
                .build();
        /*========================================================================================*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_on_login: {
                onLoginActivity();
            }
            break;
            case R.id.btn_get_result: {
                getSign();
            }
            break;
            case R.id.btn_do_get: {
                doGet();
            }
            break;
            case R.id.btn_do_post: {
                doPost();
            }
            break;
            case R.id.btn_do_post_string: {
                doPostString();
            }
            break;
            case R.id.btn_post_gsjn_index: {
                postGsjnIndex();
            }
            break;
        }
    }

    private void onLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void getSign() {
        curTime = System.currentTimeMillis();
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = requestBodyBuilder
                .add("appId", "7084c19d23954d7a8ce1a11fc436afb1")
                .add("appKey", "NzA4NGMxOWQyMzk1NGQ3YThjZTFhMTFmYzQzNmFmYjE6MTIzNDU2")
                .add("curTime", curTime.toString())
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(BASEURL + "getSign").post(requestBody).build();

        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logg.e("onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logg.e("onResponse: " + response.toString());
                final String sign = response.body().string();
            }
        });
    }

    public void postGsjnIndex() {
        HashMap<String, Object> postBodyHashMap = new HashMap<>();
        postBodyHashMap.put("pagesize_news", 10);
        postBodyHashMap.put("pagesize_video", 10);
        RequestBody requestBody = structurePostJsonBody(postBodyHashMap);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(HomeIndexPageUrl).post(requestBody).build();
        doRequest(request);
    }

    public void doPostString() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), "{userName:postString,passWord:psot123}");
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(BASEURL + "postString").post(requestBody).build();
        doRequest(request);
    }

    public RequestBody structurePostJsonBody(HashMap<String, Object> hashMap) {
        if (hashMap == null) {
            return RequestBody.create(MEDIA_TYPE, "");
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Logg.d("structurePostBody jsonObject: " + jsonObject.toString());
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, jsonObject.toString());
        return requestBody;
    }


    public void doPost() {
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = requestBodyBuilder
                .add("userName", "llf")
                .add("passWord", "llf-123")
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(BASEURL + "login").post(requestBody).build();

        doRequest(request);
    }

    public void doGet() {
        Request.Builder builder = new Request.Builder();
        final Request request = builder.url(BASEURL + "login?userName=haha&&passWord=12345").get().build();
        doRequest(request);
    }

    private void doRequest(Request request) {
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logg.e("onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logg.e("onResponse: " + response.toString());
                final String result = response.body().string();
                Logg.e("onResponse: " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TvResponse.setText(result);
                    }
                });
            }
        });
    }

}
