package com.ezen.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.project.bean.CommentsDTO;
import com.ezen.project.bean.LikesDTO;
import com.ezen.project.bean.PostAllDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.dao.FeedDAO;

@Service
public class FeedService {
	@Autowired
	private FeedDAO feedDAO;

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
			List<PostImageDTO> imglist = feedDAO.feedPostImageList(list.get(i).getPost_id());
			list.get(i).setPostImageList(imglist);
			List<CommentsDTO> cmtList = feedDAO.cmtList(list.get(i).getPost_id()); // 댓글
																					// 데이터
																					// 리스트
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
		return feedDAO.feedPostImageList(post_id);
	}

	// 댓글 데이터 뽑아오기
	public List<CommentsDTO> getCmtData(int post_id) {
		return feedDAO.getCmtData(post_id);
	}

}
