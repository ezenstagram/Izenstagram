package com.ezen.project.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ezen.project.bean.PostDTO;

@Repository
public class PostDAO {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	public int insertPost(PostDTO postDTO) {
		return sqlSessionTemplate.insert("mybatis.postMapping.insertPost",postDTO);
	}
	public int following(int user_id) {
		return sqlSessionTemplate.selectOne("mybatis.postMapping.following",user_id);
	}
	public int follower(int user_id) {
		return sqlSessionTemplate.selectOne("mybatis.postMapping.follower", user_id);
	}
	public int getcurrPost_id() {
		return sqlSessionTemplate.selectOne("mybatis.postMapping.getcurrPost_id");
	}
	public int deletePost(int post_id) {
		return sqlSessionTemplate.delete("mybatis.postMapping.deletePost",post_id);
	}
	public int postCount(int user_id) {
		return sqlSessionTemplate.selectOne("mybatis.postMapping.postCount", user_id);
	}

}
