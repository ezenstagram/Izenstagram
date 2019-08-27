package com.ezen.project.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.ezen.project.bean.PostDTO;
import com.ezen.project.bean.PostImageDTO;
import com.ezen.project.dao.PostDAO;
import com.ezen.project.dao.PostImageDAO;

@Service
public class PostService {
	@Autowired
	private PostDAO postDAO;
	@Autowired
	private PostImageDAO postImageDAO;

	public String insert(PostDTO postDTO, String filename, InputStream inputStream) {
		String result = "0";
		int su = postDAO.insertPost(postDTO);

		int post_id = postDAO.getcurrPost_id();

		
		if (su > 0) {
			
			try {
				String image_url = filename;
				System.out.println("image_url : " + image_url);
				int image_id = 1;

				File file = new File(image_url);
				try {
					FileCopyUtils.copy(inputStream, new FileOutputStream(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				PostImageDTO postImageDTO = new PostImageDTO();
				postImageDTO.setImage_id(image_id);
				postImageDTO.setImage_url(image_url);
				postImageDTO.setPost_id(post_id);

				int result1 = postImageDAO.insertPostImage(postImageDTO);//�씠誘몄�瑜� �삱�졇�쓣�븣 �꽦怨듯븯�뒗 result. 理쒖쥌 json�뿉 異쒕젰�빐�빞 �븯�뒗寃껋쓣 result濡� �빐�빞�빐�꽌 1�쓣 遺숈��쓬
				
				if (result1 > 0) {
					result = "1";
				} else {
					postDAO.deletePost(post_id);
				}
			} catch (Exception e) {
				  postDAO.deletePost(post_id);
			}
		}
		return result;
	}
	public List<PostImageDTO> profilePostRefImage(int user_id) {
		return postImageDAO.profilePostRefImage(user_id);
	}
	public int following(int user_id) {
		return postDAO.following(user_id);
	}
	public int follower(int user_id) {
		return postDAO.follower(user_id);
	}
	public int postCount(int user_id) {
		return postDAO.postCount(user_id);
	}

}
