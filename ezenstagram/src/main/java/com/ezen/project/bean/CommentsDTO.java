package com.ezen.project.bean;

public class CommentsDTO {
	private int post_id; // 게시물 번호
	private int user_id; // 유저아이디
	private int comment_id; // 댓글번호
	private String comment_cmt; // 댓글내용
	private String reg_date; // 작성일자
    private UserDTO userDTO; //댓글 쓴 유저 정보 
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
