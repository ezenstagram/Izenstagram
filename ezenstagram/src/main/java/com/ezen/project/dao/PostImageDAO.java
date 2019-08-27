package com.ezen.project.dao;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ezen.project.bean.PostImageDTO;

@Repository
public class PostImageDAO {
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int insertPostImage(PostImageDTO postImageDTO) {
		System.out.println("image_url: " + postImageDTO.getImage_url());
		System.out.println("image_id : " +postImageDTO.getImage_id());
		System.out.println("post_id: " +postImageDTO.getPost_id());
		return sqlSessionTemplate.insert("mybatis.postImageMapping.insertPostImage",postImageDTO);
	}
	public List<PostImageDTO> profilePostRefImage(int user_id) {
		return sqlSessionTemplate.selectList("mybatis.postImageMapping.PostsRefImage",user_id);
	}
}
