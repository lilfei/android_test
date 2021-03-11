package com.zzx.okhttptest.net;

import android.text.TextUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetRequestUtil extends BasicTask {

    private static NetRequestUtil instance = null;
    private Disposable disposable = null;

    private HashMap<String, Object> postBodyHashMap = null;
    private String url = null;

    private NetRequestUtil() {
    }

    public static NetRequestUtil getInstance() {
        instance = new NetRequestUtil();
        return instance;
    }

    public NetRequestUtil initParameter(String url, HashMap<String, Object> hashMap) {
        this.url = url;
        this.postBodyHashMap = hashMap;
        return this;
    }

    public void cancleRequst() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public void requestApi(final SimpleRequestListener simpleRequestListener) {
        if (TextUtils.isEmpty(url)) {
            if (simpleRequestListener != null) {
                simpleRequestListener.onFailed("请求Url为空");
            }
            return;
        }
        disposable = Observable.timer(0, TimeUnit.SECONDS).map(new Function<Long, Response>() {
            @Override
            public Response apply(Long aLong) throws Exception {
                RequestBody requestBody = OkHttpHelper.structurePostJsonBody(postBodyHashMap);
                Response response = OkHttpHelper.executePostRequest(url, requestBody, simpleRequestListener);
                return response;
            }
        }).map(new Function<Response, String>() {
            @Override
            public String apply(Response response) throws Exception {
                String res = parseResponseInfo(response, simpleRequestListener);
                if (!TextUtils.isEmpty(res)) {
                    return res;
                }
                return "null";
            }
        }).subscribeOn(Schedulers.single()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (simpleRequestListener != null) {
                    if (!TextUtils.isEmpty(s)) {
                        JSONObject jsonObject = new JSONObject(s);
                        if (TextUtils.equals(jsonObject.optString("result"), "success")) {
                            simpleRequestListener.onSuccess(s);
                        } else {
                            simpleRequestListener.onFailed("接口请求成功，但状态是：" + jsonObject.optString("result"));
                        }
                    } else {
                        simpleRequestListener.onFailed("返回结果为null");
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (simpleRequestListener != null) {
                    simpleRequestListener.onFailed(throwable.getMessage());
                }
            }
        });
    }

}
