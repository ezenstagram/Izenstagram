package com.example.izenstargram.feed.model;

import java.io.Serializable;

public class PostImageList implements Serializable {

    private Integer post_Id;

    private Integer image_Id;

    private String image_Url;

    public Integer getPost_Id() {
        return post_Id;
    }

    public void setPost_Id(Integer post_Id) {
        this.post_Id = post_Id;
    }

    public Integer getImage_Id() {
        return image_Id;
    }

    public void setImage_Id(Integer image_Id) {
        this.image_Id = image_Id;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }
}
