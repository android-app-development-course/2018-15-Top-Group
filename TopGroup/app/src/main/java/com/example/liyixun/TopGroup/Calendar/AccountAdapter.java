package com.example.liyixun.TopGroup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private Context mcontext;
    public List<Account> mAccountList;

    static class  ViewHolder extends RecyclerView.ViewHolder {
        View Accountview;
        CardView cardView;
        TextView account_num;
        TextView account_detail;
        CircleImageView Account_author_icon;
        TextView getAccount_author_name;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            Account_author_icon = (CircleImageView) view.findViewById(R.id.account_author_icon);
            getAccount_author_name = (TextView) view.findViewById(R.id.account_author_name);
            account_detail = (TextView) view.findViewById(R.id.ac_tv2_detail);
            account_num = (TextView) view.findViewById(R.id.ac_tv2_num);
        }
    }

    public AccountAdapter(List<Account> AccountList) {
        mAccountList = AccountList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        if ( mcontext == null ) {
            mcontext = parent.getContext();
        }

        View view = LayoutInflater.from(mcontext).inflate(R.layout.account_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Account Account = mAccountList.get(position);
        holder.account_detail.setText(Account.getDetail());
        holder.account_num.setText(Account.getNum());

        Glide.with(mcontext).load(R.drawable.timg_5).into(holder.Account_author_icon);
    }

    @Override
    public int getItemCount(){
        return mAccountList.size();
    }
}




