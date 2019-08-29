package com.ezen.project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.project.bean.FollowList;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.bean.UserDTO;
import com.ezen.project.service.PostService;
import com.ezen.project.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private PostService postService;
	
	// 濡쒓렇�씤
	@RequestMapping(value="user_login.do")		
	public ModelAndView user_login(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String login_id = request.getParameter("login_id");
		String password = request.getParameter("password");
		
		int result = 0;
		UserDTO userDTO = userService.userLogin(login_id, password);
		
		if(userDTO != null) {
			result = 1;
			mv.addObject("user_id", userDTO.getUser_id());
			mv.addObject("login_id", userDTO.getLogin_id());
		}
		
		mv.addObject("result", result);
		
		return mv;
	}
	// �쉶�썝媛��엯
	@RequestMapping(value="user_join.do")
	public ModelAndView user_join(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String login_id = request.getParameter("login_id");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String tel = request.getParameter("tel");
		String email = request.getParameter("email");
		
		UserDTO userDTO = new UserDTO();
		userDTO.setLogin_id(login_id);
		userDTO.setPassword(password);
		userDTO.setName(name);
		userDTO.setTel(tel);
		userDTO.setEmail(email);
		int result = userService.userJoin(userDTO);
		
		mv.addObject("result", result);
		
		return mv;
	}
	
	
	@RequestMapping(value="change_password.do")
	public ModelAndView change_password(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String login_id = request.getParameter("login_id");
		String password = request.getParameter("password");
		System.out.println("password ="+password);
		System.out.println("login_id ="+login_id);
		
		int result = 0;
		result = userService.change_password(login_id, password);
		if(result > 0) {
			result = 1;
		}

		mv.addObject("result", result);
		
		return mv;
	}
	
	@RequestMapping(value="InfoPresence.do") 				
	public ModelAndView InfoPresence(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String email = request.getParameter("email");
		String login_id = request.getParameter("login_id");
		int count = 0;
		if(email != null) {
			count = userService.emailOX(email);
		} else {
			count = userService.login_idOX(login_id);
		}
		int result=0;
		if(count > 0) {
			result = 1;
			
		}
		mv.addObject("result", result);
		
		return mv;
	}
	
	@RequestMapping(value="find_userId.do") 							
	public ModelAndView find_userId(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String login_id = request.getParameter("login_id");

		int user_id = 0;
		user_id =  userService.find_user_id(login_id);		
		
		int result=0;
		if(user_id > 0) {
			result = 1;
			mv.addObject("user_id", user_id);
		} 
		
		mv.addObject("result", result);
		
		return mv;
	}
	
	@RequestMapping(value="User_id_emailandtel.do") 
	public ModelAndView User_id_emailandtel(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String email = request.getParameter("email");
		String tel = request.getParameter("tel");
		
		String login_id;
		int result=0;
		List<String> list=null;
		int div=2;		// �쟾�솕踰덊샎吏� �씠硫붿씪�씤吏� �븞�뱶濡쒖씠�뱶�뿉�꽌 援щ텇
		
		if(email != null) {
			login_id = userService.emailForFind(email);
			div = 0;
			if(login_id != null) {
				result = 1;
				mv.addObject("login_id", login_id);
			}
		} else {
			list = userService.find_loginId(tel);
			div=1;
			if(list.size()>0) {
				result = list.size();
				mv.addObject("list", list);
				
			}
		}
	
		mv.addObject("result", result);
		mv.addObject("div", div);
		
		return mv;
	}
	@RequestMapping(value="user_profileInfo.do") 
	public ModelAndView user_profileInfo(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		
		UserDTO userDTO = userService.selectOne(user_id);
		
	
		mv.addObject("userDTO", userDTO);
		
		return mv;
	}
	@RequestMapping(value="emailAndLogin_id.do") 
	public ModelAndView emailAndLogin_id(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		String email = request.getParameter("email");
		String login_id = request.getParameter("login_id");
		
		int div= 2;
		int result = 0;
		if(email != null) {
			result = userService.emailOX(email);
			mv.addObject("email", email);
			div = 0;
		} else {
			result = userService.login_idOX(login_id);
			mv.addObject("login_id", login_id);
			div = 1;
		}
		

		mv.addObject("result", result);
		mv.addObject("div", div);
		
		return mv;
	}
	@RequestMapping(value="changeProfile.do") 
	public ModelAndView changeProfile(MultipartFile photopath, HttpServletRequest request) throws IOException {
		ModelAndView mv = new ModelAndView("jsonView");
	
		if(photopath == null) {
			System.out.println("photopath");
		}
		
		String name = request.getParameter("name");
		String login_id = request.getParameter("login_id");
		String website = request.getParameter("website");
		String introduction = request.getParameter("introduction");
		String tel = request.getParameter("tel");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		String profile_photo = request.getParameter("profile_photo");
		
		UserDTO userDTO = new UserDTO();
		userDTO.setName(name);
		userDTO.setLogin_id(login_id);
		userDTO.setWebsite(website);
		userDTO.setIntroduction(introduction);
		userDTO.setTel(tel);
		userDTO.setEmail(email);
		userDTO.setGender(gender);
		userDTO.setUser_id(user_id);
		userDTO.setProfile_photo(profile_photo);
		
		String realpath = "Z:";
		int result =0;
		
		if(photopath!= null) {
			String fname= photopath.getOriginalFilename();
			result = userService.changeProfile(userDTO, fname, photopath.getInputStream(), realpath);
		} else {
			result = userService.changeProfileNoPhoto(userDTO);
		}
		
		mv.addObject("result", result);
		
		return mv;
	}
	@RequestMapping(value="followReal") 
	public ModelAndView followReal(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		int user_id_owner = Integer.parseInt(request.getParameter("user_id_owner")); 

		int result = userService.followRelaConfirm(user_id, user_id_owner);
		
		mv.addObject("result", result);
	
		return mv;
	}
	@RequestMapping(value="follow.do") 
	public ModelAndView follow(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		int user_id_owner = Integer.parseInt(request.getParameter("user_id_owner"));
		int sign = Integer.parseInt(request.getParameter("sign"));	// sign이 0이면 팔로우, sing이 1이면 팔로우 취소
		int result = userService.follow(user_id, user_id_owner, sign);
		
		mv.addObject("result", result);
		mv.addObject("sign", sign);
		return mv;
	}
	@RequestMapping(value="followerList.do") 
	public ModelAndView followerList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int sepa = Integer.parseInt(request.getParameter("sepa"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		List<FollowList> list = userService.followerList(user_id, sepa); 
		
		
		int result = 0;
		if(list != null) {
			result = list.size();
			mv.addObject("list", list);
		} else {
			result = 0;
		}
	
		mv.addObject("result", result);
		return mv;
		
	}
}
