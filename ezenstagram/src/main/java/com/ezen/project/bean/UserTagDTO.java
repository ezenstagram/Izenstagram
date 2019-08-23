package com.ezen.project.bean;


public class UserTagDTO {

    private int post_id; // 게시글번호
    private int user_id; // 태그된 유저 아이디

    public UserTagDTO() {
        super();
    }

    public UserTagDTO(int post_id, int user_id) {
        super();
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
