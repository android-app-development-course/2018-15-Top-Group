package com.example.liyixun.TopGroup.personpage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liyixun.TopGroup.Calendar.BoomSQL;
import com.example.liyixun.TopGroup.Group;
import com.example.liyixun.TopGroup.R;
import com.example.liyixun.TopGroup.RegisterActivity;
import com.example.liyixun.TopGroup.User;
import com.example.liyixun.TopGroup.UserGroup;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class mDialog extends Dialog{
    private String title;
    private EditText edt_text;
    private TextView tv_title;
    private Button btnOK;
    private Button btnCancel;
    private String storeid;
    private User user;
    private Context context;
    public mDialog(Context context, String title) {
        super(context);
        this.context = context;
        this.title = title;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addgroup_layout);
        Bmob.initialize(this.getContext(),"03de14ff4bda451ee3108a1070c21129");
        user = BmobUser.getCurrentUser(User.class);
        edt_text= (EditText) findViewById(R.id.edt_title);
        tv_title=(TextView)findViewById(R.id.tv_title);
        btnOK = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        tv_title.setText(title);
        tv_title.setTextColor(Color.WHITE);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switch(title){
                   case "新建小组":{
                       addGroup();
                       break;
                   }
                   case "邀请成员":{
                       inviteMember();
                       break;
                   }
               }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void addGroup(){
        //检验组名是否存在
        String name = edt_text.getText().toString();
        BmobQuery<Group> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("groupname", name);
        categoryBmobQuery.findObjects(new FindListener<Group>() {
            @Override
            public void done(final List<Group> object, BmobException e) {
                if (e == null) {
                    if (object.size()==0){
                        //Group创建
                        Group group = new Group();
                        group.setGroupname(edt_text.getText().toString());
                        group.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    //用户表下添加该组
                                    user.addUnique("groupname",edt_text.getText().toString());
                                    user.update(user.getObjectId(),new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                user = BmobUser.getCurrentUser(User.class);
                                                Log.e("UG_user","update success");
                                            }else{
                                                Log.e("UG_user",e.getMessage());
                                            }
                                        }
                                    });
                                    //group的跟新
                                    Group group2 = new Group();
                                    group2.add("admin",user.getObjectId());
                                    group2.add("member",user.getObjectId());
                                    group2.update(s,new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                Log.e("UG_group","update success");
                                            }
                                            else{
                                                Log.e("UG_group_up",e.getMessage());
                                            }
                                        }
                                    });
                                } else{
                                    Log.e("UG_group_sa",e.getMessage());
                                }
                            }
                        });
                        Toast.makeText(context,"创建成功",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(context,"该组名已存在",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    private void inviteMember(){
        String nickname = edt_text.getText().toString();

        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("nickname", nickname);
        categoryBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size()==0){
                        Toast.makeText(context,"该用户不存在",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        User target_user = object.get(0);
                        invite(target_user);
                    }
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }

    private void invite(User target_user){
        Group group = BoomSQL.getGroup();
        //user的更新
        target_user.addUnique("groupname",group.getGroupname());
        target_user.update(target_user.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.e("UG_user","update success");
                }else{
                    Log.e("UG_user",e.getMessage());
                }
            }
        });
        //group的更新
        Group group2 = new Group();
        group2.add("member",target_user.getObjectId());
        group2.update(group.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    dismiss();
                    Toast.makeText(context,"邀请成功",Toast.LENGTH_SHORT).show();
                    Log.e("UG_group","update success");
                }
                else{
                    Log.e("UG_group_up",e.getMessage());
                }
            }
        });
    }

}