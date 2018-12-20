package com.example.liyixun.TopGroup;

public class Gallery {
    private String title;
    private int imageId;
    private String author;
    private int userimageId;

    public Gallery(String title,int imageId) {
        this.title = title;
        this.imageId = imageId;
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
}
