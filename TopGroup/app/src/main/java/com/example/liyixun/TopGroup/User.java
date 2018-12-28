package com.example.liyixun.TopGroup;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class User extends BmobUser{
    private String nickname;
    private BmobFile avatar;
    private Integer age;
    private Integer gender;
    private List<String> groupname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }


    public List<String> getGroupname() {
        return groupname;
    }

    public void setGroupname(List<String> groupname) {
        this.groupname = groupname;
    }
}
