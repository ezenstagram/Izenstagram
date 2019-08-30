package com.ezen.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.project.bean.CommentsDTO;
import com.ezen.project.bean.LikesDTO;
import com.ezen.project.bean.PostAllDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.bean.UserDTO;
import com.ezen.project.dao.FeedDAO;
import com.ezen.project.dao.UserDAO;

@Service
public class FeedService {
	@Autowired
	private FeedDAO feedDAO;
	@Autowired
	private UserDAO userDAO;

	/** 좋아요 기능 **/
	// 좋아요 버튼을 눌렀을 때, 특정 게시물 번호에 특정 유저가 좋아요 눌렀는지 아닌지 검사
	// 기존에 좋아요 눌렀다면 1이 리턴되고, 아니면 0이리턴
	public int chkLikes(int post_id, int user_id) {

		return feedDAO.chkLikes(post_id, user_id);
	}

	// 검사해서 좋아요 안눌렀었다면(=chkLikes에서 리턴된 값이 0이라면),
	// 좋아요 데이터 저장
	public int saveLikes(LikesDTO likesDTO) {
		return feedDAO.saveLikes(likesDTO);
	}

	// 검사해서 좋아요를 기존에 눌렀었다면(=chkLikes에서 리턴된 값이 1이라면),
	// 좋아요 데이터 삭제
	public int delLikes(int post_id, int user_id) {
		return feedDAO.delLikes(post_id, user_id);
	}

	/** 댓글 기능 **/
	// 게시글 하나 당 존재하는 comment_id max 값 구하기
	// 댓글 데이터 저장
	public int saveCmts(CommentsDTO commentsDTO) {
		// MAX값 구하기
		Integer maxCi = feedDAO.maxCi(commentsDTO.getPost_id());
		// COMMENT ID판단
		if (maxCi == null) {
			maxCi = 1;
		} else {
			maxCi = maxCi + 1;
		}
		// COMMENT INSERT
		commentsDTO.setComment_id(maxCi);
		return feedDAO.saveCmts(commentsDTO);
	}

	// 게시글 하나에 해당되는 댓글데이터 출력
	public List<CommentsDTO> cmtList(int post_id) {
		return feedDAO.cmtList(post_id);
	}

	// 댓글삭제
	public int delCmts(CommentsDTO commentsDTO) {
		return feedDAO.delCmts(commentsDTO);
	}

	// 댓글수정
	public int updateCmts(CommentsDTO commentsDTO) {
		return feedDAO.updateCmts(commentsDTO);
	}

	// 게시글 데이터 뽑아오기
	public List<PostAllDTO> feedPostList(int user_id) {
		List<PostAllDTO> list = feedDAO.feedPostList(user_id);
		for (int i = 0; i < list.size(); i++) {
			List<PostImageDTO> imglist = feedPostImageList(list.get(i).getPost_id());
			list.get(i).setPostImageList(imglist);
			List<CommentsDTO> cmtList = feedDAO.cmtList(list.get(i).getPost_id()); // 댓글
																					// 데이터
																					// 리스트
			UserDTO userDTO = userDAO.user_profile(list.get(i).getUser_id()); // 파라미터에 글쓴사람아이디
			String tempProfile = userDTO.getProfile_photo();
			System.out.println("tempProfile = " + tempProfile);
			int temp_cnt_like = feedDAO.cntLikes(list.get(i).getPost_id());

			list.get(i).setUserDTO(userDTO);
			list.get(i).getUserDTO().setProfile_photo("http://192.168.0.13:8080/image/storage/" + tempProfile);

			list.get(i).setLikes_cnt(temp_cnt_like);
			list.get(i).setComment_cnt(cmtList.size());

			int isLike = feedDAO.chkLikes(list.get(i).getPost_id(), user_id);
			if (isLike == 1) {
				list.get(i).setLike(true);
			} else {
				list.get(i).setLike(false);
			}
		}

		return list;
	}

	// 게시글 이미지 데이터 뽑아오기
	public List<PostImageDTO> feedPostImageList(int post_id) {
		List<PostImageDTO> imglist = feedDAO.feedPostImageList(post_id);
		String imgUrl = null;
		for (int i = 0; i < imglist.size(); i++) {

			imgUrl = "http://192.168.0.13:8080/image/storage/" + imglist.get(i).getImage_url();
			imglist.get(i).setImage_url(imgUrl);
		}
		return imglist;
	}

	// 댓글 데이터 뽑아오기
	public List<CommentsDTO> getCmtData(int post_id) {
		return feedDAO.getCmtData(post_id);
	}

	// 좋아요 갯수 세기
	public int cntLikes(int post_id) {
		return feedDAO.cntLikes(post_id);
	}

	// 랜덤 게시글 !한개! 데이터 뽑아오기 //이 유저아이디는 로그인한 당사자 아이디
	public PostAllDTO feedPostOne(int post_id, int user_id) { // 포스트 아무거나!
		PostAllDTO postOne = feedDAO.feedPostOne(post_id);
		// 게시글 하나당 이미지는 여러개 가능
		List<PostImageDTO> imglist = feedPostImageList(postOne.getPost_id());
		postOne.setPostImageList(imglist);
		// 댓글 갯수
		List<CommentsDTO> cmtList = feedDAO.cmtList(postOne.getPost_id());

		// 글 작성자의 유저정보 받아오기
		UserDTO userDTO = userDAO.user_profile(postOne.getUser_id()); // 파라미터에 글쓴사람아이디
		// 글 작성자의 프로필 사진 받아오기
		String tempProfile = userDTO.getProfile_photo();
		System.out.println("tempProfile = " + tempProfile);
		// 특정 post_id의 게시글에 해당하는 좋아요 갯수 받아오기
		int temp_cnt_like = feedDAO.cntLikes(postOne.getPost_id());

		postOne.setUserDTO(userDTO);
		postOne.getUserDTO().setProfile_photo("http://192.168.0.13:8080/image/storage/" + tempProfile);

		postOne.setLikes_cnt(temp_cnt_like);
		postOne.setComment_cnt(cmtList.size());

		// 로그인한 당사자 아이디
		int isLike = feedDAO.chkLikes(postOne.getPost_id(), user_id);
		if (isLike == 1) {
			postOne.setLike(true);
		} else {
			postOne.setLike(false);
		}

		return postOne;
	}

}
