package com.ezen.project.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ezen.project.bean.AllTagDTO;
import com.ezen.project.bean.LinkedTagDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.bean.UserDTO;
import com.ezen.project.bean.UserTagDTO;
import com.ezen.project.dao.TagDAO;

@Service
public class TagService {

  @Autowired
  private TagDAO tagDAO;

  /** #해시태그 등록 관련 메소드 */
  public int registerTag(int post_id, String fullContent) {
    int result2 = 0;    // 태그저장여부를 리턴(0/1)해서 mv로 출력할 값 
    // 전체 콘텐츠에서 #태그의 단어들만 추출하기
    List<String> tagStringList = new ArrayList<String>();
    System.out.println("fullContent = " + fullContent);
    if (fullContent.contains("#")) {
      String[] tokens = fullContent.substring(fullContent.indexOf("#")).split(" ");
      for (String token : tokens) {
        if (token.contains("#")) {
          System.out.println("token = " + token);
          String[] candidates = token.substring(token.indexOf("#") + 1).split("#");
          for (String candidate : candidates) {
            System.out.println("candidate = " + candidate);
            if (candidate != "" && candidate != null)
              tagStringList.add(candidate.split("@")[0]);
          }
        }
      }
    }
    // 추출된 #태그의 단어가 전체태그리스트에 저장되어있는지 검사하기
    for (int i = 0; i < tagStringList.size(); i++) {
      System.out.println(tagStringList.get(i));

      String tag_name = tagStringList.get(i);
      System.out.println("tag_name = " + tag_name);
      System.out.println(tag_name);
      boolean isExistTag = isExistTag(tag_name);
      int result1 = 0; // 새태그로 저장이 있을 경우 1 리턴
      int tag_id = 0;
      // 입력태그가 새태그인 경우: all_hashtag 테이블에 저장 후 해당 tag_id 받아오기
      if (!isExistTag) {
        AllTagDTO allTagDTO = new AllTagDTO();
        allTagDTO.setTag_name(tag_name);
        result1 = addToTagList(allTagDTO);
        tag_id = getTagIdByName(tag_name);
        // if (result1 > 0) {
        // mv.addObject("result1", result1);
        // }
      }
      
      // 입력태그가 헌태그인 경우 : 해당 tag_id값 받아오기
      else {
        tag_id = getTagIdByName(tag_name);
      }
      // 받아온 tag_id를 게시글 번호에 저장시켜주고 성공 여부 리턴
      result2 = insertLinkedTag(post_id, tag_id);
    }
    return result2;
  }

  // (@게시글입력화면) 존재하는 태그인지 검사해서 boolean 리턴
  public boolean isExistTag(String tag_name) {
    int result = tagDAO.isExistTag(tag_name);
    boolean isExistTag = false;
    if (result > 0)
      isExistTag = true;
    return isExistTag;
  }

  // (확인용) tag_name에 해당하는 tag_id찾기
  public Integer getTagIdByName(String tag_name) {
    return tagDAO.getTagIdByName(tag_name);
  }

  // (@게시글 업로드시) tag list에 새 태그 추가
  public int addToTagList(AllTagDTO allTagDTO) {
    return tagDAO.addToTagList(allTagDTO);
  }

  // (@게시글 업로드시) 게시글 post_id 에 태그 tag_id 포함시키기
  public int insertLinkedTag(int post_id, int tag_id) {
    return tagDAO.insertLinkedTag(post_id, tag_id);
  }
  
  
  
