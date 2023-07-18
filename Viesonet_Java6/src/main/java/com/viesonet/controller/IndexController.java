package com.viesonet.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Favorites;
import com.viesonet.entity.Follow;
import com.viesonet.entity.Images;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Users;
import com.viesonet.service.CommentsService;
import com.viesonet.service.FavoritesService;
import com.viesonet.service.FollowService;
import com.viesonet.service.ImagesService;
import com.viesonet.service.PostsService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

import jakarta.servlet.ServletContext;
import net.coobird.thumbnailator.Thumbnails;

@Controller
public class IndexController {

	@Autowired
	private FollowService followService;

	@Autowired
	private PostsService postsService;

	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private UsersService usersService;

	@Autowired
	ServletContext context;
	
	@Autowired
	ImagesService imagesService;
	
	@Autowired
	CommentsService commentsService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	SessionService session;
	
	@GetMapping("/findfollowing")
	public List<Posts> getFollowsByFollowingId() {
		List<Follow> followList = followService.getFollowing("UI011");
		List<String> userId = followList.stream().map(follow -> {
			return follow.getFollowing().getUserId();
		}).collect(Collectors.toList());
		return postsService.findPostsByListUserId(userId);
	}

	@ResponseBody
	@GetMapping("/findlikedposts")
	public List<String> findLikedPosts() {
		return favoritesService.findLikedPosts("UI011");
	}

	@ResponseBody
	@GetMapping("/findmyaccount")
	public Users findMyAccount() {
		return usersService.findUserById("UI011");
	}

	@ResponseBody
	@PostMapping("/likepost/{postId}")
	public void likePost(@PathVariable("postId") int postId) {
		favoritesService.likepost(usersService.findUserById("UI011"), postsService.findPostById(postId));
	}

	@ResponseBody
	@PostMapping("/didlikepost/{postId}")
	public void didlikePost(@PathVariable("postId") int postId) {
		favoritesService.didlikepost("UI011", postId);
	}

	@ResponseBody
	@PostMapping("/post")
	public String dangBai(@RequestParam("photoFiles") MultipartFile[] photoFiles, @RequestParam("content") String content ) {
		List<String> hinhAnhList = new ArrayList<>();
		// Lưu bài đăng vào cơ sở dữ liệu
		Posts myPost = postsService.post(usersService.findUserById("UI011"));
		//Lưu hình ảnh vào cở sở dữ liệu
		if (photoFiles != null && photoFiles.length > 0) {
			for (MultipartFile photoFile : photoFiles) {
				if (!photoFile.isEmpty()) {
					String originalFileName = photoFile.getOriginalFilename();
					String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					String newFileName = originalFileName + "-" + timestamp + extension;
					String pathUpload = context.getRealPath("/images/" + newFileName);

					try {
						photoFile.transferTo(new File(pathUpload));

						long fileSize = photoFile.getSize();
						if (fileSize > 1 * 1024 * 1024) {
							double quality = 0.6;
							String outputPath = pathUpload;
							Thumbnails.of(pathUpload).scale(1.0).outputQuality(quality).toFile(outputPath);
						}											
						imagesService.saveImage(myPost, newFileName);						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}

	
	@GetMapping("/")
	public String index() {
		return "Index";
	}
}
