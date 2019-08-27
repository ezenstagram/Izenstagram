package com.example.izenstargram.search.model;

public class LinkedTagDTO {

    private int post_id; // 게시글번호
    private int tag_id; // 태그번호

    public LinkedTagDTO() {
        super();
    }

    public LinkedTagDTO(int post_id, int tag_id) {
        super();
        this.post_id = post_id;
        this.tag_id = tag_id;
    }


    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

}
