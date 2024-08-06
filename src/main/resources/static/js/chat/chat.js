let stompClient = null;
let currentPage = 1;
let isLoading = false;
let observer = null;


function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
    });
}

function subscribeToChat(chatId) {
    stompClient.subscribe('/topic/messages/' + chatId, function (messageOutput) {
        updateLastMessage(JSON.parse(messageOutput.body));
    });
}

function updateLastMessage(messageOutput) {
    const chatId = messageOutput.chatId;
    const chatRoomElement = document.getElementById(`chat-${chatId}`);
    if (chatRoomElement) {
        const lastChattingElement = chatRoomElement.querySelector('.last-chatting');
        const lastChattingTimeElement = chatRoomElement.querySelector('.last-chatting-time');
        lastChattingElement.textContent = messageOutput.text.length > 50 ? messageOutput.text.substring(0, 50) + '...' : messageOutput.text; // 메시지 내용 업데이트
        lastChattingTimeElement.textContent = messageOutput.sendTime; // 메시지 시간 업데이트
    }
}

function loadChatList() {
    if (isLoading) return;
    isLoading = true; // 데이터 로드 시작

    const chatListElement = $(".chat-room-list")

    $.ajax({
        url: '/api/chats?page=' + currentPage,
        type: 'GET',
        success: function (response) {
            response.chats.forEach(function (chat) {
                const chatHtml = `
                    <a href="/chats/${chat.id}" onclick="return openChat('/chats/${chat.id}', ${chat.id})">
                        <div class="chat-room-info center" id="chat-${chat.id}">
                            <h3 class="chat-name left">${chat.name}</h3>
                            <div class="last-chatting left">${chat.lastMessage}</div>
                            <div class="last-chatting-time right">${chat.lastMessageTime}</div>
                        </div>
                    </a>
                `;

                chatListElement.append(chatHtml);

                subscribeToChat(chat.id);
            })

            currentPage++;

            if (response.lastPage) {
                observer.unobserve(document.getElementById("elementToObserve"));
            }

            isLoading = false; // 데이터 로드 완료
        }
    });
}

function setupIntersectionObserver() {
    observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                loadChatList();
            }
        });
    }, {
        root: null,
        rootMargin: '0px',
        threshold: 1.0
    });

    observer.observe(document.getElementById('elementToObserve'));
}

document.addEventListener('DOMContentLoaded', function () {
    loadChatList();
    connect();
    setupIntersectionObserver();

    $('button').click(function(){
        const questionId = $(this).val();
        $.ajax({
            url: '/api/chats', // 서버의 엔드포인트 URL
            type: 'POST',
            data: {
                'questionId': questionId
            },
            success: function(response) {
                console.log(response.message);
            }
        });
    });
});

function openChat(url, id) {
    window.open(url, 'chatPopup' + id, 'width=400,height=600');
    return false;
}

