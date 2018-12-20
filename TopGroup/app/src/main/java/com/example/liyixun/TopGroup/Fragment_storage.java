package com.example.liyixun.TopGroup;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.Bmob;

public class Fragment_storage extends Fragment {
    private List<Gallery> galleryList = new ArrayList<>();
    public Fragment_storage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage,container,false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        GalleryAdapter adapter = new GalleryAdapter(galleryList);
        recyclerView.setAdapter(adapter);

        //悬浮按钮点击
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"FAB",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this.getContext(),"03de14ff4bda451ee3108a1070c21129");
        loadGallery();
    }

    /*private void loadGallery() {
        for (int i=1; i<=50; i++) {
            Gallery p = new Gallery(getRandomLengthName("测试"+i),R.drawable.ic_storage);
            galleryList.add(p);
        }
    }*/

    private void loadGallery() {
        for (int i=1; i<=3; i++) {
            Gallery p1 = new Gallery("图片1",R.drawable.timg_2,"user1",R.drawable.timg_2);
            galleryList.add(p1);
            Gallery p2 = new Gallery("图片2",R.drawable.timg_3,"user2",R.drawable.timg_2);
            galleryList.add(p2);
            Gallery p3 = new Gallery("图片3",R.drawable.timg_4,"user3",R.drawable.timg_2);
            galleryList.add(p3);
            Gallery p4 = new Gallery("图片4",R.drawable.timg_5,"user4",R.drawable.timg_2);
            galleryList.add(p4);
            Gallery p5 = new Gallery("图片5",R.drawable.timg_6,"user5",R.drawable.timg_2);
            galleryList.add(p5);
            Gallery p6 = new Gallery("图片6",R.drawable.timg_7,"user6",R.drawable.timg_2);
            galleryList.add(p6);
        }
    }

    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20)+1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i< length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }

    /*private void loadGallery() {
        for (int i=1; i<=50; i++) {
            Gallery p = new Gallery("测试"+i,R.drawable.ic_storage);
            galleryList.add(p);
        }
    }*/

}
/*public class Fragment_storage extends Fragment {
    private List<Gallery> galleryList = new ArrayList<>();
    public Fragment_storage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage,container,false);
        loadGallery();
        GalleryAdapter adapter = new GalleryAdapter(this.getContext(),R.layout.gallery_item,galleryList);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this.getContext(),"03de14ff4bda451ee3108a1070c21129");
    }

    private void loadGallery() {
        for (int i=1; i<=50; i++) {
            Gallery p = new Gallery("测试"+i,R.drawable.ic_storage);
            galleryList.add(p);
        }
    }

}*/
