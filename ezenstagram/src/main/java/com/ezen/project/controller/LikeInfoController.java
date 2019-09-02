package com.ezen.project.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ezen.project.bean.NotifiInfoDTO;
import com.ezen.project.service.NotifiInfoService;

@Controller
public class LikeInfoController {

  Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private NotifiInfoService notifiInfoService;

  @RequestMapping(value = "myNotifiInfo.do")
  public ModelAndView myNotifiInfo(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");

    int target_user_id = Integer.parseInt(request.getParameter("target_user_id"));

    List<NotifiInfoDTO> list = notifiInfoService.selectList(target_user_id);

    if (list != null) {
      mv.addObject("result", 1);
      mv.addObject("data", list);
    } else {
      mv.addObject("result", 0);
    }

    return mv;
  }

  @RequestMapping(value = "followerNotifiInfo.do")
  public ModelAndView followerNotifiInfo(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");

    int act_user_id = Integer.parseInt(request.getParameter("act_user_id"));

    List<NotifiInfoDTO> list = notifiInfoService.selectListForFInfo(act_user_id);

    if (list != null) {
      mv.addObject("result", 1);
      mv.addObject("data", list);
    } else {
      mv.addObject("result", 0);
    }

    return mv;
  }

  @RequestMapping(value = "selectNewCnt.do")
  public ModelAndView selectNewCnt(HttpServletRequest request) throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");

    int target_user_id = Integer.parseInt(request.getParameter("target_user_id"));
    mv.addObject("result", notifiInfoService.selectNewCnt(target_user_id));
    return mv;
  }
}
