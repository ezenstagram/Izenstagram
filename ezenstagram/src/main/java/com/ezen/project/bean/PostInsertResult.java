package com.ezen.project.bean;

public class PostInsertResult {
	int post_id;
	String content;
	String result;

	public PostInsertResult(int post_id, String result, String content) {
		// TODO Auto-generated constructor stub
		this.post_id = post_id;
		this.result = result;
		this.content = content;
		
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}	
	
}
