package com.viesonet.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Follow;
import com.viesonet.entity.Posts;
import com.viesonet.entity.UserInformation;
import com.viesonet.entity.Users;
import com.viesonet.service.AccountsService;
import com.viesonet.service.FavoritesService;
import com.viesonet.service.FollowService;
import com.viesonet.service.ImagesService;
import com.viesonet.service.PostsService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

import jakarta.servlet.ServletContext;
import net.coobird.thumbnailator.Thumbnails;

@RestController
public class ProfileController {
	
	@Autowired
	private FollowService followService;

	@Autowired
	private PostsService postsService;
	
	@Autowired 
	private AccountsService accountsService;
	
	@Autowired
	private FavoritesService favoritesService;

	@Autowired
	private UsersService usersService;

	@Autowired
	ServletContext context;
	
	@Autowired
	SessionService session;
	
	@Autowired
	ImagesService imagesService;
	//Lấy thông tin về follow người dùng hiện tại	
	@GetMapping("/findmyfollow")
	public AccountAndFollow findMyAccount() {	
		 return followService.getFollowingFollower(usersService.findUserById(session.get("id")));
	}
	//Lấy thông tin chi tiết các followers
	@GetMapping("/findmyfollowers")
    public List<Users> getFollowersInfoByUserId(@SessionAttribute("id") String userId) {
        return followService.getFollowersInfoByUserId(session.get("id"));
    }
	//Lấy thông tin chi tiết các followers
	@GetMapping("/findmyfollowing")
    public List<Users> getFollowingInfoByUserId(@SessionAttribute("id") String userId) {
        return followService.getFollowingInfoByUserId(session.get("id"));
    }
	//Lấy thông tin chi tiết của người dùng trong bảng Users
	@GetMapping("/findusers")
	public Users findmyi1() {
		return usersService.findUserById(session.get("id"));
	}
	//Lấy thông tin chi tiết của người dùng trong bảng Account
	@GetMapping("/findaccounts")
	public Accounts findmyi2() {
		return accountsService.getAccountByUsers(session.get("id"));
	}
	
	//Lấy thông tin các bài viết người dùng hiện tại
	@GetMapping("/getmypost")
	public List<Posts> getMyPost(){
		return postsService.getMyPost(session.get("id"));
	}
	//Đếm số bài viết của người dùng hiện tại
	@GetMapping("/countmypost")
	public int countMyPosts() {
		return postsService.countPost(session.get("id"));
	}

	//Hiển thị trang cá nhân
	@GetMapping("/profile")
	public ModelAndView profile() {
		ModelAndView modelAndView = new ModelAndView("Profile");
        return modelAndView;
	}
}
