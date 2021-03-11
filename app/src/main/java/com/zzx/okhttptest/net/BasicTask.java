package com.zzx.okhttptest.net;

import com.zzx.okhttptest.util.Logg;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;

public class BasicTask {

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    public Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/x-www-form-urlencoded");
        return builder;
    }

    public String parseResponseInfo(Response response, BasicTask.SimpleRequestListener simpleRequestListener) {
        if (response == null) {
            return null;
        }
        Headers headers = response.headers();
        Logg.d("-------------------------------------------------------------");
        for (int i = 0; i < headers.size(); i++) {
            Logg.d("headers:" + headers.name(i) + " : " + headers.value(i));
        }
        Logg.d("-------------------------------------------------------------");
        String body = null;
        try {
            body = response.body().string();
        } catch (IOException e) {
            if (simpleRequestListener != null) {
                simpleRequestListener.onFailed(e.getMessage());
            }
            e.printStackTrace();
        }
        Logg.d("body:" + body);
        Logg.d("response.code():" + response.code());

        if (response.code() == 200) {
            return body;
        } else {
            if (simpleRequestListener != null) {
                simpleRequestListener.onFailed(response.code() + "");
            }
        }
        return null;
    }

    public interface SimpleRequestListener {
        public void onSuccess(String success);

        public void onFailed(String failed);
    }

}
