package com.ezen.project.bean;

public class CommentsDTO {
	private int post_id; // �Խù� ��ȣ
	private int user_id; // �������̵�
	private int comment_id; // ��۹�ȣ
	private String comment_cmt; // ��۳���
	private String reg_date; // �ۼ�����
    private UserDTO userDTO; //��� �� ���� ���� 
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

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public String getComment_cmt() {
		return comment_cmt;
	}

	public void setComment_cmt(String comment_cmt) {
		this.comment_cmt = comment_cmt;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
	
}
