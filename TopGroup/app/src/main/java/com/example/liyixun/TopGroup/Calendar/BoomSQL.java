package com.example.liyixun.TopGroup.Calendar;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.example.liyixun.TopGroup.Group;

import cn.bmob.v3.Bmob;

public class BoomSQL extends Application {
    private static Context context;
    private static Group group;

    public static void setGroup(Group group) {
        BoomSQL.group = group;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Bmob.initialize(this, "03de14ff4bda451ee3108a1070c21129");
    }
    public static Context getContext(){
        return context;
    }

    public static Group getGroup(){
        return group;
    }
}
