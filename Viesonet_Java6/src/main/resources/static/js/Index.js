
var app = angular.module('myApp', ['pascalprecht.translate', 'ngRoute'])

app.controller('myCtrl', function($scope, $http, $translate, $window, $rootScope) {
	$scope.myAccount = {};
	$rootScope.unseenmess=0;
	$rootScope.check=false;
	$http.get('/findmyaccount')
		.then(function(response) {
			var myAccount = response.data;
			$scope.myAccount = myAccount;
		})
		.catch(function(error) {
			console.log(error);
		});
	//Đa ngôn ngữ	
	$scope.changeLanguage = function(langKey) {
		$translate.use(langKey);
		localStorage.setItem('myAppLangKey', langKey); // Lưu ngôn ngữ đã chọn vào localStorages
	};
	// Kiểm tra xem còn tin nhắn nào chưa đọc không
	$http.get('/getunseenmessage')
		.then(function(response) {
			$rootScope.check = response.data > 0;
			$rootScope.unseenmess = response.data;
		})
		.catch(function(error) {
			console.log(error);
		});
		
	//Load thông báo
	$scope.hasNewNotification = false;
	$scope.notificationNumber = [];
	//Load thông báo chưa đọc
	$http.get('/loadnotification')
		.then(function(response) {
			var data = response.data;
			for (var i = 0; i < data.length; i++) {
				$scope.notification.push(data[i]);
				$scope.notificationNumber = $scope.notification;
				if ($scope.notificationNumber.length != 0) {
					$scope.hasNewNotification = true;
				}
			}
		})
		.catch(function(error) {
			console.log(error);
		});
	//Load tất cả thông báo
	$http.get('/loadallnotification')
		.then(function(response) {
			$scope.allNotification = response.data;
		})
		.catch(function(error) {
			console.log(error);
		});
	//Kết nối websocket
	$scope.ConnectNotification = function() {
		var socket = new SockJS('/private-notification');
		var stompClient = Stomp.over(socket);
		stompClient.debug = false;
		stompClient.connect({}, function(frame) {
			stompClient.subscribe('/private-user', function(response) {

				var data = JSON.parse(response.body)
				// Kiểm tra điều kiện đúng với user hiện tại thì thêm thông báo mới
				if ($scope.myAccount.user.userId === data.receiver.userId) {
					//thêm vào thông báo mới
					$scope.notification.push(data);
					//thêm vào tất cả thông báo
					$scope.allNotification.push(data);
					//thêm vào mảng để đếm độ số thông báo
					$scope.notificationNumber.push(data);
					//cho hiện thông báo mới
					$scope.hasNewNotification = true;
				}
				$scope.$apply();

			});
		});
	};
	
	
	//Kết nối khi mở trang web
	$scope.ConnectNotification();
	// Hàm này sẽ được gọi sau khi ng-include hoàn tất nạp tập tin "_menuLeft.html"

	// Thay đổi URL SockJS tại đây nếu cần thiết
	var sockJSUrl = '/chat';

	// Tạo một đối tượng SockJS bằng cách truyền URL SockJS
	var socket = new SockJS(sockJSUrl);

	// Tạo một kết nối thông qua Stomp over SockJS
	var stompClient = Stomp.over(socket);

	// Khi kết nối WebSocket thành công
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);

		// Đăng ký hàm xử lý khi nhận thông điệp từ server
		// Lắng nghe các tin nhắn được gửi về cho người dùng
		stompClient.subscribe('/user/' + $scope.myAccount.user.userId + '/queue/receiveMessage', function(message) {
			// Kiểm tra xem còn tin nhắn nào chưa đọc không
	$http.get('/getunseenmessage')
		.then(function(response) {
			$rootScope.check = response.data > 0;
			$rootScope.unseenmess = response.data;
		})
		.catch(function(error) {
			console.log(error);
		});
		});
	}, function(error) {
		console.error('Lỗi kết nối WebSocket:', error);
	});
	
	
	

	//xem chi tiết thông báo
	$scope.seen = function(notificationId) {
		$scope.getPostDetails(notificationId);
	}

	//Ẩn tất cả thông báo khi click vào xem
	$scope.hideNotification = function() {
		$http.post('/setHideNotification', $scope.notification)
			.then(function(response) {
				// Xử lý phản hồi từ backend nếu cần
			})
			.catch(function(error) {
				// Xử lý lỗi nếu có
			});
		$scope.hasNewNotification = false;
		$scope.notificationNumber = [];
	}

	//Xóa thông báo
	$scope.deleteNotification = function(notificationId) {
		$http.delete('/deleteNotification/' + notificationId)
			.then(function(response) {
				$scope.allNotification = $scope.allNotification.filter(function(allNotification) {
					return allNotification.notificationId !== notificationId;
				});
			})
			.catch(function(error) {
				// Xử lý lỗi nếu có
			});
	}

	//Ẩn thông báo 
	$scope.hideNotificationById = function(notificationId) {
		// Ví dụ xóa phần tử có notificationId là 123
		$scope.removeNotificationById(notificationId);
	}
	//Hàm xóa theo ID của mảng
	$scope.removeNotificationById = function(notificationIdToRemove) {
		// Lọc ra các phần tử có notificationId khác với notificationIdToRemove
		$scope.notification = $scope.notification.filter(function(notification) {
			return notification.notificationId !== notificationIdToRemove;
		});
	};
})


