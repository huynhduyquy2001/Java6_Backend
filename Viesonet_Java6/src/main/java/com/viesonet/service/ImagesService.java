package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ImagesDao;
import com.viesonet.entity.Images;
import com.viesonet.entity.Posts;

@Service
public class ImagesService {
	
	@Autowired
	ImagesDao imagesDao;
	
public void saveImage(Posts post, String imageUrl) {
	Images image = new Images();
	image.setImageUrl(imageUrl);
	image.setPost(post);
	imagesDao.saveAndFlush(image);
}
}
