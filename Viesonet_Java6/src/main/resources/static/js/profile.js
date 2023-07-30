angular.module('myApp', ['ngRoute'])
	.controller('myCtrl', function($scope, $http, $window, dataService) {

		$scope.Posts = [];
		$scope.likedPosts = [];
		$scope.myAccount = {};
		$scope.postData = {};
		$scope.postDetails = {};
		$scope.postComments = [];
		$scope.followers = [];
		$scope.followings = [];
		$scope.myPostImage = [];
		$scope.myListFollow = 0;
		$scope.UserInfo = {};

		$http.get('/findusers')
			.then(function(response) {
				var UserInfo = response.data;
				$scope.UserInfo = UserInfo;
				
			})
			.catch(function(error) {
				console.log(error);
			});
			
		
				// Truyền dữ liệu vào Service
				var currentUserId = $scope.UserInfo.userId;
				dataService.setData(currentUserId);
		
		// Hàm gọi API để lấy thông tin người dùng và cập nhật vào biến $scope.UpdateUser
		$http.get('/getUserInfo').then(function(response) {
			$scope.UserInfo = response.data;
			$scope.birthday = new Date($scope.UserInfo.birthday)
			// Khởi tạo biến $scope.UpdateUser để lưu thông tin cập nhật

			$scope.UpdateUser = angular.copy($scope.UserInfo);

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

				$scope.totalImagesCount = myPosts.reduce(function(total, post) {
					return total + post.images.length;
				}, 0);

			});

		$http.get('/countmypost')
			.then(function(response) {
				var sumPost = response.data;
				$scope.sumPost = sumPost;
			})

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
						var post = $scope.myPosts.find(function(item) {
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
						var post = $scope.myPosts.find(function(item) {
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
			var formData = new FormData();
			var fileInput = document.getElementById('inputGroupFile01');

			// Check if no files are selected
			if (fileInput.files.length === 0) {
				const Toast = Swal.mixin({
					toast: true,
					position: 'top-end',
					showConfirmButton: false,
					timer: 3000,
					timerProgressBar: true,
					didOpen: (toast) => {
						toast.addEventListener('mouseenter', Swal.stopTimer)
						toast.addEventListener('mouseleave', Swal.resumeTimer)
					}
				})

				Toast.fire({
					icon: 'warning',
					title: 'Bạn phải thêm ảnh vào bài viết'
				})
				return; // Return without doing anything
			}
			if ($scope.content === null || $scope.content === undefined) {
				$scope.content = '';
			}
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
			})
			$scope.content = '';
			const Toast = Swal.mixin({
				toast: true,
				position: 'top-end',
				showConfirmButton: false,
				timer: 3000,
				timerProgressBar: true,
				didOpen: (toast) => {
					toast.addEventListener('mouseenter', Swal.stopTimer)
					toast.addEventListener('mouseleave', Swal.resumeTimer)
				}
			})

			Toast.fire({
				icon: 'success',
				title: 'Bài viết được đăng thành công'
			})
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
		$scope.isFollowing = function(followingId) {
			// Lấy id của người dùng hiện tại
			var currentUserId = $scope.UserInfo.userId;
			// Kiểm tra xem người dùng hiện tại đã follow người dùng với id tương ứng (followingId) chưa
			// Dựa vào danh sách các người dùng mà người dùng hiện tại đã follow
			// Trong ví dụ này, danh sách này có thể được lưu trữ trong cơ sở dữ liệu hoặc được lấy từ API
			var myListFollow = $scope.listFollow;
			for (var i = 0; i < myListFollow.length; i++) {
				if (myListFollow[i].followingId === followingId && myListFollow[i].followerId === currentUserId) {
					// Người dùng hiện tại đã follow người dùng với id tương ứng
					return true;
				}
			}
			// Người dùng hiện tại chưa follow người dùng với id tương ứng
			return false;
		},
			$scope.followUser = function(followingId) {
				var currentUserId = $scope.UserInfo.userId;
				var data = {
					followerId: currentUserId,
					followingId: followingId
				};
				$http.post('/followOther', data)
					.then(function(response) {
						// Thêm follow mới thành công, cập nhật trạng thái trong danh sách và chuyển nút thành "Unfollow"
						$scope.listFollow.push(response.data);
						console.log("Success Follow!");

						// Cập nhật lại danh sách follow sau khi thêm mới thành công
						$scope.refreshFollowList();
					})
					.catch(function(error) {
						console.log(error);
					});
			};

		$scope.unfollowUser = function(followingId) {
			var currentUserId = $scope.UserInfo.userId;
			var data = {
				followerId: currentUserId,
				followingId: followingId
			};
			$http.delete('/unfollowOther', { data: data, headers: { 'Content-Type': 'application/json' } })
				.then(function(response) {
					// Cập nhật lại danh sách follow sau khi xóa thành công
					$scope.listFollow = $scope.listFollow.filter(function(follow) {

						return !(follow.followerId === currentUserId && follow.followingId === followingId);
					});
				}, function(error) {
					console.log(error);
				});
		};

		$http.get('/getallfollow')
			.then(function(response) {
				var listFollow = response.data;
				$scope.listFollow = listFollow;
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
			});
		// Hàm làm mới danh sách follow
		$scope.refreshFollowList = function() {
			$http.get('/getallfollow')
				.then(function(response) {
					var listFollow = response.data;
					$scope.listFollow = listFollow;
				})
				.catch(function(error) {
					console.log(error);
				});
		};
		$scope.changeBackground = function() {
			var formData = new FormData();
			var fileInput = document.getElementById('inputGroupFile02');

			for (var i = 0; i < fileInput.files.length; i++) {
				formData.append('photoFiles2', fileInput.files[i]);
			}
			formData.append('content', $scope.content);

			$http.post('/updateBackground', formData, {
				transformRequest: angular.identity,
				headers: {
					'Content-Type': undefined
				}
			}).then(function(response) {
				// Xử lý phản hồi thành công từ máy chủ (cập nhật ảnh bìa)
				$scope.content = '';
				alert("Đăng bài và cập nhật ảnh bìa thành công!");
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
				alert("Đăng bài và cập nhật ảnh bìa thành công!");
			});
		};
		$scope.changeAvatar = function() {
			var formData = new FormData();
			var fileInput = document.getElementById('inputGroupFile03');

			for (var i = 0; i < fileInput.files.length; i++) {
				formData.append('photoFiles3', fileInput.files[i]);
			}
			formData.append('content', $scope.content);

			$http.post('/updateAvatar', formData, {
				transformRequest: angular.identity,
				headers: {
					'Content-Type': undefined
				}
			}).then(function(response) {
				// Xử lý phản hồi thành công từ máy chủ (cập nhật ảnh bìa)
				$scope.content = '';
				alert("Đăng bài và cập nhật ảnh đại diện thành công!");
			}, function(error) {
				// Xử lý lỗi
				console.log(error);
				alert("Đăng bài và cập nhật ảnh đại diện thành công1!");
			});
		};

		$http.get('/getListImage')
			.then(function(response) {
				// Dữ liệu trả về từ API sẽ nằm trong response.data
				$scope.imageList = response.data;
				$scope.displayedImages = $scope.imageList.slice(0, 9);
			})
			.catch(function(error) {
				console.log(error);
			});

		// Khởi tạo biến selectedPost
		$scope.selectedPost = {};

		// Hàm mở modal chỉnh sửa bài viết
		$scope.openEditModal = function(myPost) {
			// Gán giá trị cho selectedPost từ myPost được chọn
			$scope.selectedPost = myPost;
			console.log($scope.selectedPost);
			// Hiển thị modal
			$('#editModal').modal('show');
		};
		$scope.showImageModal = function(imageUrl) {
			// Gán đường dẫn ảnh vào thuộc tính src của thẻ img trong modal
			document.getElementById('modalImage').src = '/images/' + imageUrl;

			// Hiển thị modal
			$('#imageModal').modal('show');
		};
		$scope.updatePost = function(selectedPost) {
			$http.put('/updatePost/' + selectedPost.postId, selectedPost)
				.then(function(response) {
					// Xử lý phản hồi thành công từ server
					alert("Cập nhật bài viết thành công!");

					// Cập nhật lại bài viết trong mảng myPosts
					var index = $scope.myPosts.findIndex(function(MyPosts) {
						return MyPosts.postId === selectedPost.postId;
					});

					if (index !== -1) {
						$scope.myPosts[index].content = selectedPost.content;
					}

					// Đóng modal
					$('#editModal').modal('hide');
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log(error);
					alert("Có lỗi xảy ra khi cập nhật bài viết.");
				});
		};

		$scope.hidePost = function(postId) {
			$http.put('/hide/' + postId)
				.then(function(response) {
					// Cập nhật trạng thái của bài viết trong danh sách myPosts
					var index = $scope.myPosts.findIndex(function(post) {
						return post.postId === postId;
					});

					if (index !== -1) {
						$scope.myPosts[index].isActive = false;
					}
					$http.get('/getmypost')
						.then(function(response) {
							var myPosts = response.data;
							$scope.myPosts = myPosts;

							$scope.totalImagesCount = myPosts.reduce(function(total, post) {
								return total + post.images.length;
							}, 0);

						});
				})
				.catch(function(error) {
					// Xử lý lỗi
					console.log(error);
				});
		};
		$scope.goToProfile = function(userId) {
			// Sử dụng $window.location.href để chuyển đến trang otherProfile.html với userId truyền vào
			$scope.otherUser = userId;
			$window.location.href = '/otherProfile/' + userId;
		
		
		};

	})

