package com.ezen.project.bean;

public class FollowDTO {
	private int user_id;  
	private int follow_user_id;  
	private String reg_date;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getFollow_user_id() {
		return follow_user_id;
	}
	public void setFollow_user_id(int follow_user_id) {
		this.follow_user_id = follow_user_id;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	
}
