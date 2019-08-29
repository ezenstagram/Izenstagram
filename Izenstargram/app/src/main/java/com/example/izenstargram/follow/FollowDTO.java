package com.example.izenstargram.follow;

public class FollowDTO {
    private boolean followStatus;
    private int user_id;
    private String name;
    private String profile_photo;
    private String login_id;

    public FollowDTO(boolean followStatus, int user_id, String name, String profile_photo, String login_id) {
        this.followStatus = followStatus;
        this.user_id = user_id;
        this.name = name;
        this.profile_photo = profile_photo;
        this.login_id = login_id;
    }

    public FollowDTO() {
    }

    public boolean isFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(boolean followStatus) {
        this.followStatus = followStatus;
    }

    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getProfile_photo() {
        return profile_photo;
    }
    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }
}
