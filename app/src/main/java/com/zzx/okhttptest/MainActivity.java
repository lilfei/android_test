package com.zzx.okhttptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.zzx.okhttptest.Contant.HomeIndexPageUrl;

public class MainActivity extends AppCompatActivity {

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private String TAG = "react";
    private String url = "http://www.imooc.com";

    private String baseUrl = "http://192.168.1.102:8080/StrutsTest1/";

    private String baseGsjnUrl = "https://rm.gsjn.gov.cn";


    private OkHttpClient client = new OkHttpClient();

    private Button BtnDoGet;
    private Button BtnDoPost;
    private TextView TvResponse;

    private static Long curTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_get_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAll();
            }
        });

        BtnDoGet = findViewById(R.id.btn_do_get);
        BtnDoGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGet();
            }
        });

        TvResponse = findViewById(R.id.tv_response);

        findViewById(R.id.btn_do_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPost();
            }
        });

        findViewById(R.id.btn_do_post_string).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPostString();
            }
        });

        findViewById(R.id.btn_post_gsjn_index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postGsjnIndex();
            }
        });
    }

    public void getAll() {
        curTime = System.currentTimeMillis();
        getSign();
    }

    public void getSign() {
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        RequestBody requestBody = requestBodyBuilder
                .add("appId", "7084c19d23954d7a8ce1a11fc436afb1")
                .add("appKey", "NzA4NGMxOWQyMzk1NGQ3YThjZTFhMTFmYzQzNmFmYjE6MTIzNDU2")
                .add("curTime", curTime.toString())
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(baseUrl + "getSign").post(requestBody).build();

        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.toString());
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
        Request request = builder.url(baseUrl + "postString").post(requestBody).build();
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
        Log.d(TAG, "structurePostBody jsonObject: " + jsonObject.toString());
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
        Request request = builder.url(baseUrl + "login").post(requestBody).build();

        doRequest(request);
    }

    public void doGet() {
        Request.Builder builder = new Request.Builder();
        final Request request = builder.url(baseUrl + "login?userName=haha&&passWord=12345").get().build();
        doRequest(request);
    }

    private void doRequest(Request request) {
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.toString());
                final String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);
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
