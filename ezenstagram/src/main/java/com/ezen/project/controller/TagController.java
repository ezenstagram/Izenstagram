package com.ezen.project.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.ezen.project.bean.AllTagDTO;
import com.ezen.project.bean.LinkedTagDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.bean.UserDTO;
import com.ezen.project.bean.UserTagDTO;
import com.ezen.project.service.TagService;


@Controller
public class TagController {
  Logger log = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private TagService tagService;

  // (@게시글 작성시) 해시태그 입력이 있을 경우 새 태그인지 헌 태그인지 검사
  // 새 태그면 : post_all_hashtag 테이블에 저장 후 post_linked_hashtag 에 데이터 저장
  // 헌 태그면 : post_linked_hastag 테이블에 데이터 저장
  @RequestMapping(value = "putTagIntoPost.do", method = RequestMethod.POST)
  public ModelAndView putTagsIntoPost(HttpServletRequest request) {
    try {
      request.setCharacterEncoding("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    ModelAndView mv = new ModelAndView("jsonView");
    int post_id = Integer.parseInt(request.getParameter("post_id"));
    String fullContent = (String) request.getParameter("fullContent");
    int result = tagService.registerTag(post_id, fullContent);
    mv.addObject("result", result);
    return mv;
  }

  // (@게시글 작성시) @태그 입력이 있을 경우
  // 해당단어로 @유저태그가 등록되어 있다면 post_user_tag 에 데이터 저장
  @RequestMapping(value = "putUserTagIntoPost.do", method = RequestMethod.POST)
  public ModelAndView putUserTagIntoPost(HttpServletRequest request) {
    try {
      request.setCharacterEncoding("UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    ModelAndView mv = new ModelAndView("jsonView");
    int post_id = Integer.parseInt(request.getParameter("post_id"));
    String fullContent = (String) request.getParameter("fullContent");
    int result = tagService.registerUserTag(post_id, fullContent);
    mv.addObject("result", result);
    return mv;
  }

  // (@검색) 첫화면:랜덤으로 게시 사진들출력
  @RequestMapping(value = "selectPostImageRandom.do", method = RequestMethod.POST)
  public ModelAndView selectPostImageRandom(HttpServletRequest request) {
    List<PostImageDTO> list = tagService.selectPostImageRandom();
    ModelAndView mv = new ModelAndView("jsonView");
    mv.addObject("data", list);
    return mv;
  }

  // (@검색) '특정 글자'가 포함된 모든 tag_name 출력시키기
  @RequestMapping(value = "selectTagNameByLetter.do", method = RequestMethod.POST)
  public ModelAndView selectTagNameByLetter(HttpServletRequest request) {
    String letter_to_search = request.getParameter("letter_to_search");
    List<AllTagDTO> list = tagService.tagListByLetter(letter_to_search);
    ModelAndView mv = new ModelAndView("jsonView");
    mv.addObject("data", list);
    return mv;
  }

  // (@검색) 특정 #tag_name으로 검색한 경우 해당되는 모든 게시물들 사진출력
  @RequestMapping(value = "selectPostIdByTagName.do", method = RequestMethod.POST)
  public ModelAndView selectPostIdByTagName(HttpServletRequest request) {
    String tag_name = request.getParameter("tag_name");
    ModelAndView mv = new ModelAndView("jsonView");
    Integer integer_tag_id = tagService.getTagIdByName(tag_name);
    int tag_id = 0;
    int result = 0;
    List<LinkedTagDTO> list;
    if (integer_tag_id == null) {
      tag_id = 0;
      mv.addObject("result", result);
    } else {
      tag_id = integer_tag_id;
      list = tagService.selectPostIdByTagId(tag_id);
      mv.addObject("data", list);
    }
    return mv;
  }

  // (@검색) '특정 글자'가 포함된 모든 user_name 출력시키기
  @RequestMapping(value = "selectUserBySearch.do", method = RequestMethod.POST)
  public ModelAndView selectUserBySearch(HttpServletRequest request) {
    String letter_to_search = request.getParameter("letter_to_search");
    List<UserDTO> list = tagService.selectUserBySearch(letter_to_search);
    String imgUrl = null;
    for (int i=0; i<list.size(); i++) {
    	imgUrl="http://192.168.0.13:8080/image/storage/" + list.get(i).getProfile_photo();
    	list.get(i).setProfile_photo(imgUrl);
    }
    ModelAndView mv = new ModelAndView("jsonView");
    mv.addObject("data", list);
    return mv;
  }
}
