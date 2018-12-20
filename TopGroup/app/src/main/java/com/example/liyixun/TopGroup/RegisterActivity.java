package com.example.liyixun.TopGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity{

    private EditText edt_account;
    private EditText edt_password;
    private Button btn_enter;
    private Button btn_skip;
    private String str1,str2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        Bmob.initialize(this,"03de14ff4bda451ee3108a1070c21129");
        edt_account = (EditText) findViewById(R.id.edt_account);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_enter = (Button) findViewById(R.id.btn_login);
        btn_skip = (Button) findViewById(R.id.btn_skip);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final User user = new User();
                user.setUsername(edt_account.getText().toString());
                str1 = edt_password.getText().toString();
                str2 = MD5Utils.getMD5(str1);
                user.setPassword(str2);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            Snackbar.make(v, "注册成功", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(v, "尚未失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                Intent intent = new Intent(RegisterActivity.this,Login.class);
                startActivity(intent);
            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
