angular.module('myApp', [])
	.controller('myCtrl', function($scope, $http) {
	
		$scope.Posts = [];
		$scope.likedPosts = [];
		$scope.myAccount = {};
		$scope.postData = {};
		$scope.postDetails = {};
		$scope.postComments = [];
		$scope.followers = [];
		$scope.followings = [];
		$scope.myPostImage = [];

		$http.get('/findusers')
			.then(function(response) {
				var UserInfo = response.data;
				$scope.UserInfo = UserInfo;
			})
			.catch(function(error) {
				console.log(error);
			});


	
		// Hàm gọi API để lấy thông tin người dùng và cập nhật vào biến $scope.UpdateUser
		$http.get('/getUserInfo').then(function(response) {
			$scope.UserInfo = response.data;
			$scope.birthday = new Date($scope.UserInfo.birthday)
			// Khởi tạo biến $scope.UpdateUser để lưu thông tin cập nhật
			
			$scope.UpdateUser = angular.copy($scope.UserInfo);
			 
			console.log($scope.UserInfo.birthday);

		});
	
		// Hàm cập nhật thông tin người dùng
		$scope.updateUserInfo = function() {
			// Gửi dữ liệu từ biến $scope.UpdateUser đến server thông qua một HTTP request (POST request)
			$scope.UpdateUser.birthday = $scope.birthday;
			$http.post('/updateUserInfo', $scope.UpdateUser).then(function(response) {
				$http.get('/findusers')
			.then(function(response) {
				var UserInfo = response.data;
				$scope.UserInfo = UserInfo;
			})
			.catch(function(error) {
				console.log(error);
			});
				console.log('User info updated successfully.');
			}).catch(function(error) {
				console.error('Error while updating user info:', error);
			});
		};
		

		// Hàm gọi API để lấy thông tin người dùng và cập nhật vào biến $scope.UpdateUser
		$http.get('/getAccInfo').then(function(response) {		
			$scope.AccInfo = response.data;
			// Khởi tạo biến $scope.UpdateUser để lưu thông tin cập nhật
			
			$scope.UpdateAcc = angular.copy($scope.AccInfo);
			
			
		});

		// Hàm cập nhật thông tin người dùng
		$scope.updateAccInfo = function() {
			// Gửi dữ liệu từ biến $scope.UpdateUser đến server thông qua một HTTP request (POST request)
			$http.post('/updateAccInfo/' + $scope.AccInfo.email + "/" + $scope.AccInfo.accountStatus.statusName).then(function(response) {
				console.log('Account info updated successfully.');
			}).catch(function(error) {
				console.error('Error while updating account info:', error);
			});
		};

		$http.get('/findaccounts')
			.then(function(response) {
				var AccInfo = response.data;
				$scope.AccInfo = AccInfo;
				$scope.accountStatus = $scope.AccInfo.accountStatus;
			})
			.catch(function(error) {
				console.log(error);
			});
			
			
		$http.get('/findmyfollowers')
			.then(function(response) {
				$scope.followers = response.data;
				//console.log($scope.followers); // Kiểm tra dữ liệu trong console log
			})
			.catch(function(error) {
				console.log(error);
			});
		$http.get('/findmyfollowing')
			.then(function(response) {
				$scope.followings = response.data;
				//console.log($scope.followings); // Kiểm tra dữ liệu trong console log
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

		$http.get('/findmyfollow')
			.then(function(response) {
				var myAccount = response.data;
				$scope.myAccount = myAccount;
			})
			.catch(function(error) {
				console.log(error);
			});
		$http.get('/getmypost')
			.then(function(response) {
				var myPosts = response.data;
				$scope.myPosts = myPosts;

			});
		$http.get('/find9post')
			.then(function(response) {
				var ninePost = response.data;
				$scope.ninePost = ninePost;
				console.log($scope.nitePost);
			});

		$http.get('/countmypost')
			.then(function(response) {
				var sumPost = response.data;
				$scope.sumPost = sumPost;
			})


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
					return minutes + ' phút trước';
				} else if (hours < 24) {
					return hours + ' giờ trước';
				}
			} else if (days === 1) {
				return 'hôm qua';
			} else {
				return days + ' ngày trước';
			}
		};
		$scope.post = function() {
			var content = $scope.postData.text;

			var formData = new FormData();
			var fileInput = document.getElementById('inputGroupFile01');

			for (var i = 0; i < fileInput.files.length; i++) {
				formData.append('photoFiles', fileInput.files[i]);
				alert(fileInput.files[i])
			}
			formData.append('content', content.content);

			$http.post('/post', formData, {
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined }
			}).then(function(response) {
				alert("ok")
				// Xử lý phản hồi thành công từ máy chủ
				console.log(response.data);
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
			});
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

			$http.get('/postdetails/' + postId)
				.then(function(response) {
					var postDetails = response.data;
					$scope.postDetails = postDetails;
					// Xử lý phản hồi thành công từ máy chủ
					$('#chiTietBaiViet').modal('show');
					console.log(response.data);
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
					$scope.myComment = '';

				}, function(error) {
					console.log(error);
				});
		};
	});        