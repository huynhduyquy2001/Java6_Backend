package com.viesonet.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.dao.FollowDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Accounts;
import com.viesonet.entity.Follow;
import com.viesonet.entity.FollowDTO;
import com.viesonet.entity.Images;
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
	private ImagesService imagesService;
	
	@Autowired
	ServletContext context;
	
	@Autowired
	SessionService session;
	
	@Autowired
	private ServletContext servletContext;
	
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
	//Lấy thông tin chi tiết các followings
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
	// Phương thức này trả về thông tin người dùng (Users) dựa vào session attribute "id".
	@GetMapping("/getUserInfo")
	public Users getUserInfo(@SessionAttribute("id") String userId) {
	    return usersService.getUserById(userId);
	}

	// Phương thức này trả về thông tin tài khoản (Accounts) dựa vào session attribute "id".
	@GetMapping("/getAccInfo")
	public Accounts getAccInfo(@SessionAttribute("id") String userId) {
	    return accountsService.getAccountById(userId);
	}

	// Phương thức này thực hiện cập nhật thông tin người dùng (Users) dựa vào dữ liệu từ request body.
	@PostMapping("/updateUserInfo")
	public void updateUserInfo(@RequestBody Users userInfo, @SessionAttribute("id") String userId) {      
	    usersService.updateUserInfo(userInfo, session.get("id"));
	}

	// Phương thức này thực hiện cập nhật thông tin tài khoản (Accounts) dựa vào dữ liệu từ các path variable email và statusId.
	@PostMapping("/updateAccInfo/{email}/{statusId}")
	public void updateAccInfo(@PathVariable String email, @PathVariable String statusId) {      
	    int id = 0;
	    if (statusId.equals("Công khai")) {
	        id = 1;
	    } else if (statusId.equals("Chỉ theo dõi")) {
	        id = 2;
	    } else if (statusId.equals("Tạm ẩn")) {
	        id = 3;
	    }
	    accountsService.updateAccInfo(session.get("id"), email, id);
	}
	//Lấy danh sách follow
	@GetMapping("/getallfollow")
	public List<FollowDTO> getFollow() {
	    List<Follow> listFollow = followService.findAllFollow();
	    List<FollowDTO> listFollowDTO = new ArrayList<>();

	    for (Follow follow : listFollow) {
	        FollowDTO followDTO = new FollowDTO();
	        followDTO.setFollowId(follow.getFollowId());
	        followDTO.setFollowerId(follow.getFollower().getUserId());
	        followDTO.setFollowingId(follow.getFollowing().getUserId());
	        followDTO.setFollowDate(follow.getFollowDate());

	        listFollowDTO.add(followDTO);
	    }

	    return listFollowDTO;	
	}
	//Nút follow
	@PostMapping("/followOther")
	public Follow followUser(@RequestBody FollowDTO followDTO) {
	    // Lấy dữ liệu người dùng hiện tại và người dùng đang được follow
	    Users follower = usersService.findUserById(followDTO.getFollowerId());
	    Users following = usersService.findUserById(followDTO.getFollowingId());

	    // Thêm dữ liệu follow vào cơ sở dữ liệu
	    Follow follow = new Follow();
	    follow.setFollower(follower);
	    follow.setFollowing(following);
	    follow.setFollowDate(new Date());
	    return followService.saveFollow(follow);
	}
	//Nút unfollow
	@ResponseBody
	@DeleteMapping("/unfollowOther")
	public void unfollowUser(@RequestBody FollowDTO followDTO) {
	    // Lấy dữ liệu người dùng hiện tại và người dùng đang được unfollow
	    Users follower = usersService.findUserById(followDTO.getFollowerId());
	    Users following = usersService.findUserById(followDTO.getFollowingId());
	    
	    // Xóa dữ liệu follow từ cơ sở dữ liệu
	    followService.deleteFollowByFollowerAndFollowing(follower, following);
	}
	//Cập nhật ảnh bìa
	@ResponseBody
	@PostMapping("/updateBackground")
	public String doiAnhBia(@RequestParam("photoFiles2") MultipartFile[] photoFiles, @RequestParam("content") String content,@SessionAttribute("id") String userId ) {
		List<String> hinhAnhList = new ArrayList<>();
		// Lưu bài đăng vào cơ sở dữ liệu
		Posts myPost = postsService.post(usersService.findUserById(session.get("id")), content);
		// Lưu hình ảnh vào thư mục static/images
		if (photoFiles != null && photoFiles.length > 0) {
			for (MultipartFile photoFile : photoFiles) {
				if (!photoFile.isEmpty()) {
					String originalFileName = photoFile.getOriginalFilename();
					String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					String newFileName = originalFileName + "-" + timestamp + extension;

					String rootPath = servletContext.getRealPath("/");
					String parentPath = new File(rootPath).getParent();
					String pathUpload = parentPath + "/resources/static/images/" + newFileName;
					
					try {
						photoFile.transferTo(new File(pathUpload));
						String contentType = photoFile.getContentType();
						boolean type = true;
						if (contentType.startsWith("image")) {

						} else if (contentType.startsWith("video")) {
							type = false;
						}
						if (type == true) {
							long fileSize = photoFile.getSize();
							if (fileSize > 1 * 1024 * 1024) {
								double quality = 0.6;
								String outputPath = pathUpload;
								Thumbnails.of(pathUpload).scale(1.0).outputQuality(quality).toFile(outputPath);
							}
						}
						imagesService.saveImage(myPost, newFileName, type);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// Cập nhật ảnh bìa cho người dùng
	                if (myPost != null) {
	                    String newBackgroundImageUrl = newFileName;
	                    usersService.updateBackground(userId, newBackgroundImageUrl);
	                }
				}
				
			}
		}
		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}
	//Cập nhật ảnh đại diện
	@ResponseBody
	@PostMapping("/updateAvatar")
	public String doiAnhDaiDien(@RequestParam("photoFiles3") MultipartFile[] photoFiles, @RequestParam("content") String content,@SessionAttribute("id") String userId ) {
		List<String> hinhAnhList = new ArrayList<>();
		// Lưu bài đăng vào cơ sở dữ liệu
		Posts myPost = postsService.post(usersService.findUserById(session.get("id")), content);
		// Lưu hình ảnh vào thư mục static/images
		if (photoFiles != null && photoFiles.length > 0) {
			for (MultipartFile photoFile : photoFiles) {
				if (!photoFile.isEmpty()) {
					//Tạo tên file ảnh
					String originalFileName = photoFile.getOriginalFilename();
					String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
					String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
					String newFileName = originalFileName + "-" + timestamp + extension;
					//Tạo đường dẫn để lưu trữ
					String rootPath = servletContext.getRealPath("/");
					String parentPath = new File(rootPath).getParent();
					String pathUpload = parentPath + "/resources/static/images/" + newFileName;
					
					try {
						photoFile.transferTo(new File(pathUpload));
						String contentType = photoFile.getContentType();
						boolean type = true;
						if (contentType.startsWith("image")) {

						} else if (contentType.startsWith("video")) {
							type = false;
						}
						if (type == true) {
							long fileSize = photoFile.getSize();
							if (fileSize > 1 * 1024 * 1024) {
								double quality = 0.6;
								String outputPath = pathUpload;
								Thumbnails.of(pathUpload).scale(1.0).outputQuality(quality).toFile(outputPath);
							}
						}
						imagesService.saveImage(myPost, newFileName, type);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// Cập nhật ảnh bìa cho người dùng
	                if (myPost != null) {
	                    String newAvatarImageUrl = newFileName;
	                    usersService.updateAvatar(userId, newAvatarImageUrl);
	                }
				}
				
			}
		}
		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}
	//Lấy danh sách ảnh theo UserId
	@GetMapping("/getListImage")
    public List<Images> getImagesByUserIdFromSession(@SessionAttribute("id") String userId) {
         return imagesService.getImagesByUserId(userId);
    }
	
    //Cập nhật bài viết
    @PutMapping("/updatePost/{postId}")
    public void updatePost(@PathVariable int postId, @RequestBody Posts posts) {
    	Posts existingPost = postsService.getPostById(postId);


        existingPost.setContent(posts.getContent());
        postsService.savePost(existingPost);
    }
    //Ẩn bài viết
    @PutMapping("/hide/{postId}")
    public void hidePost(@PathVariable int postId) {
        postsService.hidePost(postId);
    }
	//Hiển thị trang cá nhân
	@GetMapping("/profile")
	public ModelAndView profile() {
		ModelAndView modelAndView = new ModelAndView("Profile");
        return modelAndView;
	}
	
	@GetMapping("/otherProfile/{userId}")
	public ModelAndView otherProfile(@PathVariable String userId) {
	    ModelAndView modelAndView = new ModelAndView("otherProfile");
	    
	    return modelAndView;
	}
	
	@GetMapping("/getInfoOtherProfile/{userId}")
    public Map<String, Object> getInfoOtherProfile(@PathVariable String userId) {
        Map<String, Object> responseData = new HashMap<>();

        // Lấy thông tin người dùng theo userId
        Users userInfo = usersService.getUserById(userId);
        responseData.put("userInfo", userInfo);

        // Lấy thông tin tài khoản của người dùng theo userId
        Accounts accountInfo = accountsService.getAccountByUsers(userId);
        responseData.put("accountInfo", accountInfo);

        // Lấy danh sách bài viết của người dùng theo userId
        List<Posts> userPosts = postsService.getMyPost(userId);
        responseData.put("userPosts", userPosts);

        // Lấy danh sách ảnh của người dùng theo userId
        List<Images> userImages = imagesService.getImagesByUserId(userId);
        responseData.put("userImages", userImages);
        
        // Lấy số lượng các follow
        AccountAndFollow follow = followService.getFollowingFollower(userInfo);
        responseData.put("myFollowersAndFollowing", follow);
        
        // Lấy danh sách chi tiết các follower
        List<Users> followers = followService.getFollowersInfoByUserId(userId);
        responseData.put("followers", followers);
        
        // Lấy danh sách following
        List<Users> followings = followService.getFollowingInfoByUserId(userId);
        responseData.put("followings", followings);
        
        // Lấy tổng số lượng bài viết
        Integer sumPost =  postsService.countPost(userId);
        responseData.put("sumPost", sumPost);
        return responseData;
    }

}
