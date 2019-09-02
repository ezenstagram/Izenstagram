package com.ezen.project.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.ezen.project.bean.AllTagDTO;
import com.ezen.project.bean.LinkedTagDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.bean.UserDTO;
import com.ezen.project.bean.UserTagDTO;

@Repository
public class TagDAO {
  @Autowired
  private SqlSessionTemplate sqlSessionTemplate;

  public int isExistTag(String tag_name) {
    return sqlSessionTemplate.selectOne("mybatis.tagMapping.isExistTag", tag_name);
  }
  
  public Integer getTagIdByName(String tag_name) {
    return sqlSessionTemplate.selectOne("mybatis.tagMapping.getTagIdByName", tag_name);
  }

  public int addToTagList(AllTagDTO allTagDTO) {
    return sqlSessionTemplate.insert("mybatis.tagMapping.addToTagList", allTagDTO);
  }

  public int insertLinkedTag(int post_id, int tag_id) {
    Map<String, Integer> map = new HashMap<String, Integer>();
    map.put("post_id", post_id);
    map.put("tag_id", tag_id);
    return sqlSessionTemplate.insert("mybatis.tagMapping.insertLinkedTag", map);
  }

 // public int findUserIdByLoginId(String login_id) {
    public Integer findUserIdByLoginId(String login_id) {
    return sqlSessionTemplate.selectOne("mybatis.tagMapping.findUserIdByLoginId", login_id);
  }
  
  public int insertUserTag(int post_id, int user_id) {
    Map<String, Integer> map = new HashMap<String, Integer>();
    map.put("post_id", post_id);
    map.put("user_id", user_id);
    return sqlSessionTemplate.insert("mybatis.tagMapping.insertUserTag", map);
  }

  public List<AllTagDTO> allTagList() {
    return sqlSessionTemplate.selectList("mybatis.tagMapping.allTagList");
  }

  public List<LinkedTagDTO> taggedLinkList(int post_id) {
    return sqlSessionTemplate.selectList("mybatis.tagMapping.taggedLinkList", post_id);
  }

  public List<UserTagDTO> taggedUserList(int post_id) {
    return sqlSessionTemplate.selectList("mybatis.tagMapping.taggedUserList", post_id);
  }
  
  public List<AllTagDTO> tagListByLetter(String letter_to_search){
    return sqlSessionTemplate.selectList("mybatis.tagMapping.tagListByLetter", letter_to_search); 
  }
  
//  public List<LinkedTagDTO> selectPostIdByTagId(int tag_id){
//    return sqlSessionTemplate.selectList("mybatis.tagMapping.selectPostIdByTagId", tag_id);
//  }
  
  // 다시...
  public List<PostImageDTO> selectPostImageByTagId(int tag_id){
	  return sqlSessionTemplate.selectList("mybatis.tagMapping.selectPostImageByTagId", tag_id);
  }
  // 다시....
  
  
  public List<UserDTO> selectUserBySearch(String letter_to_search){
    return sqlSessionTemplate.selectList("mybatis.tagMapping.selectUserBySearch", letter_to_search);
  }
  
  public List<PostImageDTO> selectPostImageRandom(){
    return sqlSessionTemplate.selectList("mybatis.tagMapping.selectPostImageRandom");
  }
  
}
