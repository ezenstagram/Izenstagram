package com.ezen.project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ezen.project.bean.NotifiInfoDTO;
import com.ezen.project.dao.NotifiInfoDAO;

@Service
public class NotifiInfoService {
  @Autowired
  private NotifiInfoDAO notifiInfoDAO;

  public List<NotifiInfoDTO> selectList(int target_user_id) {
    return notifiInfoDAO.selectList(target_user_id);
  }
}
