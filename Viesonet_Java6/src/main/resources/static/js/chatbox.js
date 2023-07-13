'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var Id = generateRandomString(4);

function generateRandomString(length) {
	var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
	var result = '';

	for (var i = 0; i < length; i++) {
		var randomIndex = Math.floor(Math.random() * characters.length);
		result += characters.charAt(randomIndex);
	}

	return result;
}

function connect(event) {
	username = document.querySelector('#name').value.trim();

	if (username) {
		usernamePage.classList.add('hidden');
		chatPage.classList.remove('hidden');

		var socket = new SockJS('http://localhost:8080/testchat');
		stompClient = Stomp.over(socket);

		stompClient.connect({}, onConnected, onError);
	}
	event.preventDefault();
}


function onConnected() {
	// Subscribe to the Public Topic
	stompClient.subscribe('/start/initial.' + username, onMessageReceived);

	// Tell your username to the server
	stompClient.send("/current/adduser." + username,
		{},
		JSON.stringify({ sender: Id, type: 'JOIN', chat: "test" })
	)

	//connectingElement.classList.add('hidden');
}


function onError(error) {
	// connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	// connectingElement.style.color = 'red';
}


function send(event) {
	var messageContent = messageInput.value.trim();

	if (messageContent && stompClient) {
		var chatMessage = {
			sender: Id,
			chat: messageInput.value,
			content: messageInput.value,
			type: 'CHAT'
		};

		stompClient.send("/current/resume." + username, {}, JSON.stringify(chatMessage));
		messageInput.value = '';
	}
	event.preventDefault();
}


function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	if (message.type === 'CHAT') {

		var li = document.createElement('li');
		var div = document.createElement('div');
		var img = document.createElement('img');
		var div2 = document.createElement('div');
		var span = document.createElement('span');
		var p = document.createElement('p');
		var small = document.createElement('small');
		
		li.classList.add('mb-3');
		p.classList.add('card-subtitle', 'mt-1');
		if (message.sender === Id) {
			div.appendChild(div2);
			div.appendChild(img);
			div.classList.add('d-flex', 'justify-content-end');
			div2.classList.add('me-2');
			div2.setAttribute('id', 'div2');
			div2.style.display = 'block';
			div2.style.maxWidth = '400px';
			div2.style.wordWrap = 'break-word';
			div2.style.backgroundColor = '#7c9aeb';
			div2.style.borderRadius = '10px';
			div2.style.padding = '10px';
		} else {
			div.appendChild(img);
			div.appendChild(div2);
			div.classList.add('d-flex', 'align-items-start');
			div2.classList.add('ms-2');
			div2.setAttribute('id', 'div2');
			div2.style.display = 'block';
			div2.style.maxWidth = '400px';
			div2.style.wordWrap = 'break-word';
			div2.style.backgroundColor = '#eae7e7';
			div2.style.borderRadius = '10px';
			div2.style.padding = '10px';
		}

		img.classList.add('rounded-circle');
		img.setAttribute('src', '/images/user-1.jpg');
		img.setAttribute('width', '40');
		img.setAttribute('height', '40');
		img.style.objectFit = 'cover';

		span.textContent = message.content;

		var currentDate = new Date();
		var currentHour = currentDate.getHours();
		var currentMinute = currentDate.getMinutes();
		var currentSecond = currentDate.getSeconds();
		var amPM = currentHour >= 12 ? 'PM' : 'AM';

		// Chuyển đổi sang định dạng 12 giờ
		currentHour = currentHour % 12;
		currentHour = currentHour ? currentHour : 12;

		var currentTime = addLeadingZero(currentHour) + ':' + addLeadingZero(currentMinute) + ':' + addLeadingZero(currentSecond) + ' ' + amPM;

		small.textContent = currentTime;

		p.appendChild(small);
		div2.appendChild(span);
		div2.appendChild(p);

		li.appendChild(div);

		messageArea.appendChild(li);
	}
}

function addLeadingZero(number) {
	return number.toString().padStart(2, '0');
}


function createUniqueRoomId(username1, username2) {
	var sortedUsernames = [username1, username2].sort();
	return sortedUsernames.join('-');
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', send, true)
