package com.example.liyixun.TopGroup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

public class Fragment_spend extends Fragment implements View.OnClickListener{

    private RelativeLayout r2;
    private Button btn_cancle_spend;
    private Button btn_submit_spend;
    private ImageView iv_type_spend;
    private TextView tv_type_spend;
    private EditText et_type_spend;
    private ImageView iv_other_spend;
    private ImageView iv_food_spend;
    private ImageView iv_traffic_spend;
    private ImageView iv_drug_spend;
    private ImageView iv_fruit_spend;
    private ImageView iv_snacks_spend;
    private ImageView iv_tel_spend;
    private MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.account_spend, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    private void init(View view) {
        r2 =(RelativeLayout) view.findViewById(R.id.r2);
        //r2.setBackgroundColor(0xffFFC0CB);
        //取消“X”按钮
        btn_cancle_spend =(Button) view.findViewById(R.id.btn_cancel_spend);
        //提交“√”按钮
        btn_submit_spend =(Button) view.findViewById(R.id.btn_submit_spend);

        //支出类型图片
        iv_type_spend =(ImageView) view.findViewById(R.id.iv_type_spend);
        //支出类型
        tv_type_spend =(TextView) view.findViewById(R.id.tv_type_spend);
        //添加支出金额
        et_type_spend =(EditText) view.findViewById(R.id.et_type_spend);

        iv_other_spend =(ImageView) view.findViewById(R.id.iv_other_spend);       //其他支出
        iv_food_spend =(ImageView) view.findViewById(R.id.iv_food);               //餐饮支出
        iv_traffic_spend =(ImageView) view.findViewById(R.id.iv_traffic);         //交通支出
        iv_drug_spend =(ImageView) view.findViewById(R.id.iv_drug);               //药品支出
        iv_fruit_spend =(ImageView) view.findViewById(R.id.iv_fruit);             //水果支出
        iv_snacks_spend =(ImageView) view.findViewById(R.id.iv_snacks);           //零食支出
        iv_tel_spend =(ImageView) view.findViewById(R.id.iv_telephone);//话费支出

        iv_other_spend.setOnClickListener(this);
        iv_food_spend.setOnClickListener(this);
        iv_traffic_spend.setOnClickListener(this);
        iv_drug_spend.setOnClickListener(this);
        iv_fruit_spend.setOnClickListener(this);
        iv_snacks_spend.setOnClickListener(this);
        iv_tel_spend.setOnClickListener(this);
        btn_submit_spend.setOnClickListener(this);
        btn_cancle_spend.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_other_spend:{
                iv_type_spend.setBackground(iv_other_spend.getBackground());
                tv_type_spend.setText("其它");
                break;
            }
            case R.id.iv_food:{
                iv_type_spend.setBackground(iv_food_spend.getBackground());
                tv_type_spend.setText("餐饮");
                break;
            }case R.id.iv_traffic:{
                iv_type_spend.setBackground(iv_traffic_spend.getBackground());
                tv_type_spend.setText("交通");
                break;
            }case R.id.iv_drug:{
                iv_type_spend.setBackground(iv_drug_spend.getBackground());
                tv_type_spend.setText("药品");
                break;
            }case R.id.iv_fruit:{
                iv_type_spend.setBackground(iv_fruit_spend.getBackground());
                tv_type_spend.setText("水果");
                break;
            }case R.id.iv_snacks:{
                iv_type_spend.setBackground(iv_snacks_spend.getBackground());
                tv_type_spend.setText("零食");
                break;
            }case R.id.iv_telephone:{
                iv_type_spend.setBackground(iv_snacks_spend.getBackground());
                tv_type_spend.setText("话费");
                break;
            }

            case R.id.btn_cancel_spend:{
                activity = (MainActivity) getActivity();
                //activity.removeFragment(this);
                Fragment_account fragment_account = new Fragment_account();
                activity.replaceFragment(fragment_account);
                break;
            }

            case R.id.btn_submit_spend:{
                if (et_type_spend.getText().toString().isEmpty() ) return;
                if (BoomSQL.getGroup() == null) {
                    Toast.makeText(getContext(),"请到个人界面选择小组",Toast.LENGTH_SHORT).show();
                    return;
                }
                activity = (MainActivity) getActivity();
                Group mgroup = BoomSQL.getGroup();
                User muser = BmobUser.getCurrentUser(User.class);
                String type = "支出";
                String detail = tv_type_spend.getText().toString();
                Integer num = Integer.valueOf(et_type_spend.getText().toString());
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
                            activity = (MainActivity) getActivity();
                            Fragment_account fragment_account = new Fragment_account();
                            activity.replaceFragment(fragment_account);
                            Toast.makeText(getContext(),"发送成功",Toast.LENGTH_SHORT).show();
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
