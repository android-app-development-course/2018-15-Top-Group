package com.example.liyixun.TopGroup.Calendar;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.example.liyixun.TopGroup.Account;
import com.example.liyixun.TopGroup.Group;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class BoomSQL extends Application {
    private static Context context;
    private static Group group;
    private static List<Account> income = new ArrayList<Account>();
    private static List<Account> spend = new ArrayList<Account>();

    public static void setGroup(Group group) {
        BoomSQL.group = group;
    }

    public static List<Account> getIncome() {
        return income;
    }

    public static void setIncome(List<Account> income) {
        BoomSQL.income = income;
    }

    public static List<Account> getSpend() {
        return spend;
    }

    public static void setSpend(List<Account> spend) {
        BoomSQL.spend = spend;
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
