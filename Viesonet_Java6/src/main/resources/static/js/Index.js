angular.module('myApp', [])
.controller('myCtrl', function($scope, $http) {
	$scope.Posts = [];
	$scope.likedPosts = [];
	$scope.myAccount = {};
	$scope.postData = {};
	$scope.postDetails = {};
	$scope.postComments = [];
	$scope.replyContent = {}; // Khởi tạo replyContent      
	$scope.check = false;
	$scope.unseenmess=0;

//kiểm tra xem còn tin nhắn nào chưa đọc không
	$http.get('/getunseenmessage')
		.then(function(response) {
			var count = response.data;
			if (count > 0) {
				$scope.check = true;
				$scope.unseenmess = count;
			}
		})
		.catch(function(error) {
			console.log(error);
		});

	$http.get('/findfollowing')
		.then(function(response) {
			var Posts = response.data;
			$scope.Posts = Posts;
		})
		.catch(function(error) {
			console.log(error);
		});

	$http.get('/findlikedposts')
		.then(function(response) {
			var likedPosts = response.data;
			$scope.likedPosts = likedPosts;
		})
		.catch(function(error) {
			console.log(error);
		});

	$http.get('/findmyaccount')
		.then(function(response) {
			var myAccount = response.data;
			$scope.myAccount = myAccount;
		})
		.catch(function(error) {
			console.log(error);
		});

	$scope.saveImages = function() {

	}
	$scope.likePost = function(postId) {
		var likedIndex = $scope.likedPosts.indexOf(postId.toString());
		var likeEndpoint = '/likepost/' + postId;
		var dislikeEndpoint = '/didlikepost/' + postId;

		// Nếu postId chưa tồn tại trong mảng likedPosts
		if (likedIndex === -1) {
			// Thêm postId vào mảng likedPosts
			$scope.likedPosts.push(postId.toString());

			// Gửi yêu cầu POST để like bài viết và cập nhật likeCount+1
			$http.post(likeEndpoint)
				.then(function(response) {

					// Cập nhật thuộc tính likeCount+1 trong đối tượng post
					var post = $scope.Posts.find(function(item) {
						return item.postId === postId;
					});
					if (post) {
						post.likeCount++;

					}
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log(error);
				});
		} else {

			// Xóa postId khỏi mảng likedPosts
			$scope.likedPosts.splice(likedIndex, 1);

			// Gửi yêu cầu POST để dislike bài viết và cập nhật likeCount-1
			$http.post(dislikeEndpoint)
				.then(function(response) {
					// Xử lý thành công
					//console.log(response.data);

					// Cập nhật thuộc tính likeCount-1 trong đối tượng post
					var post = $scope.Posts.find(function(item) {
						return item.postId === postId;
					});
					if (post) {
						post.likeCount--;

					}
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log(error);
				});
		}
	};
	$scope.getFormattedTimeAgo = function(date) {
		var currentTime = new Date();
		var activityTime = new Date(date);
		var timeDiff = currentTime.getTime() - activityTime.getTime();
		var seconds = Math.floor(timeDiff / 1000);
		var minutes = Math.floor(timeDiff / (1000 * 60));
		var hours = Math.floor(timeDiff / (1000 * 60 * 60));
		var days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

		if (days === 0) {
			if (hours === 0 && minutes < 60) {
				if (seconds < 60) {
					return seconds + ' giây trước';
				} else {
					return minutes + ' phút trước';
				}
			} else if (hours < 24) {
				return hours + ' giờ trước';
			}
		} else if (days === 1) {
			return 'Hôm qua';
		} else {
			return days + ' ngày trước';
		}
	};

	$scope.post = function() {
	    var formData = new FormData();
	    var fileInput = document.getElementById('inputGroupFile01');

	    for (var i = 0; i < fileInput.files.length; i++) {
	        formData.append('photoFiles', fileInput.files[i]);
	    }
	    formData.append('content', $scope.content);

	    $http.post('/post', formData, {
	        transformRequest: angular.identity,
	        headers: {
	            'Content-Type': undefined
	        }
	    }).then(function(response) {
	        // Xử lý phản hồi thành công từ máy chủ
	        
	    }, function(error) {
	        // Xử lý lỗi
	        console.log(error);
	    });
	    alert("ok");
	    $scope.content='';
	};


	$scope.getPostDetails = function(postId) {
		$http.get('/findpostcomments/' + postId)
			.then(function(response) {
				var postComments = response.data;
				$scope.postComments = postComments;


				console.log(response.data);
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
			});

		$scope.isReplyEmpty = true;
		$http.get('/postdetails/' + postId)
			.then(function(response) {
				var postDetails = response.data;
				$scope.postDetails = postDetails;
				// Xử lý phản hồi thành công từ máy chủ
				$('#chiTietBaiViet').modal('show');

			}, function(error) {
				// Xử lý lỗi
				console.log(error);
			});
	};


	$scope.addComment = function(postId) {
		var myComment = $scope.myComment;
		$http.post('/addcomment/' + postId + '?myComment=' + myComment)
			.then(function(response) {
				$scope.postComments.unshift(response.data);
				var postToUpdate = $scope.Posts.find(function(post) {
					return post.postId = postId;
				});
				if (postToUpdate) {
					postToUpdate.commentCount++;
				}
				$scope.myComment = '';


			}, function(error) {
				console.log(error);
			});
	};


	$scope.logout = function() {
		$http.get('/logout')
			.then(function() {
				window.location.href = '/login';
			}, function(error) {
				console.log(error);
			});
	};


	$scope.sendReply = function(receiverId, replyContent, replyId, commentId) {
		var requestData = {
			receiverId: receiverId,
			replyContent: replyContent,
			commentId: commentId
		};
		$http.post('/addreply', requestData)
			.then(function(response) {
				var comment = $scope.postComments.find(function(comment) {
					return comment.commentId === commentId;
				});
				comment.reply.unshift(response.data);
				$scope.replyContent[replyId] = '';
			})
			.catch(function(error) {
				// Xử lý lỗi
				console.log('Lỗi:', error);
			});

	};



	$scope.sendReplyForComment = function(receiverId, commentId, replyContent) {
		var requestData = {
			receiverId: receiverId,
			replyContent: replyContent,
			commentId: commentId
		};
		$http.post('/addreply', requestData)
			.then(function(response) {
				var comment = $scope.postComments.find(function(comment) {
					return comment.commentId === commentId;
				});
				comment.reply.unshift(response.data);
				$scope.replyContent[commentId] = '';

			})
			.catch(function(error) {
				// Xử lý lỗi
				console.log('Lỗi:', error);
			});

	};
	$scope.handleKeyDown = function(event, userId, replyContent, replyId, commentId) {
		if (event.keyCode === 13) {
			// Người dùng đã nhấn phím Enter
			$scope.sendReply(userId, replyContent, replyId, commentId);
		}
	};

	$scope.handleKeyDownReplyForComment = function(event, receiverId, commentId, replyContent) {
		if (event.keyCode === 13) {
			// Người dùng đã nhấn phím Enter
			$scope.sendReplyForComment(receiverId, commentId, replyContent);
		}
	};

	//Thay đổi URL SockJS tại đây nếu cần thiết
	var sockJSUrl = '/chat';

	// Tạo một đối tượng SockJS bằng cách truyền URL SockJS
	var socket = new SockJS(sockJSUrl);

	// Tạo một kết nối thông qua Stomp over SockJS
	var stompClient = Stomp.over(socket);

	// Khi kết nối WebSocket thành công
	// Khi kết nối WebSocket thành công
	stompClient.connect({}, function (frame) {
	    // Đăng ký hàm xử lý khi nhận thông điệp từ server
	    // Lắng nghe các tin nhắn được gửi về cho người dùng
	    stompClient.subscribe('/user/'+$scope.myAccount.user.userId+'/queue/receiveMessage', function (message) {
	    	$scope.check = true;
	    	$scope.$apply();
	    });
	}, function (error) {
	    console.error('Lỗi kết nối WebSocket:', error);
	});


});     