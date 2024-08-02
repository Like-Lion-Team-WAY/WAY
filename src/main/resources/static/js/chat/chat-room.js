let stompClient = null;
let chatId = null;

function getChatIdFromUrl() {
    // URL 경로에서 chatId 추출
    const path = window.location.pathname;
    const pathParts = path.split('/');
    chatId = pathParts[pathParts.length - 1]; // chatId가 URL의 마지막 부분에 위치

    if (!chatId) {
        console.error('chatId not found in URL');
        return;
    }

    connect(chatId);
}

function connect(chatId) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages/' + chatId, function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
            scrollToBottom();
        });
    });
}

function sendMessage() {
    if (!chatId) {
        console.error('chatId is not set');
        return;
    }
    const text = document.getElementById('message-input').value;
    stompClient.send("/app/sendMessage", {}, JSON.stringify({'chatId': chatId, 'text': text}));
    document.getElementById('message-input').value = '';
}

function showMessageOutput(messageOutput) {
    const chatBody = document.getElementsByClassName('container')[0];
    console.log(chatBody);

    const chatMessage = document.createElement('div');
    chatMessage.classList.add('chat-message');

    const userIcon = document.createElement('div');
    userIcon.classList.add('user-icon');
    userIcon.textContent = '유저네임';

    const text = document.createElement('div');
    text.classList.add('text');
    text.textContent = messageOutput.text;

    const responseTime = document.createElement('div');
    responseTime.classList.add('message-time');
    responseTime.textContent = messageOutput.createdAt;

    chatMessage.appendChild(userIcon);
    chatMessage.appendChild(text);
    chatMessage.appendChild(responseTime);

    chatBody.appendChild(chatMessage);
}

function scrollToBottom() {
    const chatContainer = document.getElementById('chat-container');
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

document.addEventListener('DOMContentLoaded', function() {
    getChatIdFromUrl();
    // 모든 메시지가 로드된 후 스크롤 이동
    scrollToBottom();
});