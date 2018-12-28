package com.example.liyixun.TopGroup;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liyixun.TopGroup.Calendar.BoomSQL;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Fragment_income extends Fragment implements View.OnClickListener{

    private RelativeLayout r3;
    private Button btn_cancle_income;
    private Button btn_submit_income;
    private ImageView iv_type_income;
    private TextView tv_type_income;
    private EditText et_type_income;
    private ImageView iv_other_income;
    private ImageView iv_invest_income;
    private ImageView iv_refund_income;
    private ImageView iv_cash_income;
    private ImageView iv_redmoney_income;
    private ImageView iv_bonus_income;
    private MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.account_income, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        r3 =(RelativeLayout) view.findViewById(R.id.r3);
        et_type_income = (EditText) view.findViewById(R.id.et_type_income);
        //r3.setBackgroundColor(0xffFFC0CB);
        //取消“X”按钮
        btn_cancle_income =(Button) view.findViewById(R.id.btn_cancel_income);
        //提交“√”按钮
        btn_submit_income =(Button) view.findViewById(R.id.btn_submit_income);
        //收入类型图片
        iv_type_income =(ImageView) view.findViewById(R.id.iv_type_income);
        //收入类型
        tv_type_income =(TextView) view.findViewById(R.id.tv_type_income);
        //添加收入金额
        et_type_income =(EditText) view.findViewById(R.id.et_type_income);

        iv_other_income =(ImageView) view.findViewById(R.id.iv_other_income);     //其他收入
        iv_invest_income =(ImageView) view.findViewById(R.id.iv_invest);          //投资收入
        iv_refund_income =(ImageView) view.findViewById(R.id.iv_refund);          //退款收入
        iv_cash_income =(ImageView) view.findViewById(R.id.iv_cash);              //现金收入
        iv_redmoney_income =(ImageView) view.findViewById(R.id.iv_redmoney);      //红包收入
        iv_bonus_income =(ImageView) view.findViewById(R.id.iv_bonus);  //奖金收入

        iv_other_income.setOnClickListener(this);
        iv_invest_income.setOnClickListener(this);
        iv_refund_income.setOnClickListener(this);
        iv_cash_income.setOnClickListener(this);
        iv_redmoney_income.setOnClickListener(this);
        iv_bonus_income.setOnClickListener(this);
        btn_submit_income.setOnClickListener(this);
        btn_cancle_income.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.iv_other_income:{
                 iv_type_income.setBackground(iv_other_income.getBackground());
                 tv_type_income.setText("其它");
                 break;
             }
             case R.id.iv_invest:{
                 iv_type_income.setBackground(iv_invest_income.getBackground());
                 tv_type_income.setText("投资");
                 break;
             }case R.id.iv_refund:{
                 iv_type_income.setBackground(iv_refund_income.getBackground());
                 tv_type_income.setText("退款");
                 break;
             }case R.id.iv_cash:{
                 iv_type_income.setBackground(iv_cash_income.getBackground());
                 tv_type_income.setText("现金");
                 break;
             }case R.id.iv_redmoney:{
                 iv_type_income.setBackground(iv_redmoney_income.getBackground());
                 tv_type_income.setText("红包");
                 break;
             }case R.id.iv_bonus:{
                 iv_type_income.setBackground(iv_bonus_income.getBackground());
                 tv_type_income.setText("奖金");
                 break;
             }

             case R.id.btn_cancel_income:{
                 activity = (MainActivity) getActivity();
                 //activity.removeFragment(this);
                 Fragment_account fragment_account = new Fragment_account();
                 activity.replaceFragment(fragment_account);
                 break;
             }

             case R.id.btn_submit_income:{
                 if (et_type_income.getText().toString().isEmpty() ) return;
                 if (BoomSQL.getGroup() == null) {
                     Toast.makeText(getContext(),"请到个人界面选择小组",Toast.LENGTH_SHORT).show();
                     return;
                 }
                 Group mgroup = BoomSQL.getGroup();
                 User muser = BmobUser.getCurrentUser(User.class);
                 String type = "收入";
                 String detail = tv_type_income.getText().toString();
                 Integer num = Integer.valueOf(et_type_income.getText().toString());
                 Account account = new Account();
                 account.setGroup(mgroup);
                 account.setUser(muser);
                 account.setType(type);
                 account.setDetail(detail);
                 account.setNum(num);
                 account.save(new SaveListener<String>() {
                     @Override
                     public void done(String s, BmobException e) {
                         if (e==null){
                             Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
                             activity = (MainActivity) getActivity();
                             Fragment_account fragment_account = new Fragment_account();
                             activity.replaceFragment(fragment_account);
                         }else{
                             Toast.makeText(getContext(),"发送失败",Toast.LENGTH_SHORT).show();
                         }
                     }
                 });

                 break;
             }
         }
    }
}
