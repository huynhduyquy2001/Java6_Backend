angular.module('myApp', []).controller('myCtrl', function($scope, $http) {
	$scope.profile = {};
	$scope.list = [];
	$scope.chatRoom = [];
	$scope.chatuser = {};

	$http.get('/chat/profile')
		.then(function(response) {
			$scope.profile = response.data;
		});

	$http.get('/chat/list')
		.then(function(response) {
			$scope.list = response.data;
		});
	// Khởi tạo biến lưu trạng thái click ban đầu
	$scope.clicked = false;
	$scope.chatUser = function(userIdChat, userIdSend, avatar1, avatar2) {
		if (!$scope.clicked) {
			console.log("Click rồi")
			$scope.clicked = true;
		} else {
			console.log("Click lần 2")
			$scope.disconnect();
		}
		$http.get('/chat/chatuser/' + userIdChat)
			.then(function(response) {
				$scope.chatuser = response.data;
			});

		var data = $http.get('/chat/chatroom/' + userIdChat + '/' + userIdSend)
			.then(function(response) {
				$scope.chatRoom = response.data;
			});

		Promise.all([data]).then(function() {
			$scope.stompClient = null;
			$scope.room = null;

			var roomId = [];
			var content = [];
			for (var i = 0; i < $scope.chatRoom.length; i++) {
				roomId.push($scope.chatRoom[i].roomChat);
				content.push($scope.chatRoom[i].content)
			}

			// Hàm $scope.connect
			$scope.connect = function() {
				$scope.room = roomId[0];
				if ($scope.room) {
					var socket = new SockJS('http://localhost:8080/testchat');
					$scope.stompClient = Stomp.over(socket);

					$scope.stompClient.connect({}, $scope.onConnected, $scope.onError);
				}
			};

			// Hàm onConnected
			$scope.onConnected = function() {
				$scope.stompClient.subscribe('/start/initial.' + $scope.room, $scope.onMessageReceived);
				$scope.stompClient.send(
					'/current/adduser.' + $scope.room,
					{},
					JSON.stringify({ sender: userIdSend, type: 'JOIN', chat: 'chat'})
				);
			};

			// Hàm onError
			$scope.onError = function(error) {
				// connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
				// connectingElement.style.color = 'red';
			};

			//Hàm ngắt kết nối
			$scope.disconnect = function() {
				if ($scope.stompClient && $scope.stompClient.connected) {
					$scope.stompClient.send(
					'/current/adduser.' + $scope.room,
					{},
					JSON.stringify({ sender: userIdSend, type: 'OUT', chat: 'Disconnect' })
				);
					$scope.stompClient.disconnect(function() {
						console.log('Đã ngắt kết nối với WebSocket.');
					});
				}
			};

			// Hàm $scope.send
			$scope.messageText = '';
			$scope.send = function() {
				var text = $scope.messageText;
				var messageContent = text;


				if (messageContent && $scope.stompClient) {
					var chatMessage = {
						sender: userIdSend,
						chat: text,
						content: text,
						type: 'CHAT'
					};
					var currentDate = new Date();
					var hour = currentDate.getHours();
					var minutes = currentDate.getMinutes();
					var time = hour + ":" + minutes;
					$scope.chatRoom.push({
						chatRoom: $scope.room,
						sender: { avatar: avatar1, userId: userIdSend },
						content: text,
						timeSend: hour + ":" + minutes
					});
					$scope.messageText = '';
					$scope.stompClient.send('/current/resume.' + $scope.room, {}, JSON.stringify(chatMessage));
				}
				//
				$http.get('/chat/createChat/' + $scope.room + "/" + userIdSend + "/" + userIdChat + "/" + text + "/" + time + "/" + true)
					.then(function(response) {
					});
				//
			};

			// Hàm onMessageReceived
			$scope.onMessageReceived = function(payload) {
				if (payload && payload.body) {
					var message = JSON.parse(payload.body);
					if (message.type === 'CHAT' && message.sender === userIdChat) {
						var currentDate = new Date();
						var hour = currentDate.getHours();
						var minutes = currentDate.getMinutes();
						var time = hour + ":" + minutes;
						if (message.sender === userIdChat) {
							$scope.chatRoom.push({
								chatRoom: $scope.room,
								sender: { avatar: avatar2, userId: userIdChat },
								content: message.content,
								timeSend: time
							});
						}
					}
				}
				// Cập nhật giao diện thủ công bằng $scope.$apply()
				$scope.$apply();
				//

			};
			//
			$scope.connect();
			// Hàm xử lý sự kiện khi nhấn phím Enter
			$scope.onKeyUp = function(event) {
				if (event.keyCode === 13) { // 13 là mã phím Enter
					$scope.send();
				}
			};
		})
		//
	}
	//
}); 