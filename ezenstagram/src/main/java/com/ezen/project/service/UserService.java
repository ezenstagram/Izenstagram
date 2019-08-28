package com.ezen.project.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.ezen.project.bean.UserDTO;
import com.ezen.project.dao.UserDAO;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;
	
	public UserDTO userLogin(String login_id, String password) {
//		UserDTO userDTO = userDAO.userLogin(login_id, password);
//		int user_id = 0;
//		if(userDTO != null) {
//			user_id = userDTO.getUser_id();
//		}
		return userDAO.userLogin(login_id, password);
	}
//	public int loginTotal(String login_id, String password) {
//		return userDAO.loginTotal(login_id, password);
//	}
	public int userJoin(UserDTO userDTO) {
		return userDAO.userJoin(userDTO);
	}
	
	public List<String> find_loginId(String tel) {
		return userDAO.find_loginId(tel);
	}
//	public int find_password(String login_id, String tel) {
//		return userDAO.find_password(login_id, tel);
//	}
	public int change_password(String login_id, String password) {
		return userDAO.change_password(login_id, password);
	}
	public UserDTO user_profile(int user_id) {
		return userDAO.user_profile(user_id);
	}
	public int follow(int user_id, int follow_user_id) {
		return userDAO.follow(user_id, follow_user_id);
	}
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

		return login_id;
	}
	public UserDTO selectOne(int string) {
		return userDAO.selectOne(string);
	}
	
	public int changeProfile(UserDTO userDTO, String filename, InputStream inputStream, String realpath) {
		int result = 0;
		int user_id = userDTO.getUser_id();
		result = userDAO.changeProfile(userDTO);
		
		if(result > 0) {
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
		
		return result;

	}
}
