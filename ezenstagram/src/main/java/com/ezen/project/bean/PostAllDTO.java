package com.ezen.project.bean;

import java.util.List;

public class PostAllDTO {
	private int post_id; // �Խù� ���� ��ȣ
	private int user_id; // usertable ������ user_id �����°�/ �α����� �Ǿ� ���� �ۼ��� �� �����Ƿ�
	private String content;// �ۼ�����
	private String location;// ��ġ
	private String reg_date;// ���ε� ��¥
	private List<PostImageDTO> postImageList;
	private boolean isLike;
	private int comment_cnt;
	private UserDTO userDTO;

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	
	public List<PostImageDTO> getPostImageList() {
		return postImageList;
	}

	public void setPostImageList(List<PostImageDTO> postImageList) {
		this.postImageList = postImageList;
	}

	
	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public int getComment_cnt() {
		return comment_cnt;
	}

	public void setComment_cnt(int comment_cnt) {
		this.comment_cnt = comment_cnt;
	}

}