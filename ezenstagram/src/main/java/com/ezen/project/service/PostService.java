package com.ezen.project.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

				int result1 = postImageDAO.insertPostImage(postImageDTO);//이미지를 올렸을때 성공하는 result. 최종 json에 출력해야 하는것을 result로 해야해서 1을 붙였음
				
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

}
