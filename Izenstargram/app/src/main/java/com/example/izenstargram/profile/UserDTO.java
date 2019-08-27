package com.example.izenstargram.profile;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private int user_id;					// 소스 안에서만 사용하는 id
    private String login_id;				// 로그인 할때 사용하는 id & 사용자 이름. 한글 입력 불가
    private String name;					// 이름. 한글 입력 가능
    private String password;				// 비밀번호
    private String profile_photo;			// 프로필 사진
    private String website;					// 웹 사이트
    private String introduction;			// 자기소개
    private String email;					// 이메일
    private String tel;				 		// 전화
    private String gender;					// 성별

    public UserDTO() {
    }

    public UserDTO(int user_id, String login_id, String name, String password, String profile_photo, String website, String introduction, String email, String tel, String gender) {
        this.user_id = user_id;
        this.login_id = login_id;
        this.name = name;
        this.password = password;
        this.profile_photo = profile_photo;
        this.website = website;
        this.introduction = introduction;
        this.email = email;
        this.tel = tel;
        this.gender = gender;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
