package com.ezen.project.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.project.bean.CommentsDTO;
import com.ezen.project.bean.LikesDTO;
import com.ezen.project.bean.PostAllDTO;
import com.ezen.project.bean.PostDTO;
import com.ezen.project.bean.PostImageDTO;

@Repository
public class FeedDAO {
	@Autowired
	private SqlSessionTemplate sqlSession;

	public int chkLikes(int post_id, int user_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("post_id", post_id);
		map.put("user_id", user_id);
		return sqlSession.selectOne("mybatis.feedMapper.chkLikes", map);
	}

	public int saveLikes(LikesDTO likesDTO) {
		return sqlSession.insert("mybatis.feedMapper.saveLikes", likesDTO);
	}

	public int delLikes(int post_id, int user_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("post_id", post_id);
		map.put("user_id", user_id);
		return sqlSession.delete("mybatis.feedMapper.delLikes", map);
	}

	public int saveCmts(CommentsDTO commentsDTO) {
		return sqlSession.insert("mybatis.feedMapper.saveCmts", commentsDTO);
	}

	public List<CommentsDTO> cmtList(int post_id) {
		return sqlSession.selectList("mybatis.feedMapper.cmtList", post_id);
	}

	public int delCmts(CommentsDTO commentsDTO) {
		return sqlSession.delete("mybatis.feedMapper.delCmts", commentsDTO);
	}

	public int updateCmts(CommentsDTO commentsDTO) {
		return sqlSession.update("mybatis.feedMapper.updateCmts", commentsDTO);
	}

	public Integer maxCi(int post_id) {
		return sqlSession.selectOne("mybatis.feedMapper.maxCi", post_id);
	}

	public List<PostAllDTO> feedPostList(int user_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("user_id", user_id);
		map.put("user_id2", user_id);
		return sqlSession.selectList("mybatis.feedMapper.feedPostList", map);
	}

	public List<PostImageDTO> feedPostImageList(int post_id) {
		return sqlSession.selectList("mybatis.feedMapper.feedPostImageList", post_id);
	}

	public List<CommentsDTO> getCmtData(int post_id) {
		return sqlSession.selectList("mybatis.feedMapper.getCmtData", post_id);
	}
	
	public int cntLikes(int post_id) {
		return sqlSession.selectOne("mybatis.feedMapper.maxLikes", post_id);
	}

}
