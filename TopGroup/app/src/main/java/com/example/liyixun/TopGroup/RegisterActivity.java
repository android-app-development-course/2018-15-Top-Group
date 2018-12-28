package com.example.liyixun.TopGroup;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity{

    private EditText r_account;
    private EditText r_password;
    private EditText r_password2;
    private EditText r_nickname;
    private RadioButton rbtn1;
    private RadioButton rbtn2;
    private Button r_login;
    private Integer gender;
    private Drawable right;
    private Drawable wrong;
    private Boolean bool_login;
    private Boolean bool_ac;
    private Boolean bool_pw;
    private Boolean bool_pw2;
    private Boolean bool_nn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        init();

        judge();

        click();
    }

    private void init() {
        Bmob.initialize(this,"03de14ff4bda451ee3108a1070c21129");
        bool_login = false;
        bool_ac = false;
        bool_pw = false;
        bool_pw2 = false;
        bool_nn = false;
        gender = 0;
        r_account = (EditText) findViewById(R.id.edt_rat);
        r_password = (EditText) findViewById(R.id.edt_rpw);
        r_password2 = (EditText) findViewById(R.id.edt_rpw2);
        r_nickname = (EditText) findViewById(R.id.edt_rnn);
        r_login = (Button) findViewById(R.id.r_login);
        rbtn1 = (RadioButton) findViewById(R.id.r_rb1);
        rbtn2 = (RadioButton) findViewById(R.id.r_rb2);

        right = getResources().getDrawable(R.drawable.ic_right);
        wrong = getResources().getDrawable(R.drawable.ic_wrong);
    }

    private void btn_judge(){
        if (bool_ac && bool_pw && bool_pw2 && bool_nn && !bool_login) {
            bool_login = true;
            r_login.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            r_login.setEnabled(true);
        } else if (bool_login && !( bool_ac && bool_pw && bool_pw2 && bool_nn )) {
            bool_login = false;
            r_login.setBackgroundColor(getResources().getColor(R.color.red0));
            r_login.setEnabled(false);
        }
    }

    private void judge(){
        //账户
        r_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String account = r_account.getText().toString();
                    if (!hasFocus) {
                        if (account.length() > 10 || account.length() == 0 ) {
                            r_account.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,wrong,null);
                            Toast.makeText(RegisterActivity.this,"长度不能为0或超过10",Toast.LENGTH_LONG).show();
                        }
                        else {
                            BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
                            categoryBmobQuery.addWhereEqualTo("username", account);
                            categoryBmobQuery.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> object, BmobException e) {
                                    if (e == null) {
                                        if (object.size()==0){
                                            r_account.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,right,null);
                                            bool_ac = true;
                                        }
                                        else {
                                            Toast.makeText(RegisterActivity.this,"账户名已存在",Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Log.e("BMOB", e.toString());
                                    }
                                }
                            });
                        }
                    btn_judge();
                    }
                }
            }
        });

        //密码
        r_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String password = r_password.getText().toString();
                    if (!hasFocus) {
                        if (password.length() > 12 || password.length() == 0 ) {
                            bool_pw = false;
                            r_password.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,wrong,null);
                            Toast.makeText(RegisterActivity.this,"长度不能为0或超过12",Toast.LENGTH_LONG).show();
                        } else {
                            bool_pw = true;
                            r_password.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,right,null);
                        }
                        btn_judge();
                    }
                }
            }
        });

        //再次确认密码
        r_password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String password = r_password2.getText().toString();
                    if (!hasFocus) {
                        if (!password.equals(r_password.getText().toString())) {
                            r_password2.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,wrong,null);
                            Toast.makeText(RegisterActivity.this,"密码输入不一致",Toast.LENGTH_LONG).show();
                            bool_pw2 = false;
                        } else if (password.length()!=0){
                            r_password2.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,right,null);
                            bool_pw2 = true;
                        }
                        btn_judge();
                    }
                }
            }
        });

        //昵称
        r_nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String nickname = r_nickname.getText().toString();
                        if (nickname.length() > 10 || nickname.length() == 0 ) {
                            r_nickname.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,wrong,null);
                            bool_nn = false;
                            btn_judge();
                            Toast.makeText(RegisterActivity.this,"长度不能为0或超过10",Toast.LENGTH_LONG).show();
                        }
                        else {
                            BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
                            categoryBmobQuery.addWhereEqualTo("nickname", nickname);
                            categoryBmobQuery.findObjects(new FindListener<User>() {
                                @Override
                                public void done(List<User> object, BmobException e) {
                                    if (e == null) {
                                        if (object.size()==0){
                                            r_nickname.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,right,null);
                                            bool_nn = true;
                                        }
                                        else {
                                            Toast.makeText(RegisterActivity.this,"该昵称已存在",Toast.LENGTH_LONG).show();
                                            r_nickname.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,wrong,null);
                                            bool_nn = false;
                                        }
                                    } else {
                                        Log.e("BMOB", e.toString());
                                        r_nickname.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,wrong,null);
                                        bool_nn = false;
                                    }
                                    btn_judge();
                                }
                            });
                        }
                }
            }
        });
    }

    private void click() {

        //性别男
        rbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 0;
            }
        });

        //性别女
        rbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = 1;
                /*String str;
                str = String.valueOf(bool_ac) + String.valueOf(bool_pw) +String.valueOf(bool_pw2) +String.valueOf(bool_nn) +String.valueOf(bool_login);
                Toast.makeText(RegisterActivity.this,str,Toast.LENGTH_LONG).show();*/
            }
        });

        //注册
        r_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String account = r_account.getText().toString();
                String password = r_password.getText().toString();
                String md5 = MD5Utils.getMD5(password);
                String nickname = r_nickname.getText().toString();

                final User user = new User();
                user.setUsername(account);
                user.setPassword(md5);
                user.setNickname(nickname);
                user.setGender(gender);
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

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("account",account);
                intent.putExtra("password",password);
                setResult(RESULT_OK,intent);
                finish();
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


}
