package com.example.liyixun.TopGroup;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liyixun.TopGroup.Calendar.BoomSQL;
import com.example.liyixun.TopGroup.personpage.Fragment_person;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Fragment_account extends Fragment implements View.OnClickListener{

    private LinearLayout ll_RG;
    private RelativeLayout rl;
    private Button btn_income;
    private Button btn_spend;
    private TextView tv_income;
    private TextView tv_spend;
    private MainActivity activity;
    private Fragment_income fragment_income;
    private Fragment_spend fragment_spend;
    private List<Account> income = new ArrayList<Account>();
    private List<Account> spend = new ArrayList<Account>();
    private Integer  num_income;
    private Integer  num_spend;
    private Integer  sum;
    private Group mgroup;
    private User muser;
    private RadioButton rb_income;
    private RadioButton rb_spend;
    private static final int LOAD_START = 1;
    private static final int LOAD_LIST = 2;
    private static final int LOAD_END = 3;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOAD_START: {
                    income.clear();
                    spend.clear();
                    break;
                }
                case LOAD_LIST: {
                    Account account = (Account) msg.getData().getSerializable("account");
                    if (account.getType().equals("收入")){
                        income.add(account);
                        Log.e("handle",account.getDetail());
                    } else if (account.getType().equals("支出")){
                        spend.add(account);
                    }
                    break;
                }
                case LOAD_END: {
                    BoomSQL.setIncome(income);
                    BoomSQL.setSpend(spend);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);
        init(view);
        muser = BmobUser.getCurrentUser(User.class);
        if (BoomSQL.getGroup() != null){
            mgroup = BoomSQL.getGroup();
            refresh();
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(getContext(),"03de14ff4bda451ee3108a1070c21129");

        //初始化主界面

    }

    private void init(View view) {
        //设置背景颜色
        rl =(RelativeLayout) view.findViewById(R.id.rl);
        //rl.setBackgroundColor(R.color.white);
        ll_RG =(LinearLayout) view.findViewById(R.id.ll_RG);
        //收入按钮
        btn_income =(Button) view.findViewById(R.id.btn_income);
        //支出按钮
        btn_spend =(Button)view.findViewById(R.id.btn_spend);
        //显示收入
        tv_income =(TextView) view.findViewById(R.id.tv_income);
        //显示支出
        tv_spend =(TextView) view.findViewById(R.id.tv_spend);

        //进入收入界面
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_income();
            }
        });
        //进入支出界面
        btn_spend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_spend();
            }
        });

        rb_income = (RadioButton) view.findViewById(R.id.rb_income);
        rb_spend = (RadioButton) view.findViewById(R.id.rb_spend);

        rb_income.setOnClickListener(this);
        rb_spend.setOnClickListener(this);

    }

    private void activity_spend() {
        activity = (MainActivity) getActivity();
        fragment_spend = new Fragment_spend();

        activity.replaceFragment(fragment_spend);
    }

    private void activity_income() {
        activity = (MainActivity) getActivity();
        fragment_income = new Fragment_income();
        activity.replaceFragment(fragment_income);
    }

    private void refresh(){
        BmobQuery<Account> bmobQuery = new BmobQuery<>();

        bmobQuery.addWhereEqualTo("group",mgroup);
        bmobQuery.include("user");

        bmobQuery.findObjects(new FindListener<Account>() {
            @Override
            public void done(final List<Account> object, BmobException e) {
                if (e==null){
                    int n = object.size();
                    int nincome=0;
                    int nspend=0;
                    int sum;
                    Message message1 = new Message();
                    message1.what = LOAD_START;
                    handler.sendMessage(message1);
                    for (Account account: object){
                        Message message = new Message();
                        message.what = LOAD_LIST;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("account",account);
                        message.setData(bundle);
                        handler.sendMessage(message);
                        if (account.getType().equals("收入")){
                            nincome += account.getNum();
                        } else if (account.getType().equals("支出")){
                            nspend += account.getNum();
                        }
                    }
                    Message message2 = new Message();
                    message2.what = LOAD_END;
                    handler.sendMessage(message2);
                    sum = nincome - nspend;
                    tv_income.setText(String.valueOf(nincome));
                    tv_spend.setText(String.valueOf(nspend));
                } else {
                    Log.e("account_load",e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_income:{
                activity = (MainActivity) getActivity();
                Fragment_list fragment_list = new Fragment_list();
                activity.replaceFragment2(fragment_list);
                break;
            }
            case R.id.rb_spend:{
                activity = (MainActivity) getActivity();
                Fragment_list2 fragment_list2 = new Fragment_list2();
                activity.replaceFragment2(fragment_list2);
                break;
            }
            case R.id.btn_income:{
                activity_income();
                break;
            }
            case R.id.btn_spend:{
                activity_spend();
                break;
            }
            default:
                break;
        }
    }
}
