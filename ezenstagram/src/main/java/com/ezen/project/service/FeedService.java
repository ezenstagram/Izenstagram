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

	/** ���ƿ� ��� **/
	// ���ƿ� ��ư�� ������ ��, Ư�� �Խù� ��ȣ�� Ư�� ������ ���ƿ� �������� �ƴ��� �˻�
	// ������ ���ƿ� �����ٸ� 1�� ���ϵǰ�, �ƴϸ� 0�̸���
	public int chkLikes(int post_id, int user_id) {

		return feedDAO.chkLikes(post_id, user_id);
	}

	// �˻��ؼ� ���ƿ� �ȴ������ٸ�(=chkLikes���� ���ϵ� ���� 0�̶��),
	// ���ƿ� ������ ����
	public int saveLikes(LikesDTO likesDTO) {
		return feedDAO.saveLikes(likesDTO);
	}

	// �˻��ؼ� ���ƿ並 ������ �������ٸ�(=chkLikes���� ���ϵ� ���� 1�̶��),
	// ���ƿ� ������ ����
	public int delLikes(int post_id, int user_id) {
		return feedDAO.delLikes(post_id, user_id);
	}

	/** ��� ��� **/
	// �Խñ� �ϳ� �� �����ϴ� comment_id max �� ���ϱ�
	// ��� ������ ����
	public int saveCmts(CommentsDTO commentsDTO) {
		// MAX�� ���ϱ�
		Integer maxCi = feedDAO.maxCi(commentsDTO.getPost_id());
		// COMMENT ID�Ǵ�
		if (maxCi == null) {
			maxCi = 1;
		} else {
			maxCi = maxCi + 1;
		}
		// COMMENT INSERT
		commentsDTO.setComment_id(maxCi);
		return feedDAO.saveCmts(commentsDTO);
	}

	// �Խñ� �ϳ��� �ش�Ǵ� ��۵����� ���
	public List<CommentsDTO> cmtList(int post_id) {
		return feedDAO.cmtList(post_id);
	}

	// ��ۻ���
	public int delCmts(CommentsDTO commentsDTO) {
		return feedDAO.delCmts(commentsDTO);
	}

	// ��ۼ���
	public int updateCmts(CommentsDTO commentsDTO) {
		return feedDAO.updateCmts(commentsDTO);
	}

	// �Խñ� ������ �̾ƿ���
	public List<PostAllDTO> feedPostList(int user_id) {
		List<PostAllDTO> list = feedDAO.feedPostList(user_id);

		for (int i = 0; i < list.size(); i++) {
			List<PostImageDTO> imglist = feedDAO.feedPostImageList(list.get(i).getPost_id());
			list.get(i).setPostImageList(imglist);
			List<CommentsDTO> cmtList = feedDAO.cmtList(list.get(i).getPost_id()); // ���
																					// ������
																					// ����Ʈ
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

	// �Խñ� �̹��� ������ �̾ƿ���
	public List<PostImageDTO> feedPostImageList(int post_id) {
		return feedDAO.feedPostImageList(post_id);
	}

	// ��� ������ �̾ƿ���
	public List<CommentsDTO> getCmtData(int post_id) {
		return feedDAO.getCmtData(post_id);
	}

}
