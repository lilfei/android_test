package com.llf.android_test.mvc.controller;

import com.llf.android_test.mvc.model.ResString;
import com.llf.android_test.mvc.model.ResStringModel;
import com.llf.android_test.net.OkHttpHelper;
import com.llf.android_test.util.Logg;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.llf.android_test.util.Constant.BASEURL;
import static com.llf.android_test.util.Constant.LOGINURL;
import static com.llf.android_test.util.Constant.POSTSTRING;

public class LoginController implements ResStringModel {
    @Override
    public void Login(String userName, String passWord, OnResStringListener onResStringListener) {
        HashMap<String, Object> postBodyHashMap = new HashMap<>();
        postBodyHashMap.put("userName", userName);
        postBodyHashMap.put("passWord", passWord);
//        NetRequestUtil.getInstance().initParameter(LOGINURL, postBodyHashMap).requestApi(new BasicTask.SimpleRequestListener() {
//            @Override
//            public void onSuccess(String success) {
//                ResString res = new ResString();
//                res.setResult(success);
//                onResStringListener.onSuccess(res);
//            }
//
//            @Override
//            public void onFailed(String failed) {
//                ResString res = new ResString();
//                res.setResult(failed);
//                onResStringListener.onSuccess(res);
//            }
//        });

        RequestBody requestBody = OkHttpHelper.structurePostJsonBody(postBodyHashMap);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(POSTSTRING).post(requestBody).build();
        OkHttpHelper.doRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onResStringListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Logg.e(response.toString());
                ResString res = new ResString();
                res.setResult(response.body().string());
                onResStringListener.onSuccess(res);
            }
        });
    }
}
