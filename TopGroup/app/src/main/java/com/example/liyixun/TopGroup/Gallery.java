package com.example.liyixun.TopGroup;

import android.graphics.Bitmap;

import java.io.Serializable;

import cn.bmob.v3.datatype.BmobFile;

public class Gallery implements Serializable {
    private String title;
    private int imageId;
    private String author;
    private int userimageId;
    private Bitmap image;
    private Bitmap icon;

    public Gallery(String title,String author,Bitmap image) {
        this.title = title;
        this.author = author;
        this.image = image;
    }

    public Gallery(String title,String author,Bitmap image,Bitmap icon) {
        this.title = title;
        this.author = author;
        this.image = image;
        this.icon = icon;
    }

    public Gallery(String title,int imageId,String author,int userimageId) {
        this.title = title;
        this.imageId = imageId;
        this.author = author;
        this.userimageId = userimageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getUserimageId() {
        return userimageId;
    }

    public void setUserimageId(int userimageId) {
        this.userimageId = userimageId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
