package com.example.liyixun.TopGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends AppCompatActivity{

    private EditText edt_account;
    private EditText edt_password;
    private Button btn_login;
    private TextView tev_register;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        init();
        Bmob.initialize(this,"03de14ff4bda451ee3108a1070c21129");
        edt_account = (EditText) findViewById(R.id.account);
        edt_password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tev_register = (TextView) findViewById(R.id.tev_register);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = edt_account.getText().toString();
                String password = edt_password.getText().toString();
                String md5 = MD5Utils.getMD5(password);
                login(v,account,md5);
            }
        });

        tev_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void init(){

    }

    private void login(final View view, String str1, String str2) {
        final User user = new User();
        //此处替换为你的用户名
        user.setUsername(str1);
        //此处替换为你的密码
        user.setPassword(str2);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                if (e == null) {
                    User user = BmobUser.getCurrentUser(User.class);
                    Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this,UserGroup.class);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
