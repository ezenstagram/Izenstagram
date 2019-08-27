package com.example.izenstargram.like.bean;

public class NotifiInfo {
    private int notifi_id;
    private int mode_id;
    private String post_image;
    private String comment_txt;
    private boolean follow_flg;
    private int target_id;
    private int target_sub_id;
    private int act_user_id;
    private String act_login_id;
    private String profile_photo;
    private int target_user_id;
    private String target_login_id;
    private boolean delete_flg;
    private String reg_date;

    public int getNotifi_id() {
        return notifi_id;
    }

    public void setNotifi_id(int notifi_id) {
        this.notifi_id = notifi_id;
    }

    public int getMode_id() {
        return mode_id;
    }

    public void setMode_id(int mode_id) {
        this.mode_id = mode_id;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getComment_txt() {
        return comment_txt;
    }

    public void setComment_txt(String comment_txt) {
        this.comment_txt = comment_txt;
    }

    public boolean isFollow_flg() {
        return follow_flg;
    }

    public void setFollow_flg(boolean follow_flg) {
        this.follow_flg = follow_flg;
    }

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public int getTarget_sub_id() {
        return target_sub_id;
    }

    public void setTarget_sub_id(int target_sub_id) {
        this.target_sub_id = target_sub_id;
    }

    public int getAct_user_id() {
        return act_user_id;
    }

    public void setAct_user_id(int act_user_id) {
        this.act_user_id = act_user_id;
    }

    public String getAct_login_id() {
        return act_login_id;
    }

    public void setAct_login_id(String act_login_id) {
        this.act_login_id = act_login_id;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public int getTarget_user_id() {
        return target_user_id;
    }

    public void setTarget_user_id(int target_user_id) {
        this.target_user_id = target_user_id;
    }

    public String getTarget_login_id() {
        return target_login_id;
    }

    public void setTarget_login_id(String target_login_id) {
        this.target_login_id = target_login_id;
    }

    public boolean isDelete_flg() {
        return delete_flg;
    }

    public void setDelete_flg(boolean delete_flg) {
        this.delete_flg = delete_flg;
    }

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
}
