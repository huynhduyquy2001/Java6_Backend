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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.AccountAndFollow;
import com.viesonet.entity.Comments;
import com.viesonet.entity.Favorites;
import com.viesonet.entity.Follow;
import com.viesonet.entity.Images;
import com.viesonet.entity.Interaction;
import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Reply;
import com.viesonet.entity.ReplyRequest;
import com.viesonet.entity.Users;
import com.viesonet.service.CommentsService;
import com.viesonet.service.CookieService;
import com.viesonet.service.FavoritesService;
import com.viesonet.service.FollowService;
import com.viesonet.service.ImagesService;
import com.viesonet.service.InteractionService;
import com.viesonet.service.NotificationsService;
import com.viesonet.service.PostsService;
import com.viesonet.service.ReplyService;
import com.viesonet.service.SessionService;
import com.viesonet.service.UsersService;

import jakarta.servlet.ServletContext;
import net.coobird.thumbnailator.Thumbnails;
@EnableScheduling
@RestController
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
	InteractionService interactionService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private SessionService session;

	@Autowired
	CookieService cookieService;

	@Autowired
	ReplyService replyService;

	@Autowired
	NotificationsService notificationsService;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/findfollowing")
	public List<Posts> getFollowsByFollowingId() {
		List<Follow> followList = followService.getFollowing(session.get("id"));
		List<String> userId = followList.stream().map(follow -> {
			return follow.getFollowing().getUserId();
		}).collect(Collectors.toList());
		return postsService.findPostsByListUserId(userId);
	}

	@ResponseBody
	@GetMapping("/findlikedposts")
	public List<String> findLikedPosts() {
		return favoritesService.findLikedPosts(session.get("id"));
	}

	@ResponseBody
	@GetMapping("/findmyaccount")
	public AccountAndFollow findMyAccount() {
		return followService.getFollowingFollower(usersService.findUserById(session.get("id")));
	}

	@ResponseBody
	@PostMapping("/likepost/{postId}")
	public void likePost(@PathVariable("postId") int postId) {
		// thêm tương tác
		Posts post = postsService.findPostById(postId);
		interactionService.plusInteraction(session.get("id"), post.getUser().getUserId());

		// thêm thông báo
		notificationsService.createNotifications(usersService.findUserById(session.get("id")), post.getLikeCount(),
				post.getUser().getUserId(), post, 3);

		favoritesService.likepost(usersService.findUserById(session.get("id")), postsService.findPostById(postId));
	}

	@ResponseBody
	@PostMapping("/didlikepost/{postId}")
	public void didlikePost(@PathVariable("postId") int postId) {
		Posts post = postsService.findPostById(postId);
		interactionService.minusInteraction(session.get("id"), post.getUser().getUserId());
		favoritesService.didlikepost(session.get("id"), postId);
	}

	@GetMapping("/postdetails/{postId}")
	public Posts postDetails(@PathVariable("postId") int postId) {
		return postsService.findPostById(postId);
	}

	@PostMapping("/addcomment/{postId}")
	public Comments addComment(@PathVariable("postId") int postId, @RequestParam("myComment") String content) {
		// thêm tương tác
		Posts post = postsService.findPostById(postId);
		interactionService.plusInteraction(session.get("id"), post.getUser().getUserId());

		// thêm thông báo
		notificationsService.createNotifications(usersService.findUserById(session.get("id")), post.getCommentCount(),
				post.getUser().getUserId(), post, 4);

		return commentsService.addComment(postsService.findPostById(postId),
				usersService.findUserById(session.get("id")), content);
	}

	@PostMapping("/addreply")
	public ResponseEntity<Reply> addReply(@RequestBody ReplyRequest request) {
		// Lấy các tham số từ request
		String receiverId = request.getReceiverId();
		String replyContent = request.getReplyContent();
		int commentId = request.getCommentId();

		return ResponseEntity.ok(replyService.addReply(usersService.findUserById(session.get("id")), replyContent,
				commentsService.getCommentById(commentId), usersService.findUserById(receiverId)));

	}

	@GetMapping("/findpostcomments/{postId}")
	public List<Comments> findPostComments(@PathVariable("postId") int postId) {
		return commentsService.findCommentsByPostId(postId);
	}

	@ResponseBody
	@PostMapping("/post")
	public String dangBai(@RequestParam("photoFiles") MultipartFile[] photoFiles,
			@RequestParam("content") String content) {
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
		
//		//thêm thông báo
//		List<Interaction> interaction = interactionService.findListInteraction(session.get("id"));
//
//		if(interaction == null) {
//			System.out.println("Chưa có ai tương tác với account này");
//		}else {
//			for(Interaction it : interaction) {
//				notificationsService.createNotifications(usersService.findUserById(session.get("id")), 0, it.getInteractedPerson(), myPost, 1);
//			}
//			
//		}
		
		// Xử lý và lưu thông tin bài viết kèm ảnh vào cơ sở dữ liệu
		return "success";
	}
	
	@Scheduled(fixedRate = 1000)  // Lặp lại theo thời gian
    public void sendRealTimeThongBao() {
        List<Notifications> thongBao = notificationsService.findNotificationByReceiver(); // Implement hàm này để lấy thông báo từ CSDL
        messagingTemplate.convertAndSend("/private-chat", thongBao);
    }
	
	@GetMapping("/loadnotification")
    public List<Notifications> getThongBao() {
        return notificationsService.findNotificationByReceiver(); // Implement hàm này để lấy thông báo từ CSDL
    }

	@GetMapping("/")
	public ModelAndView getHomePage() {
		ModelAndView modelAndView = new ModelAndView("Index");
		return modelAndView;
	}

	@GetMapping("/logout")
	public ModelAndView logout() {
		session.remove("id");
		session.remove("role");
		cookieService.delete("user");
		cookieService.delete("pass");
		return new ModelAndView("redirect:/login");
	}

}
