let stompClient = null;
let chatId = null;
let isLoading = false;
let observer = null;
let lastLoadMessageId = "";


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
    const userId = document.getElementById('user-id').value;
    console.log(userId);
    stompClient.send("/app/sendMessage", {}, JSON.stringify({'chatId': chatId, 'text': text, 'userId': userId, 'type': 'message'}));
    document.getElementById('message-input').value = '';
}

function showMessageOutput(messageOutput) {
    const chatBody = document.getElementById('chat-container');

    const chatMessage = document.createElement('div');
    chatMessage.classList.add('chat-message');

    const userIcon = document.createElement('div');
    userIcon.classList.add('user-icon');
    userIcon.textContent = messageOutput.userNickname;

    const text = document.createElement('div');
    text.classList.add('text');
    text.textContent = messageOutput.text;

    const responseTime = document.createElement('div');
    responseTime.classList.add('message-time');
    responseTime.textContent = messageOutput.sendTime;

    chatMessage.appendChild(userIcon);
    chatMessage.appendChild(text);
    chatMessage.appendChild(responseTime);

    chatBody.appendChild(chatMessage);
}

function loadMessages() {
    if (isLoading) return;
    isLoading = true; // 데이터 로드 시작

    const observerElement = $("#elementToObserve")

    $.ajax({
        url: '/api/messages/' + chatId + '?lastLoadMessageId=' + lastLoadMessageId,
        type: 'GET',
        success: function (response) {
            console.log(response);
            response.messages.forEach(function (message){
                const messageDiv = document.createElement('div');
                messageDiv.classList.add('chat-message');
                messageDiv.innerHTML = `
                    <div class="user-icon">${message.userNickname}</div>
                    <div class="text">${message.text}</div>
                    <div class="message-time">${message.sendTime}</div>
                `;

                observerElement.after(messageDiv);
                lastLoadMessageId = message.id;
            })

            if (response.lastPage) {
                observer.unobserve(document.getElementById('elementToObserve'));
            }

            isLoading = false; // 데이터 로드 완료
        }
    });
}

function setupIntersectionObserver() {
    const scrollArea = document.getElementById('chat-container');
    observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                loadMessages();
            }
        });
    }, {
        root: scrollArea, // chatContainer가 스크롤의 컨테이너 역할을 하므로 root로 설정
        rootMargin: '0px',
        threshold: 1.0
    });

    observer.observe(document.getElementById('elementToObserve'));
}

function scrollToBottom() {
    const chatContainer = document.getElementById('chat-container');
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

document.addEventListener('DOMContentLoaded', function () {
    getChatIdFromUrl();
    loadMessages();
    scrollToBottom();
    setupIntersectionObserver();
});