package com.ezen.project.bean;

public class LikesDTO {
	private int post_id; // �Խù� ��ȣ
	private int user_id; // �������̵�
	private String reg_date; // �ۼ�����

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

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
}
