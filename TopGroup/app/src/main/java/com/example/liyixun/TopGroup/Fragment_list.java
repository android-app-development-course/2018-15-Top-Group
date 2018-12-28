package com.example.liyixun.TopGroup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.liyixun.TopGroup.Calendar.BoomSQL;

import cn.bmob.v3.Bmob;

public class Fragment_list extends Fragment {

    private RecyclerView recyclerView;
    private AccountAdapter adapter;
    private MainActivity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.ac_recycle_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        activity = (MainActivity) getActivity();
        adapter = new AccountAdapter(BoomSQL.getIncome());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getContext(),"03de14ff4bda451ee3108a1070c21129");
    }
}
