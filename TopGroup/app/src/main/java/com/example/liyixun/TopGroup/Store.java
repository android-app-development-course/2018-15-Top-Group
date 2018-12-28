package com.example.liyixun.TopGroup;

import android.graphics.BitmapFactory;
import android.graphics.Point;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;

public class Store extends BmobObject{
    private BmobFile bfile;
    private String title;
    private Group group;
    private User user;

    public BmobFile getBfile() {
        return bfile;
    }

    public void setBfile(BmobFile bfile) {
        this.bfile = bfile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
