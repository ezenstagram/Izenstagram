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
			List<PostImageDTO> imglist = feedPostImageList(list.get(i).getPost_id());
			list.get(i).setPostImageList(imglist);
			List<CommentsDTO> cmtList = feedDAO.cmtList(list.get(i).getPost_id()); // ���
																					// ������
																					// ����Ʈ
			UserDTO userDTO = userDAO.user_profile(list.get(i).getUser_id()); // �Ķ���Ϳ� �۾�������̵�
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

	// �Խñ� �̹��� ������ �̾ƿ���
	public List<PostImageDTO> feedPostImageList(int post_id) {
		List<PostImageDTO> imglist = feedDAO.feedPostImageList(post_id);
		String imgUrl = null;
		for (int i = 0; i < imglist.size(); i++) {

			imgUrl = "http://192.168.0.13:8080/image/storage/" + imglist.get(i).getImage_url();
			imglist.get(i).setImage_url(imgUrl);
		}
		return imglist;
	}

	// ��� ������ �̾ƿ���
	public List<CommentsDTO> getCmtData(int post_id) {
		return feedDAO.getCmtData(post_id);
	}

	// ���ƿ� ���� ����
	public int cntLikes(int post_id) {
		return feedDAO.cntLikes(post_id);
	}

	// ���� �Խñ� !�Ѱ�! ������ �̾ƿ��� //�� �������̵�� �α����� ����� ���̵�
	public PostAllDTO feedPostOne(int post_id, int user_id) { // ����Ʈ �ƹ��ų�!
		PostAllDTO postOne = feedDAO.feedPostOne(post_id);
		// �Խñ� �ϳ��� �̹����� ������ ����
		List<PostImageDTO> imglist = feedPostImageList(postOne.getPost_id());
		postOne.setPostImageList(imglist);
		// ��� ����
		List<CommentsDTO> cmtList = feedDAO.cmtList(postOne.getPost_id());

		// �� �ۼ����� �������� �޾ƿ���
		UserDTO userDTO = userDAO.user_profile(postOne.getUser_id()); // �Ķ���Ϳ� �۾�������̵�
		// �� �ۼ����� ������ ���� �޾ƿ���
		String tempProfile = userDTO.getProfile_photo();
		System.out.println("tempProfile = " + tempProfile);
		// Ư�� post_id�� �Խñۿ� �ش��ϴ� ���ƿ� ���� �޾ƿ���
		int temp_cnt_like = feedDAO.cntLikes(postOne.getPost_id());

		postOne.setUserDTO(userDTO);
		postOne.getUserDTO().setProfile_photo("http://192.168.0.13:8080/image/storage/" + tempProfile);

		postOne.setLikes_cnt(temp_cnt_like);
		postOne.setComment_cnt(cmtList.size());

		// �α����� ����� ���̵�
		int isLike = feedDAO.chkLikes(postOne.getPost_id(), user_id);
		if (isLike == 1) {
			postOne.setLike(true);
		} else {
			postOne.setLike(false);
		}

		return postOne;
	}

}
