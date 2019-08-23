package com.ezen.project.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.project.bean.CommentsDTO;
import com.ezen.project.bean.LikesDTO;
import com.ezen.project.bean.PostAllDTO;
import com.ezen.project.bean.PostDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.service.FeedService;

@Controller
public class FeedController {
	@Autowired
	private FeedService feedService;

	@RequestMapping(value = "chkLikes.do")
	public ModelAndView chkLikes(HttpServletRequest request) throws Exception {

		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", feedService.chkLikes(post_id, user_id));
		return mv;
	}

	@RequestMapping(value = "saveLikes.do")
	public ModelAndView saveLikes(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		LikesDTO likesDTO = new LikesDTO();
		likesDTO.setPost_id(post_id);
		likesDTO.setUser_id(user_id);
		mv.addObject("result", feedService.saveLikes(likesDTO));
		return mv;
	}

	@RequestMapping(value = "delLikes.do")
	public ModelAndView delLikes(HttpServletRequest request) throws Exception {
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("result", feedService.delLikes(post_id, user_id));
		return mv;
	}

	@RequestMapping(value = "saveCmts.do")
	public ModelAndView saveCmts(HttpServletRequest request) throws Exception {
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		String comment_cmt = request.getParameter("comment_cmt");
		CommentsDTO commentsDTO = new CommentsDTO();
		commentsDTO.setPost_id(post_id);
		commentsDTO.setUser_id(user_id);
		commentsDTO.setComment_cmt(comment_cmt);
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", feedService.saveCmts(commentsDTO));
		return mv;
	}

	@RequestMapping(value = "cmtList.do")
	public ModelAndView cmtList(HttpServletRequest request) throws Exception {
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		List<CommentsDTO> cmtList = feedService.cmtList(post_id);
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", cmtList);
		return mv;
	}

	@RequestMapping(value = "delCmts.do")
	public ModelAndView delCmts(HttpServletRequest request) throws Exception {
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		int comment_id = Integer.parseInt(request.getParameter("comment_id"));
		CommentsDTO commentsDTO = new CommentsDTO();
		commentsDTO.setPost_id(post_id);
		commentsDTO.setUser_id(user_id);
		commentsDTO.setComment_id(comment_id);
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", feedService.delCmts(commentsDTO));
		return mv;
	}

	@RequestMapping(value = "updateCmts.do")
	public ModelAndView updateCmts(HttpServletRequest request) throws Exception {
		int post_id = Integer.parseInt(request.getParameter("post_id"));
		int user_id = Integer.parseInt(request.getParameter("user_id"));
		int comment_id = Integer.parseInt(request.getParameter("comment_id"));
		String comment_cmt = request.getParameter("comment_cmt");
		CommentsDTO commentsDTO = new CommentsDTO();
		commentsDTO.setPost_id(post_id);
		commentsDTO.setUser_id(user_id);
		commentsDTO.setComment_id(comment_id);
		commentsDTO.setComment_cmt(comment_cmt);
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", feedService.updateCmts(commentsDTO));
		return mv;
	}

	@RequestMapping(value = "feedPostList.do")
	public ModelAndView feedPostList(HttpServletRequest request) throws Exception {
		int user_id = Integer.parseInt(request.getParameter("user_id"));

		PostDTO postDTO = new PostDTO();
		postDTO.setUser_id(user_id);
		List<PostAllDTO> postList = feedService.feedPostList(user_id);
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", postList);
		return mv;
	}

	@RequestMapping(value = "feedPostImageList.do")
	public ModelAndView getPostImgData(HttpServletRequest request) throws Exception {
		int post_id = Integer.parseInt(request.getParameter("post_id"));

		PostImageDTO postImageDTO = new PostImageDTO();
		postImageDTO.setPost_id(post_id);
		List<PostImageDTO> postIamgeList = feedService.feedPostImageList(post_id);
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", postIamgeList);
		return mv;
	}

	@RequestMapping(value = "getCmtData.do")
	public ModelAndView getCmtData(HttpServletRequest request) throws Exception {
		int post_id = Integer.parseInt(request.getParameter("post_id"));

		CommentsDTO commentsDTO = new CommentsDTO();
		commentsDTO.setPost_id(post_id);
		List<CommentsDTO> commentList = feedService.getCmtData(post_id);
		ModelAndView mv = new ModelAndView("jsonView");
		mv.addObject("data", commentList);
		return mv;
	}

}
