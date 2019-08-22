package com.ezen.project.dao;

import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import com.ezen.project.bean.TestMemDTO;

@Repository
public class TestMemDAO {

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  public TestMemDTO selectOne(String id) {

    return sqlSessionTemplate.selectOne("mybatis.memberMapping.selectOne", id);
  }

}
