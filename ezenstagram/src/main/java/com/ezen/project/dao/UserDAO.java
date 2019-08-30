package com.ezen.project.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.ezen.project.bean.UserDTO;

@Repository
public class UserDAO {

	@Resource
	private SqlSessionTemplate sqlSession;
	
	public UserDTO userLogin(String login_id, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginId", login_id);
		map.put("password", password);
		 
		return sqlSession.selectOne("mybatis.memberMapping.userLogin", map);
	}
//	public int loginTotal(String login_id, String password) {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("loginId", login_id);
//		map.put("password", password);
//		
//		return sqlSession.selectOne("mybatis.userMapping.total", map);
//	}
	public int userJoin(UserDTO userDTO) {
		return sqlSession.insert("mybatis.memberMapping.userJoin", userDTO);
	}
	public List<String> find_loginId(String tel) {
		return sqlSession.selectList("mybatis.memberMapping.find_loginId", tel);
	}
	public int change_password(String login_id, String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("login_id", login_id);
		map.put("password", password);
		
		return sqlSession.update("mybatis.memberMapping.change_password", map);
	}
	public UserDTO user_profile(int user_id) {
		return sqlSession.selectOne("mybatis.memberMapping.user_profile", user_id);
	}
	public int follow(int user_id, int follow_user_id) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("user_id", user_id);
		map.put("follow_user_id", follow_user_id);
		return sqlSession.insert("mybatis.memberMapping.followUser", map);
	}
	public int emailOX(String email) {
		return sqlSession.selectOne("mybatis.memberMapping.emailOX", email);
	}
	public int login_idOX(String login_id) {
		return sqlSession.selectOne("mybatis.memberMapping.login_idOX", login_id);
	}
	public int find_user_id(String login_id) {
		return sqlSession.selectOne("mybatis.memberMapping.find_user_id", login_id);
	}
	public String emailForFind(String email) {
		return sqlSession.selectOne("mybatis.memberMapping.emailForFind", email);
	}
	public UserDTO selectOne(int user_id) {
		return sqlSession.selectOne("mybatis.memberMapping.selectOne", user_id);
	}
	public int changeProfile(UserDTO userDTO) {
		return sqlSession.update("mybatis.memberMapping.changeProfile", userDTO);
	}

	public int followRelaConfirm(int user_id, int user_id_owner) {		// user_id_owner가 로그인 한 사람
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("user_id", user_id_owner);
		map.put("follow_user_id", user_id);
		return  sqlSession.selectOne("mybatis.memberMapping.followRelaConfirm", map);
	}
	public int follow(int user_id, int user_id_owner, int sign) {		// user_id_owner가 로그인 한 사람
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("user_id", user_id_owner);
		map.put("follow_user_id", user_id);
		

//		System.out.println("user_id_owner = "+user_id_owner);
//		System.out.println("user_id = "+user_id);
//		System.out.println("sign = "+sign);
		int result = 0;
		if(sign == 0) {
			result = sqlSession.insert("mybatis.memberMapping.follow", map);

//			System.out.println("user_id_owner = "+user_id_owner);
		} else {
			result = sqlSession.delete("mybatis.memberMapping.unfollow", map);

		}
		return  result;
	}
	public List<UserDTO> followerList(int user_id) {
		return sqlSession.selectList("mybatis.memberMapping.followerList", user_id);
	}
	public List<UserDTO> followingList(int user_id) {
		return sqlSession.selectList("mybatis.memberMapping.followingList", user_id);
	}
} 
