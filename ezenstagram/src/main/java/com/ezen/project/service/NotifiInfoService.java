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
    List<NotifiInfoDTO> list = notifiInfoDAO.selectList(target_user_id);
    String url = "http://192.168.0.13:8080/image/storage/";
    for (NotifiInfoDTO notifiInfoDTO : list) {
      notifiInfoDTO.setPost_image(url + notifiInfoDTO.getPost_image());
      notifiInfoDTO.setProfile_photo(url + notifiInfoDTO.getProfile_photo());
    }
    return list;
  }
}
