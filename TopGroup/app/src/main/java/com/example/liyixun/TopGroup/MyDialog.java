package com.example.liyixun.TopGroup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class MyDialog extends Dialog{
    private String title;
    private EditText edt_title;
    private Button btnOK;
    private Button btnCancel;
    private String storeid;
    public MyDialog(Context context,String storeid,String dialogName) {
        super(context);
        this.title = dialogName;
        this.storeid = storeid;
    }

    public MyDialog(Context context,String storeid) {
        super(context);
        this.title = "图片";
        this.storeid = storeid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_dialog);
        Bmob.initialize(this.getContext(),"03de14ff4bda451ee3108a1070c21129");

        edt_title = (EditText) findViewById(R.id.edt_title);
        btnOK = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        edt_title.setText(title);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edt_title.getText().toString();
                if (!str.isEmpty()) {
                    title = str;
                }
                set_title();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_title();
            }
        });
    }

    public void set_title(){
        Store store = new Store();
        store.setObjectId(storeid);
        store.setTitle(title);
        store.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(getContext(),"设置标题成功",Toast.LENGTH_SHORT).show();
                    //Log.i("Store","设置标题成功");
                } else {
                    Log.e("Store","设置标题失败");
                }
                dismiss();
            }
        });
    }
}
