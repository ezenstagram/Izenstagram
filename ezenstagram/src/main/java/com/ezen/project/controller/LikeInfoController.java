package com.ezen.project.controller;

import java.awt.font.TextMeasurer;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ezen.project.bean.TestMemDTO;
import com.ezen.project.service.TestMemService;

@Controller
public class LikeInfoController {

  Logger log = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  private TestMemService testMemService;

  @RequestMapping(value = "test.do")
  public ModelAndView test() throws Exception {
    ModelAndView mv = new ModelAndView("jsonView");
    List<TestMemDTO> list = new ArrayList<TestMemDTO>();
    
    TestMemDTO testMemDTO = new TestMemDTO();
    testMemDTO.setName("박길동");
    list.add(testMemDTO);
    
    TestMemDTO testMemDTO2 = new TestMemDTO();
    testMemDTO2.setName("호길동");
    list.add(testMemDTO2);
    
    mv.addObject("testData", testMemService.testSelect());
    mv.addObject("aaa", 1111);

    mv.addObject("data",list);
    log.debug("로그찍기는 이걸루");
    return mv;
  }
}
