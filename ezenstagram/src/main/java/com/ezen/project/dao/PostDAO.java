package com.ezen.project.dao;

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

	public int getcurrPost_id() {
		return sqlSessionTemplate.selectOne("mybatis.postMapping.getcurrPost_id");
	}
	public int deletePost(int post_id) {
		return sqlSessionTemplate.delete("mybatis.postMapping.deletePost",post_id);
	}

}
