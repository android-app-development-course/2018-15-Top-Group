package com.example.liyixun.TopGroup;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;

public class Fragment_storage extends Fragment {
    private List<Gallery> galleryList = new ArrayList<>();
    private MainActivity activity;
    private User muser;
    private RecyclerView recyclerView;
    private Group mgroup;
    private Boolean isload;
    private GalleryAdapter adapter;
    private int count;
    public static final int CHOOSE_PHOTO = 2;
    public static final int DOWNLOAD_BMOBFILE = 3;

    public Fragment_storage() {
        // Required empty public constructor
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DOWNLOAD_BMOBFILE:
                    isload = true;
                    int size = msg.arg1;
                    count++;
                    //Toast.makeText(activity,String.valueOf(size),Toast.LENGTH_LONG).show();
                    Gallery gallery = (Gallery) msg.getData().getSerializable("gallery");
                    adapter.mGalleryList.add(gallery);
                    adapter.notifyItemChanged(adapter.mGalleryList.size());
                    if (count == size)
                        //Toast.makeText(activity,"the last one",Toast.LENGTH_LONG).show();
                        setGalleryAdapter();
                        isload = false;
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_storage,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if (ContextCompat.checkSelfPermission( activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        galleryList = activity.gl;

        RefreshLayout refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);

        //设置 Footer 为 球脉冲 样式
        refreshLayout.setRefreshFooter(new BallPulseFooter(activity).setSpinnerStyle(SpinnerStyle.Scale));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.d("refresh_isload",String.valueOf(isload));
                if (!isload) load();
                refreshlayout.finishRefresh(2000,false);//传入false表示刷新失败
            }
        });

        isload = false;

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (!isload) load();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        adapter = new GalleryAdapter(activity.gl);
        recyclerView.setAdapter(adapter);
        if (activity.gl.size() == 0 ) load();
        else {
            count = activity.gl.size();
            //setGalleryAdapter();
        }


        //悬浮按钮点击
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission( activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this.getContext(),"03de14ff4bda451ee3108a1070c21129");
        activity = (MainActivity) getActivity();
        mgroup = activity.getMGroup();
        muser = activity.getMuser();
        count=0;

    }

    @Override
    public void onStop() {
        super.onStop();
        activity.gl = galleryList;
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

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(activity,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageONKitKat(data);
                    }
                    else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageONKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(activity,uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1]; //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是contnt类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); //根据图片路径显示图片
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            final BmobFile bmobFile = new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        addstore(bmobFile);
                        Toast.makeText(activity,"上传文件成功:" ,Toast.LENGTH_LONG).show();
                        //toast("上传文件成功:" + bmobFile.getFileUrl());
                    }else{
                        Toast.makeText(activity,"上传文件失败:" ,Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
        } else {
            Toast.makeText(activity,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    public String getImagePath(Uri uri,String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = activity.getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()){
                path = cursor.getString((cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            }
            cursor.close();
        }
        return path;
    }

    private void addstore(final BmobFile bmobFile){
        final Store store = new Store();
        store.setGroup(mgroup);
        store.setUser(muser);
        store.setBfile(bmobFile);
        store.setTitle("图片");
        store.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    MyDialog myDialog = new MyDialog(activity,store.getObjectId());
                    myDialog.show();
                    Toast.makeText(activity,"图片上传成功",Toast.LENGTH_SHORT).show();
                    //Log.i("Storage","图片上传成功");
                } else {
                    Log.e("Storage",e.getMessage());
                }
            }
        });
    }

    public void load(){
        isload = true;
        BmobQuery<Store> bmobQuery = new BmobQuery<>();

        bmobQuery.addWhereEqualTo("group",new BmobPointer(mgroup));
        bmobQuery.include("user");

        bmobQuery.findObjects(new FindListener<Store>() {
            @Override
            public void done(final List<Store> object, BmobException e) {
                if(e==null){
                    Log.d("load_ojsz",String.valueOf(object.size()));
                    Log.d("load_count",String.valueOf(count));
                    //Toast.makeText(activity,String.valueOf(object.size())+String.valueOf(count),Toast.LENGTH_LONG).show();
                    if (object.size() < count) {
                        isload = false;
                        return;
                    }
                    int i=0;
                    for (final Store store : object) {
                        i++;
                        Log.d("load_i",String.valueOf(i));

                        if ( i<= count ) continue;
                        BmobFile bmobfile = store.getBfile();
                        if(bmobfile!= null){
                            //调用bmobfile.download方法
                            bmobfile.download(new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(s);
                                    String title = store.getTitle();
                                    String name = store.getUser().getNickname();
                                    Gallery gallery = new Gallery(title,name,bitmap);
                                    Message message = new Message();
                                    message.what = DOWNLOAD_BMOBFILE;
                                    message.arg1 = object.size();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("gallery",gallery);
                                    message.setData(bundle);
                                    handler.sendMessage(message);
                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }
                            });
                        }
                    }
                }else{
                    Log.e("Storage","查询失败");
                }
            }
        });
    }

    private void setGalleryAdapter(){
        //adapter.notifyDataSetChanged();
        activity.gl = adapter.mGalleryList;
    }
}