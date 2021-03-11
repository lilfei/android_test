package com.zzx.okhttptest.mvc.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zzx.okhttptest.R;
import com.zzx.okhttptest.mvc.controller.LoginController;
import com.zzx.okhttptest.mvc.controller.OnResStringListener;
import com.zzx.okhttptest.mvc.model.ResString;
import com.zzx.okhttptest.util.Logg;

public class LoginActivity extends Activity implements OnResStringListener, View.OnClickListener {

    EditText EtUserName;
    EditText EtPassWord;

    String userName;
    String passWord;

    LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindView();
        loginController = new LoginController();
    }

    private void bindView() {
        EtUserName = findViewById(R.id.et_login_username);
        EtPassWord = findViewById(R.id.et_login_password);
        findViewById(R.id.btn_login_activity).setOnClickListener(this);
    }

    private void onLogin() {
        userName = EtUserName.getText().toString();
        passWord = EtPassWord.getText().toString();

        loginController.Login(userName, passWord, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_activity: {
                onLogin();
            }
        }
    }

    @Override
    public void onSuccess(ResString result) {
        Logg.e(result.getResult() + " onSuccess --------------------------");
    }

    @Override
    public void onFailure() {
        Logg.e("onFailure --------------------------");
    }
}