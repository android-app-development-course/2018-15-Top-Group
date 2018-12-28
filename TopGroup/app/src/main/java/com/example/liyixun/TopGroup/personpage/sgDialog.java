package com.example.liyixun.TopGroup.personpage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liyixun.TopGroup.Calendar.BoomSQL;
import com.example.liyixun.TopGroup.Gallery;
import com.example.liyixun.TopGroup.Group;
import com.example.liyixun.TopGroup.MainActivity;
import com.example.liyixun.TopGroup.R;
import com.example.liyixun.TopGroup.User;
import com.example.liyixun.TopGroup.UserGroup;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class sgDialog extends Dialog {

    private Context context;
    private Spinner spinner;
    private TextView textView;
    private MainActivity activity;
    private Button btn_ok;
    private Button btn_cancel;
    private User user;
    private List<String> name = new ArrayList<String>();
    private static final int GET_GROUPNAME = 1;
    private static final int SET_GROUP = 2;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_GROUPNAME:
                    name = (List<String>) msg.obj;
                    spn_update();
                    break;
                case SET_GROUP:
                    Group mgroup = (Group) msg.getData().getSerializable("group");
                    BoomSQL.setGroup(mgroup);
                    Intent intent = new Intent(context,MainActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    public sgDialog(Context context){
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.switchgroup_dialog);
        Bmob.initialize(this.getContext(),"03de14ff4bda451ee3108a1070c21129");

        spinner = (Spinner) findViewById(R.id.sw_spinner);
        textView = (TextView) findViewById(R.id.sw_tv_title);
        btn_ok = (Button) findViewById(R.id.sw_btn_ok);
        btn_cancel = (Button) findViewById(R.id.sw_btn_cancel);

        textView.setText("选择进入的小组");
        user = BmobUser.getCurrentUser(User.class);
        spn_update();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_ok();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_cancel();
            }
        });
    }

    private void select_ok(){
        if (name == null ) return;
        spinner.getSelectedItemPosition();
        final String groupname = name.get(spinner.getSelectedItemPosition());
        BmobQuery<Group> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("groupname", groupname);
        categoryBmobQuery.findObjects(new FindListener<Group>() {
            @Override
            public void done(List<Group> object, BmobException e) {
                if (e == null) {
                    //MainActivity.actionStart(context,object.get(0).getObjectId());
                    Message message = new Message();
                    message.what = SET_GROUP;
                    Bundle bundle = new Bundle();
                    Group mgroup = (Group)object.get(0);
                    bundle.putSerializable("group",mgroup);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Toast.makeText(context,"进入小组:"+groupname,Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("BMOB", e.toString());
                }
                dismiss();
            }
        });
    }

    private void select_cancel(){
        dismiss();
    }

    private void spn_update(){
        name = user.getGroupname();
        if (name!=null) {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,name);
            spinner.setAdapter(adapter);
        }
    }

    private void getgname(){
        BmobQuery<Group> query = new BmobQuery<Group>();
        //String [] userid = {"阅读","唱歌"};
        query.addWhereContains("member",user.getObjectId());
        query.findObjects(new FindListener<Group>() {
            @Override
            public void done(List<Group> object,BmobException e) {
                if(e==null){
                    List<String> ln = new ArrayList<String>();
                    for (final Group group : object){
                        ln.add(group.getGroupname());
                    }
                    Message message = new Message();
                    message.what = GET_GROUPNAME;
                    message.obj = ln;
                    handler.sendMessage(message);
                    Log.i("bmob","查询成功：共" + object.size() + "条数据。");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }
}


