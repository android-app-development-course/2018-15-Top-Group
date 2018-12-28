package com.example.liyixun.TopGroup.personpage;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.liyixun.TopGroup.Calendar.BoomSQL;
import com.example.liyixun.TopGroup.Group;
import com.example.liyixun.TopGroup.LoginActivity;
import com.example.liyixun.TopGroup.MainActivity;
import com.example.liyixun.TopGroup.R;
import com.example.liyixun.TopGroup.User;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Fragment_person extends Fragment implements View.OnClickListener{
    public TextView UserName;
    public TextView GroupName;
    public TextView GroupNumber;
    public TextView CalendarNumber;
    public ImageView mebg;
    public CircleImageView meavatar;
    public Button meImage;
    public Button add_group;
    public Button invite_person;
    public Button switch_group;
    public Button quit;
    private MainActivity activity;

    private User user;
    private Group group;
    public static final int CHOOSE_PHOTO = 2;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        //Toolbar toolbar=(Toolbar) view.findViewById(R.id.me_toolbar);
        //CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout) view.findViewById(R.id.collasping_toobar);
        UserName=(TextView) view.findViewById(R.id.me_username);
        GroupName=(TextView) view.findViewById(R.id.nowGroup);
        GroupNumber=(TextView) view.findViewById(R.id.value_groupnumber);
        CalendarNumber=(TextView) view.findViewById(R.id.value_notenumber);
        meImage=(Button) view.findViewById(R.id.setImage);
        mebg=(ImageView) view.findViewById(R.id.background);
        meavatar=(CircleImageView) view.findViewById(R.id.userImage);
        add_group=(Button) view.findViewById(R.id.me_addGroup);
        invite_person=(Button) view.findViewById(R.id.joinGroup);
        switch_group=(Button) view.findViewById(R.id.me_SwitchGroup);
        quit=(Button) view.findViewById(R.id.quit);


        //setHasOptionsMenu(true);
        //activity.setSupportActionBar(toolbar);
        //ActionBar actionBar=activity.getSupportActionBar();
        meImage.setOnClickListener(this);
        add_group.setOnClickListener(this);
        invite_person.setOnClickListener(this);
        switch_group.setOnClickListener(this);
        quit.setOnClickListener(this);

        //-----初始化数据与更新UI--
        if ( BoomSQL.getGroup() == null ){
            initName_Group(user.getNickname(),"");
            GroupNumber.setText("0");
            CalendarNumber.setText("0");
        } else {
            group = BoomSQL.getGroup();
            initName_Group(user.getNickname(),group.getGroupname());
            initCalendarNumber(group.getGroupname());
        }
        initGroupNumber(user.getObjectId());

        if (user.getAvatar() != null ) {
            loadavatar();
        } else {
            Glide.with(getContext()).load(R.drawable.gui).into(mebg);
            Glide.with(getContext()).load(R.drawable.gui).into(meavatar);
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        user = BmobUser.getCurrentUser(User.class);


      /*  initName_Group(user.getNickname(),"");
        initCalendarNumber("1");
        initGroupNumber("aa");*/
    }
    public void initName_Group(String username,String groupname){
        UserName.setText(username);
        GroupName.setText("现在所在小组："+groupname);
    }
    public void initGroupNumber(String username){
        BmobQuery<Group> query = new BmobQuery<Group>();
        String [] user = {username};
        query.addWhereContainsAll("member", Arrays.asList(user));
        query.findObjects(new FindListener<Group>() {

            @Override
            public void done(List<Group> object,BmobException e) {
                if(e==null){
                    String a=""+object.size();
                    GroupNumber.setText(a);
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }

        });
    }
    public void initCalendarNumber(String groupname){
        BmobQuery<com.example.liyixun.TopGroup.Calendar.Calendar> query = new BmobQuery<com.example.liyixun.TopGroup.Calendar.Calendar>();
        query.addWhereEqualTo("GroupName", groupname);
        query.count(com.example.liyixun.TopGroup.Calendar.Calendar.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    CalendarNumber.setText(count.toString());
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.setImage:{
                if (ContextCompat.checkSelfPermission( activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
                break;
            }

            case R.id.me_addGroup:{
                mDialog mdialog =new mDialog(getContext(),"新建小组");
                mdialog.show();
            }
                break;
            case R.id.joinGroup:{
                if (BoomSQL.getGroup() == null ){
                    Toast.makeText(BoomSQL.getContext(),"请进入小组内再进行该操作",Toast.LENGTH_SHORT).show();
                } else {
                    mDialog mdialog =new mDialog(getContext(),"邀请成员");
                    mdialog.show();
                }
                break;
            }

            case R.id.me_SwitchGroup:{
                sgDialog sgDialog = new sgDialog(getContext());
                sgDialog.show();

                break;
            }

            case R.id.quit:{
                Intent intent = new Intent(getContext(), LoginActivity.class);
                activity.finish();
                startActivity(intent);
                break;
            }
        }
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
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            update_avatar(bitmap);
            final BmobFile bmobFile = new BmobFile(new File(imagePath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        addavatar(bmobFile);
                        Log.i("Avatar","上传文件成功:" );
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

    private void addavatar(BmobFile bmobFile){
        user.setAvatar(bmobFile);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null) {
                    Toast.makeText(getContext(),"头像上传成功",Toast.LENGTH_SHORT);
                } else {
                    Log.e("Avatar",e.getMessage());
                }
            }
        });
    }

    private void loadavatar(){
        BmobFile file = user.getAvatar();
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    //toast("下载成功,保存路径:"+savePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(savePath);
                    update_avatar(bitmap);
                }else{
                    Log.e("load_avatar",e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }
        });
    }

    private void update_avatar(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(bitmap);
        Glide.with(getContext()).load(drawable).into(mebg);
        Glide.with(getContext()).load(drawable).into(meavatar);
    }
}
