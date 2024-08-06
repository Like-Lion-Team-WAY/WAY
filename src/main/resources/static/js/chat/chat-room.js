let stompClient = null;
let chatId = null;
let isLoading = false;
let observer = null;
let lastLoadMessageId = "";

/////////
function noActiveSetting() {
    document.getElementById("change-name-btn").remove();
    document.getElementById("nickname-request-btn").remove();
    document.getElementById("report-btn").remove();
    document.getElementById("message-input").disabled = true;
    document.getElementById("submit-button").disabled = true;
}

function user2Setting() {
    document.getElementById("nickname-request-btn").remove();
}

////////// 메세지 통신 연결
function getChatIdFromUrl() {
    // URL 경로에서 chatId 추출
    const path = window.location.pathname;
    const pathParts = path.split('/');
    chatId = pathParts[pathParts.length - 1]; // chatId가 URL의 마지막 부분에 위치
}

function connect(chatId) {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages/' + chatId, function (messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
    });
}

////////// 메세지 송신
function submitBtn() {
    $("#input-form").submit(function (event) {
        event.preventDefault(); // 폼 제출의 기본 동작 방지
        const text = document.getElementById('message-input').value;
        sendMessage(text, 'message'); // sendMessage 함수 호출
    });
}

function sendMessage(text, type) {
    if (!chatId) {
        console.error('chatId is not set');
        return;
    }

    const userId = document.getElementById('user-id').value;
    stompClient.send("/app/sendMessage", {}, JSON.stringify({
        'chatId': chatId,
        'text': text,
        'senderId': userId,
        'type': type
    }));
    document.getElementById('message-input').value = '';
}

////////// 메세지 수신
function showMessageOutput(messageOutput) {
    const chatBody = document.getElementById('chat-container');

    const userId = Number(document.getElementById('user-id').value);
    const messageDiv = document.createElement('div');
    if (messageOutput.type !== "message") {
        messageDiv.classList.add('system-message');
        messageDiv.innerHTML = `
            <div class="text">${messageOutput.text}</div>
        `;
    } else if (messageOutput.senderId === userId) {
        messageDiv.classList.add('user-message');
        messageDiv.innerHTML = `
            <div class="text">${messageOutput.text}</div>
            <div class="message-time">${messageOutput.sendTime}</div>                    
       `;
    } else {
        messageDiv.classList.add('partner-message');
        messageDiv.innerHTML = `
            <div class="user-nickname">${messageOutput.userNickname}</div>
            <div class="text">${messageOutput.text}</div>
            <div class="message-time">${messageOutput.sendTime}</div>
        `;
    }

    chatBody.appendChild(messageDiv);

    if (messageOutput.senderId === userId) {
        scrollToBottom(false);
    }
}

////////// 이전 메세지 로드
function loadMessages(firstCall) {
    if (isLoading) return;
    isLoading = true; // 데이터 로드 시작

    const observerElement = $("#elementToObserve")

    $.ajax({
        url: '/api/messages/' + chatId + '?lastLoadMessageId=' + lastLoadMessageId,
        type: 'GET',
        success: function (response) {
            response.messages.forEach(function (message) {
                const userId = Number(document.getElementById('user-id').value);
                const messageDiv = document.createElement('div');
                if (message.type !== "message") {
                    messageDiv.classList.add('system-message');
                    messageDiv.innerHTML = `
                        <div class="text">${message.text}</div>
                    `;
                } else if (message.senderId === userId) {
                    messageDiv.classList.add('user-message');
                    messageDiv.innerHTML = `
                        <div class="text">${message.text}</div>
                        <div class="message-time">${message.sendTime}</div>                    
                    `;
                } else {
                    messageDiv.classList.add('partner-message');
                    messageDiv.innerHTML = `
                        <div class="user-nickname">${message.userNickname}</div>
                        <div class="text">${message.text}</div>
                        <div class="message-time">${message.sendTime}</div>
                    `;
                }

                observerElement.after(messageDiv);
                lastLoadMessageId = message.id;

                if (firstCall === true) {
                    scrollToBottom();
                }
            })

            if (response.lastPage) {
                observer.unobserve(document.getElementById('elementToObserve'));
            }

            isLoading = false; // 데이터 로드 완료
        }
    });
}

////////// 무한 스크롤 세팅
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

////////// 사이드바 설정
function openSide() {
    document.getElementById("side-menu").style.width = "250px";
}

function closeSide() {
    document.getElementById("side-menu").style.width = "0"
}

/////////// 채팅방 나가기
function leaveBtn() {
    $("#leave-btn").click(function () {
        const confirmation = confirm("채팅방을 나가시겠습니까?");

        if (confirmation) {
            leaveChat()
        }
    });
}

function leaveChat() {
    $.ajax({
        url: '/api/chats/leave/' + chatId,
        type: 'PUT',
        success: function (response) {
            sendMessage(response.text, response.result);
            window.open('', '_self').close();
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    const isActive = document.getElementById('is-active').value;
    const isUser2 = document.getElementById('is-user2').value;
    getChatIdFromUrl();

    if (isActive === 'false') {
        noActiveSetting();
    } else if (isUser2 === 'true') {
        user2Setting();
    }
    connect(chatId);
    loadMessages(true);
    setupIntersectionObserver();
    closeSide();
    submitBtn();
    leaveBtn();
});