  /** @유저 등록 관련 메소드 */
  // @유저태그 등록
  public int registerUserTag(int post_id, String fullContent) {
    int result=0;
    System.out.println("fullContent = " + fullContent);
    List<String> userTagStringList = new ArrayList<String>();
    // 전체 콘텐츠에서 @태그의 단어들만 추출하기
    if (fullContent.contains("@")) {
      String[] tokens = fullContent.substring(fullContent.indexOf("@")).split(" ");
      for (String token : tokens) {
        if (token.contains("@")) {
          System.out.println("token = " + token);
          String[] candidates = token.substring(token.indexOf("@") + 1).split("@");
          for (String candidate : candidates) {
            System.out.println("candidate = " + candidate);
            if (candidate != "" && candidate != null && candidate != " ")
              userTagStringList.add(candidate.split("#")[0]);
          }
        }
      }
    }

    // 추출해온 @login_id값이 유저테이블에 존재하는 아이디인지 검사
    for (int i = 0; i < userTagStringList.size(); i++) {
      System.out.println(userTagStringList.get(i));
      String login_id = userTagStringList.get(i);
      System.out.println("login_id = " + login_id);

      int user_id = 0;
      // 추출된 @login_id값과 매치되는 실제 login_id가 테이블 상 존재하는지 확인
      Integer integer_user_id = findUserIdByLoginId(login_id);
      if (integer_user_id == null) {
        user_id = 0;
      } else {
        user_id = integer_user_id;
      }
      // 해당 아이디가 존재하는 경우, post_user_tag에 해당 user_id를 저장시켜준다
      if (user_id > 0) {
        System.out.println("user_id = " + user_id);
        result = insertUserTag(post_id, user_id);
        if (result > 0) {
        }
       // mv.addObject("result", result);
      }
    }
    return result;
  }
  
  
  
  

  // (확인용) 게시글에 @login_id 태그시 해당하는 user_id 찾아오기
  // public int findUserIdByLoginId(String login_id) {
  public Integer findUserIdByLoginId(String login_id) {
    return tagDAO.findUserIdByLoginId(login_id);
  }

  // (@게시글 업로드시) 게시글 post_id에 유저태그 user_id 포함시키기
  public int insertUserTag(int post_id, int user_id) {
    return tagDAO.insertUserTag(post_id, user_id);
  }

  // (확인용) 전체 태그 리스트 출력시키기
  public List<AllTagDTO> allTagList() {
    return tagDAO.allTagList();
  }

  // (@검색화면 첫화면) 랜덤으로 게시글들의 이미지 출력시키기
  public List<PostImageDTO> selectPostImageRandom() {
    return tagDAO.selectPostImageRandom();
  }

  // (@검색화면에서 태그 입력시) '특정 글자'가 포함된 모든 tag_name 출력시키기
  public List<AllTagDTO> tagListByLetter(String letter_to_search) {
    return tagDAO.tagListByLetter(letter_to_search);
  }

  // (@검색화면에서 태그 입력시) 특정 태그 tag_name을 가진 모든 게시글 수 출력(optional)

  // (@검색화면에서 태그 선택시) 선택된 tag_id를 가진 모든 게시글 post_id 들 출력시키기(사진보이기용)
  public List<LinkedTagDTO> selectPostIdByTagId(int tag_id) {
    return tagDAO.selectPostIdByTagId(tag_id);
  }

  // (@검색화면에서 유저 입력시) '특정 글자'가 포함된 모든 user name 출력시키기
  public List<UserDTO> selectUserBySearch(String letter_to_search) {
	  
    return tagDAO.selectUserBySearch(letter_to_search);
  }

  // (@검색화면에서 유저 입력시) '특정 글자'가 포함된 모든 user의 설명글 출력시키기(optional)

  // (@검색화면에서 유저 선택시) 선택된 user의 프로필화면 출력



  // // 특정 게시글 post_id에 포함된 전체 해시태그리스트 출력(x)
  // public List<LinkedTagDTO> taggedLinkList(int post_id) {
  // return tagDAO.taggedLinkList(post_id);
  // }
  // // 특정 게시글 post_id에 포함된 전체 유저태그리스트 출력(x)
  // public List<UserTagDTO> taggedUserList(int post_id) {
  // return tagDAO.taggedUserList(post_id);
  // }

}
