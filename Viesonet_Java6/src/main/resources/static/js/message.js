angular.module('myApp', []).controller('myCtrl', function($scope, $http) {

	$scope.profile = {};
	$scope.list = [];
	$scope.chatRoom = [];
	$scope.chatuser = {};
	$scope.notification = [];

	var data = $http.get('/chat/profile')
		.then(function(response) {
			$scope.profile = response.data;
		});//

	$http.get('/chat/list')
		.then(function(response) {
			$scope.list = response.data;
		});

	$scope.chatUser = function(userIdChat) {
		var user = $http.get('/chat/chatuser/' + userIdChat)
			.then(function(response) {
				$scope.chatuser = response.data;
			});
		Promise.all([user]).then(function() {
			// Hàm xử lý sự kiện khi nhấn phím Enter
			$scope.onKeyUp = function(event) {
				if (event.keyCode === 13) { // 13 là mã phím Enter
					$scope.send($scope.chatuser.userId);
				}
			};
		});//
	}

	Promise.all([data]).then(function() {
		$scope.stompClient = null;

		// Hàm $scope.connect
		$scope.connect = function() {
			if ($scope.profile.userId) {
				var socket = new SockJS('/message');
				$scope.stompClient = Stomp.over(socket);

				$scope.stompClient.connect({}, $scope.onConnected, $scope.onError);
			}
		};//

		// Hàm onConnected
		$scope.onConnected = function() {
			$scope.stompClient.subscribe('/topic/public', $scope.onMessageReceived);
			$scope.stompClient.send("/app/chat.register", {}, JSON.stringify({ sender: $scope.profile.userId, type: 'JOIN' })
			)
		};//

		// Hàm $scope.send
		$scope.messageText = '';
		$scope.send = function(receiverId) {
			var messageContent = $scope.messageText;

			if (messageContent && $scope.stompClient) {
				var chatMessage = {
					sender: $scope.profile.userId,
					avatar: $scope.profile.avatar,
					receiver: receiverId,
					content: messageContent,
					type: 'CHAT'
				};
				$scope.stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
				$scope.messageText = '';
			}
		};//

		$scope.onMessageReceived = function(payload) {
			var message = JSON.parse(payload.body);
			if (message.type === 'CHAT' && message.receiver === $scope.profile.userId) {
				var currentDate = new Date();
				$scope.notification.push({
					userId: message.sender,
					content: message.content,
					avatar: message.avatar
				});
				$scope.$apply();
			}
		}

		$scope.onError = function(error) {

		};//

		$scope.connect();
	});//

});//1
