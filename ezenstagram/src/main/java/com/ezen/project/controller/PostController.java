package com.ezen.project.controller;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.project.bean.PostDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.service.PostService;

@Controller
public class PostController {
	@Autowired
	private PostService postService;

	@RequestMapping(value = "post.do", method = RequestMethod.POST)
	public ModelAndView postInsert(MultipartFile img, HttpServletRequest request) throws Exception {
		
		String content = request.getParameter("content");
		System.out.println("content :" +content);
		String location = request.getParameter("location");
		String reg_date = request.getParameter("reg_date");
		int user_id = Integer.parseInt(request.getParameter("user_id"));

		// DB 저장
		PostDTO postDTO = new PostDTO();
		postDTO.setUser_id(user_id);
		postDTO.setContent(content);
		postDTO.setLocation(location);
		postDTO.setReg_date(reg_date);
		
		//파일 저장용 함수들을 service에 보내기
		String realPath="Z:";
		String result = postService.insert(postDTO, img.getOriginalFilename(), img.getInputStream(),realPath);
		
		ModelAndView mv = new ModelAndView("jsonView"); //// 꼭 jsonView 로 해주기
		mv.addObject("result", result);
		return mv;

	}
	@RequestMapping(value="userProfileRefPost.do") 
	public ModelAndView userProfileRefPost(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		int result = 0;
		
		List<PostImageDTO> list = postService.profilePostRefImage(user_id);
		
		if(list.size()>0) {
			result = list.size();
			mv.addObject("list", list);
		}
		mv.addObject("result", result);
		
		return mv;
	}
	@RequestMapping(value="profileInfo") 
	public ModelAndView profileInfo(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		
		int following = postService.following(user_id);
		int follower = postService.follower(user_id);
		int postCount = postService.postCount(user_id);
		
		mv.addObject("postCount", postCount);
		mv.addObject("follower", follower);
		mv.addObject("following", following);
		
		return mv;
	}
}
