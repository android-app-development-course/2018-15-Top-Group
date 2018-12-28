package com.example.liyixun.TopGroup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PhotoDialog extends Dialog{
    private Bitmap bitmap;
    private ImageView imv;
    public PhotoDialog(Context context,Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_dialog);
        imv = (ImageView) findViewById(R.id.pd_imv);
        //imv.setImageDrawable(drawable);
        Drawable drawable = new BitmapDrawable(bitmap);
        Glide.with(getContext()).load(drawable).into(imv);
        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
