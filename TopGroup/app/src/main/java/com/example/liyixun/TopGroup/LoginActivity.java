package com.example.liyixun.TopGroup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity{

    private EditText edt_account;
    private EditText edt_password;
    private Button btn_login;
    private TextView tev_register;
    private Boolean bool_account;
    private Boolean bool_password;
    private Boolean bool_login;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        init();

        //judge();

        click();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK ) {
                    assert data != null;
                    String account = data.getStringExtra("account");
                    String password = data.getStringExtra("password");
                    edt_account.setText(account);
                    edt_password.setText(password);
                }
                break;
            default:
        }
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
                    Snackbar.make(view, "登录成功：" + user.getUsername(), Snackbar.LENGTH_LONG).show();

                    editor.putBoolean("rem",true);
                    editor.putString("account",edt_account.getText().toString());
                    editor.putString("password",edt_password.getText().toString());
                    editor.apply();

                    /*Intent intent = new Intent(LoginActivity.this,UserGroup.class);
                    intent.putExtra("userid",bmobUser.getObjectId());
                    startActivity(intent);*/
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    //intent.putExtra("userid",bmobUser.getObjectId());
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "登录失败：" + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN ) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定当前是否需要隐藏
     */
    protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void init(){
        Bmob.initialize(this,"03de14ff4bda451ee3108a1070c21129");
        edt_account = (EditText) findViewById(R.id.account);
        edt_password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tev_register = (TextView) findViewById(R.id.tev_register);
        bool_account = bool_password = false;
        bool_login = false;

        preferences = getPreferences(MODE_PRIVATE);
        editor = preferences.edit();
        Boolean rem = preferences.getBoolean("rem",false);
        if ( rem ) {
            String account = preferences.getString("account",null);
            String password = preferences.getString("password",null);
            edt_account.setText(account);
            edt_password.setText(password);
        }
    }

    private void btn_judge(){
        if ( bool_account && bool_password && !bool_login ) {
            btn_login.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            bool_login = true;
        }
        else if ( !(bool_account && bool_password) && bool_login ) {
            btn_login.setBackgroundColor(getResources().getColor(R.color.red0));
            bool_login = false;
        }
    }

    private void judge(){
        //账号
        edt_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String account = edt_account.getText().toString();
                    if (!hasFocus) {
                        if (account.length()!=0){
                            bool_account = true;
                        } else{
                            bool_account = false;
                        }
                    }
                    btn_judge();
                }
            }
        });

        //密码
        edt_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String password = edt_password.getText().toString();
                    if (!hasFocus) {
                        if (password.length()!=0){
                            bool_password = true;
                        } else {
                            bool_password = false;
                        }
                    }
                    btn_judge();
                }
            }
        });
    }

    private void click(){
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
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
}
