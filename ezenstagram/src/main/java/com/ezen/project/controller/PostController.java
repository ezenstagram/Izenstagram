package com.ezen.project.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.project.bean.PostDTO;
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

		// DB 泥섎━
		PostDTO postDTO = new PostDTO();
		postDTO.setUser_id(user_id);
		postDTO.setContent(content);
		postDTO.setLocation(location);
		postDTO.setReg_date(reg_date);

		String result = postService.insert(postDTO, img.getOriginalFilename(), img.getInputStream());

		ModelAndView mv = new ModelAndView("jsonView"); //// �쁾�쁾 瑗� jsonView濡� �빐以섑뼆�븿
		mv.addObject("result", result);
		return mv;

	}

}
