package com.ezen.project.dao;

import java.util.List;
import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import com.ezen.project.bean.NotifiInfoDTO;
import com.ezen.project.bean.TestMemDTO;

@Repository
public class NotifiInfoDAO {

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  public List<NotifiInfoDTO> selectList(int target_user_id) {

    return sqlSessionTemplate.selectList("mybatis.notifiInfoMapping.selectList", target_user_id);
  }

}
