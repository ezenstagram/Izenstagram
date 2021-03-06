package com.ezen.project.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.ezen.project.bean.FollowList;
import com.ezen.project.bean.UserDTO;
import com.ezen.project.dao.UserDAO;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	
	public UserDTO userLogin(String login_id, String password) {
		return userDAO.userLogin(login_id, password);
	}

	public int userJoin(UserDTO userDTO) {
		return userDAO.userJoin(userDTO);
	}
	
	public List<String> find_loginId(String tel) {
		return userDAO.find_loginId(tel);
	}
	public int change_password(String login_id, String password) {
		return userDAO.change_password(login_id, password);
	}
	public UserDTO user_profile(int user_id) {
		return userDAO.user_profile(user_id);
	}
//	public int follow(int user_id, int follow_user_id) {
//		return userDAO.follow(user_id, follow_user_id);
//	}
	public int emailOX(String email) {
		return userDAO.emailOX(email);
	}
	public int login_idOX(String login_id) {
		return userDAO.login_idOX(login_id);
	}
	public int find_user_id(String login_id) {
		return userDAO.find_user_id(login_id);
	}

	public String emailForFind(String email) {
		String login_id = null;
		login_id = userDAO.emailForFind(email);
		System.out.println("login_id1"+login_id);
		return login_id;
	}
	public UserDTO selectOne(int string) {
		return userDAO.selectOne(string);
	}
	
	public int changeProfile(UserDTO userDTO, String filename, InputStream inputStream, String realpath) {
		int result = userDAO.changeProfile(userDTO);
	
		if(result > 0) {
			if(filename != null) {
				String image_url = realpath+"/"+filename;
				File file = new File(image_url);
				try {
					FileCopyUtils.copy(inputStream, new FileOutputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;

	}
	public int changeProfileNoPhoto(UserDTO userDTO) {
		
		return userDAO.changeProfile(userDTO);
	}
	
	
	public int followRelaConfirm(int user_id, int user_id_owner) {	
		return  userDAO.followRelaConfirm(user_id, user_id_owner);
	}
	public int follow(int user_id, int user_id_owner, int sign) {		// user_id_owner가 로그인 한 사람
		return  userDAO.follow(user_id, user_id_owner, sign);
	}
	public List<FollowList> followerList(int user_id, int sepa) {
		List<UserDTO> list = new ArrayList<UserDTO>();
		if(sepa == 0) {
			list = userDAO.followerList(user_id);		
		} else {
			list = userDAO.followingList(user_id);
		}
		List<FollowList> follow= new ArrayList<FollowList>();
		
		if(list != null) {
			UserDTO userDTO = null;
			FollowList followList = null;
			
			for(int i=0; i<list.size(); i++) {
				boolean status_follow = false;
				userDTO = list.get(i);
				followList = new FollowList();
				int status = 0;
				
				if(sepa == 0) {
					status = userDAO.followRelaConfirm(userDTO.getUser_id(), user_id);
				} else {
					status = 1;
				}
				if(status > 0) {
					System.out.println("if=" +status + "=status");
					status_follow = true;
					status = 0;
				}
				
				followList.setFollowStatus(status_follow);
				followList.setName(userDTO.getName());
				followList.setProfile_photo(userDTO.getProfile_photo());
				followList.setUser_id(userDTO.getUser_id());
				followList.setLogin_id(userDTO.getLogin_id());
				follow.add(followList);
			}
		}
		return follow;
	}
}
