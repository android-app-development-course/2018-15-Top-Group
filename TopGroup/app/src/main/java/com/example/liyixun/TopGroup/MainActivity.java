package com.example.liyixun.TopGroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.*;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;


import com.example.liyixun.TopGroup.Calendar.BoomSQL;
import com.example.liyixun.TopGroup.Calendar.Fragment_calendar;
import com.example.liyixun.TopGroup.personpage.Fragment_person;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public List<Gallery> gl = new ArrayList<>();
    private Toolbar tb;
    public Fragment_storage fragment_storage;
    public Fragment_calendar fragment_calendar;
    public Fragment_account fragment_account;
    public Fragment_person fragment_person;
    private String groupid;
    private User muser;
    private Group mgroup;
    private BottomNavigationView navigation;
    public List<Account> al_income = new ArrayList<>();
    public List<Account> al = new ArrayList<>();
    public List<Account> al_spend = new ArrayList<>();
    private int now_fragment = 0;
    private static final int GET_GROUP = 11;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_GROUP:
                    Group group = (Group) msg.getData().getSerializable("group");
                    mgroup = group;
                    groupid = mgroup.getObjectId();
                    BoomSQL.setGroup(group);
                    Log.e("MainActivity",mgroup.getGroupname());
                    fragment_person = new Fragment_person();
                    replaceFragment(fragment_person);
                    break;
                default:
                    break;
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    if (now_fragment == 1) return false;
                    fragment_calendar = new Fragment_calendar();
                    getSupportFragmentManager().getFragments();
                    replaceFragment(fragment_calendar);
                    now_fragment = 1;
                    //mTextMessage.setText(R.string.title_calendar);
                    return true;
                case R.id.navigation_account:
                    if (now_fragment == 2) return false;
                    fragment_account = new Fragment_account();
                    getSupportFragmentManager().getFragments();
                    replaceFragment(fragment_account);
                    now_fragment = 2;
                    //mTextMessage.setText(R.string.title_account);
                    return true;
                case R.id.navigation_storage:
                    if (now_fragment == 3) return false;
                    fragment_storage = new Fragment_storage();
                    getSupportFragmentManager().getFragments();
                    replaceFragment(fragment_storage);
                    now_fragment = 3;
                    return true;
                case R.id.navigation_me:
                    if (now_fragment == 4) return false;
                    fragment_person = new Fragment_person();
                    getSupportFragmentManager().getFragments();
                    replaceFragment(fragment_person);
                    now_fragment = 4;
                    //mTextMessage.setText(R.string.title_me);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this,"03de14ff4bda451ee3108a1070c21129");
        setContentView(R.layout.activity_main);
        init();


    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout,fragment);
        transaction.commit();
    }

    public void replaceFragment2(Fragment fragment) {
        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void removeFragment(Fragment fragment) {
        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    public static void actionStart(Context context, String id){
         Intent intent = new Intent(context,MainActivity.class);
         intent.putExtra("groupid",id);
         context.startActivity(intent);
    }


    private void init(){

        //groupid = getIntent().getStringExtra("groupid");

        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        navigation.setSelectedItemId(R.id.navigation_me);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (BoomSQL.getGroup() == null) {
            groupid = null;
            fragment_person = new Fragment_person();
            replaceFragment(fragment_person);
        } else {
            mgroup = BoomSQL.getGroup();
            groupid = mgroup.getObjectId();
            update_data();
        }
        if (groupid == null ){
            Toast.makeText(MainActivity.this,"请到个人界面选择小组",Toast.LENGTH_LONG).show();
        }
    }

    private void update_data() {
        muser = BmobUser.getCurrentUser(User.class);
        BmobQuery<Group> bmobQuery = new BmobQuery<Group>();
        bmobQuery.getObject(groupid, new QueryListener<Group>() {
            @Override
            public void done(Group group, BmobException e) {
                if (e==null){
                   Message message = new Message();
                   message.what = GET_GROUP;
                   Bundle bundle = new Bundle();
                    bundle.putSerializable("group",group);
                    message.setData(bundle);
                    handler.sendMessage(message);
                   Log.e("MainActivity",group.getGroupname());
                } else {
                    Log.e("MainActivity",e.getMessage());
                }
            }
        });
    }

    public Group getMGroup(){
        return mgroup;
    }

    public User getMuser() {
        return muser;
    }
}


