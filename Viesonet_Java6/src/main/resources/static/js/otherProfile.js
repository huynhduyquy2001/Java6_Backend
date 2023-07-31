angular.module('myApp').controller('myCtrl2', function($scope, $http, $window, dataService) {
		var userId = dataService.getData().userId;
		$http.get('/getInfoOtherProfile/' + userId)
			.then(function(response) {
				var data = response.data;
				var userId = data.id;
		$http.get('/getInfoOtherProfile/' + userId)
			.then(function(response) {
				var data = response.data;
				console.log(data)		
				$scope.UserInfo = data.userInfo;
				$scope.AccInfo = data.accountInfo;
				$scope.myPosts = data.userPosts;
				$scope.myFollowersAndFollowing = data.myFollowersAndFollowing;
				$scope.followers = data.followers;
				$scope.followings = data.followings;
				$scope.sumPost = data.sumPost;
				$scope.imageList = data.userImages;
				$scope.totalImagesCount = $scope.imageList.length;
				$scope.displayedImages = $scope.imageList.slice(0, 9);
				$scope.currentUserId = data.currentUserId;
				var currentUserId = $scope.currentUserId;
				$scope.userId = data.id;
				
				$scope.isFollowing = function(followingId) {
					// Lấy id của người dùng hiện tại
					var currentUserId = $scope.currentUserId;
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
				}
				$scope.followUser = function(followingId) {
					var currentUserId = $scope.currentUserId;
					console.log(currentUserId)
					var data = {
						followerId: currentUserId,
						followingId: userId
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
					var currentUserId = $scope.currentUserId;
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
				$http.get('/findfollowing')
					.then(function(response) {
						var Posts = response.data;
						$scope.Posts = Posts;
					})

				$http.get('/findmyaccount')
					.then(function(response) {
						var myAccount = response.data;
						$scope.myAccount = myAccount;
					})
					.catch(function(error) {
						console.log(error);
					});

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
					$scope.content = '';
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

			$scope.goToProfiles = function(userId) {
			// Sử dụng $window.location.href để chuyển đến trang otherProfile.html với userId truyền vào
			console.log("CC: "+userId)
			$http.post('/saveUserId', { userId: userId })
           	 .then(function(response) {
                console.log('UserId temporarily saved.');
                // Gọi hàm để lấy userId từ máy chủ và lưu vào dataService (nếu cần thiết)
                $http.get('/getTemporaryUserId')
                    .then(function(response) {
                        var temporaryUserId = response.data;
                        dataService.setData(temporaryUserId);
                        console.log('UserId saved in dataService:', temporaryUserId);
                    })
                    .catch(function(error) {
                        console.log('Error while getting temporary userId:', error);
                    });
            })
            .catch(function(error) {
                console.log('Error while saving temporary userId:', error);
            });
			$window.location.href = '/otherProfile/' + userId;
			
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

				$scope.showImageModal = function(imageUrl) {
					// Gán đường dẫn ảnh vào thuộc tính src của thẻ img trong modal
					document.getElementById('modalImage').src = '/images/' + imageUrl;

					// Hiển thị modal
					$('#imageModal').modal('show');
				};
			})
			
			});

});