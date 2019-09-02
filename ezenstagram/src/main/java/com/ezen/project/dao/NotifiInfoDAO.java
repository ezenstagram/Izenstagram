package com.ezen.project.dao;

import java.util.List;
import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import com.ezen.project.bean.NotifiInfoDTO;
import com.ezen.project.bean.NotificationDTO;

@Repository
public class NotifiInfoDAO {

  @Resource
  private SqlSessionTemplate sqlSessionTemplate;

  public List<NotifiInfoDTO> selectList(int target_user_id) {
    return sqlSessionTemplate.selectList("mybatis.notifiInfoMapping.selectList", target_user_id);
  }

  public List<NotifiInfoDTO> selectListForFInfo(int act_user_id) {
    return sqlSessionTemplate.selectList("mybatis.notifiInfoMapping.selectListForFInfo",
        act_user_id);
  }

  public int insert(NotificationDTO notificationDTO) {
    return sqlSessionTemplate.insert("mybatis.notifiInfoMapping.insertNotification",
        notificationDTO);
  }
  
  public int delete(NotificationDTO notificationDTO) {
    return sqlSessionTemplate.update("mybatis.notifiInfoMapping.deleteNotification",
        notificationDTO);
  }
  
  public int selectNewCnt(int target_user_id) {
    return sqlSessionTemplate.selectOne("mybatis.notifiInfoMapping.selectNewCnt",
        target_user_id);
  }
  
  public int updateNewFlgNotification(int target_user_id) {
    return sqlSessionTemplate.update("mybatis.notifiInfoMapping.updateNewFlgNotification",
        target_user_id);
  }

}
