package com.ezen.project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ezen.project.bean.CommentsDTO;
import com.ezen.project.bean.NotifiInfoDTO;
import com.ezen.project.bean.NotificationDTO;
import com.ezen.project.dao.FeedDAO;
import com.ezen.project.dao.NotifiInfoDAO;
import com.ezen.project.dao.PostDAO;

@Service
public class NotifiInfoService {
  @Autowired
  private NotifiInfoDAO notifiInfoDAO;
  @Autowired
  private FeedDAO feedDAO;
  @Autowired
  private PostDAO postDAO;

  public List<NotifiInfoDTO> selectList(int target_user_id) {
    List<NotifiInfoDTO> list = notifiInfoDAO.selectList(target_user_id);
    String url = "http://192.168.0.13:8080/image/storage/";
    for (NotifiInfoDTO notifiInfoDTO : list) {
      notifiInfoDTO.setPost_image(url + notifiInfoDTO.getPost_image());
      notifiInfoDTO.setProfile_photo(url + notifiInfoDTO.getProfile_photo());
    }
    return list;
  }

  public List<NotifiInfoDTO> selectListForFInfo(int act_user_id) {
    List<NotifiInfoDTO> list = notifiInfoDAO.selectListForFInfo(act_user_id);
    String url = "http://192.168.0.13:8080/image/storage/";
    for (NotifiInfoDTO notifiInfoDTO : list) {
      notifiInfoDTO.setPost_image(url + notifiInfoDTO.getPost_image());
      notifiInfoDTO.setProfile_photo(url + notifiInfoDTO.getProfile_photo());
    }
    return list;
  }

  public int insert(int mode_id, Integer target_id, int act_user_id, String comment_cmt) {
    NotificationDTO notificationDTO = makeDTO(mode_id, target_id, act_user_id, comment_cmt);
    return notifiInfoDAO.insert(notificationDTO);
  }
  public int delete(int mode_id, Integer target_id, int act_user_id, String comment_cmt) {
    NotificationDTO notificationDTO = makeDTO(mode_id, target_id, act_user_id, comment_cmt);
    return notifiInfoDAO.delete(notificationDTO);
  }
  
  private NotificationDTO makeDTO(int mode_id, Integer target_id, int act_user_id, String comment_cmt) {
    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setMode_id(mode_id);
    notificationDTO.setTarget_id(target_id);
    CommentsDTO commentsDTO = new CommentsDTO();
    commentsDTO.setPost_id(target_id);
    commentsDTO.setUser_id(act_user_id);
    commentsDTO.setComment_cmt(comment_cmt);
    if(mode_id == 2) {
      int target_sub_id = feedDAO.getCommentId(commentsDTO);
      notificationDTO.setTarget_sub_id(target_sub_id);
    }
    notificationDTO.setAct_user_id(act_user_id);
    int target_user_id = (mode_id != 3) ? postDAO.getPostWriter(target_id) : target_id;
    notificationDTO.setTarget_user_id(target_user_id);
    return notificationDTO;
  }
}
