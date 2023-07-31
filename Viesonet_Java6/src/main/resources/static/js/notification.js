angular.module('myApp', [])
.controller('myCtrl', function($scope, $window, $http, $timeout) {
		$scope.notification = [];
		$scope.allNotification = [];
		//Load thông báo

		$http.get('/loadallnotification')
			.then(function(response) {
				$scope.allNotification = response.data;
			})
			.catch(function(error) {
				console.log(error);
			});

		$scope.hasNewNotification = false;
		$scope.ConnectNotification = function() {

			var socket = new SockJS('/private-notification');
			var stompClient = Stomp.over(socket);
			stompClient.debug = false;
			stompClient.connect({}, function(frame) {
				stompClient.subscribe('/private-user', function(response) {
					var data = JSON.parse(response.body)
					// Thêm dữ liệu mới vào mảng notification
					for (var i = 0; i < data.length; i++) {
						if (data[i].receiver.userId === $scope.myAccount.user.userId) {
							// Kiểm tra nếu thông báo chưa tồn tại trong mảng, thì mới thêm vào
							if (!$scope.isNotificationExists(data[i])) {
								$scope.notification.push(data[i]);
								$scope.hasNewNotification = true;
								$scope.$apply(); // Cập nhật giao diện khi có dữ liệu mới
							}
							$scope.$apply();
						}
					}

				});
			});
		};

		$scope.isNotificationExists = function(newNotification) {
			return $scope.notification.some(function(notification) {
				return notification.notificationId === newNotification.notificationId;
			});
		}

		$scope.removeNotificationById = function(notificationIdToRemove) {
			// Lọc ra các phần tử có notificationId khác với notificationIdToRemove
			$scope.notification = $scope.notification.filter(function(notification) {
				return notification.notificationId !== notificationIdToRemove;
			});
		};

		$scope.seen = function(notificationId, postId) {
			$http.put('/seennotification/' + notificationId)
				.then(function(response) {
					$scope.allNotification = response.data;
				}, function(error) {
					console.log(error);
				});
			$scope.getPostDetails(postId);
			// Ví dụ xóa phần tử có notificationId là 123
			$scope.removeNotificationById(notificationId);
		}
		
		$scope.seenall = function(postId) {
			$scope.getPostDetails(postId);
		}

		$scope.hideNotification = function() {
			$scope.hasNewNotification = false;
		}

		$scope.ConnectNotification();
});
