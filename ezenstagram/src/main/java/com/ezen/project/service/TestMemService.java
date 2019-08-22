package com.ezen.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ezen.project.bean.TestMemDTO;
import com.ezen.project.dao.TestMemDAO;

@Service
public class TestMemService {

  @Autowired
  private TestMemDAO memberDAO;

  public String testSelect() {
    TestMemDTO mem = memberDAO.selectOne("hong");
    return mem.getName();
  }
}
