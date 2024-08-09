let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/messages/0', function (messageOutput) {
            addNewChatRoomInfo(JSON.parse(messageOutput.body));
        });
    });
}

function addNewChatRoomInfo(messageOutput) {
    const userId = (Number)(document.getElementById('user-id').value);

    if (messageOutput.senderId === userId || messageOutput.receiverId === userId) {
        const chatListElement = $(".chat-room-list");
        const chatId = messageOutput.type.substring(6);

        const newMessageElement = messageOutput.senderId === userId
            ? '<div class="new-message" style="display: none;">NEW</div>'
            : '<div class="new-message">NEW</div>';

        const chatHtml = `
                    <a href="/chats/${chatId}" onclick="return openChat('/chats/${chatId}', ${chatId})">
                        <div class="chat-room-info center" id="chat-${chatId}">
                            <div class="top">
                                <h3 class="chat-name left">${messageOutput.chatName}</h3>
                                ${newMessageElement}
                            </div>
                            <div class="last-chatting left">${messageOutput.text}</div>
                            <div class="last-chatting-time right">${messageOutput.sendTime}</div>
                        </div>
                    </a>
                `;

        chatListElement.prepend(chatHtml);

        subscribeToChat(chatId);
    }
}

function subscribeToChat(chatId) {
    const subscription = stompClient.subscribe('/topic/messages/' + chatId, function (messageOutput) {
        updateChatRoomInfo(JSON.parse(messageOutput.body), subscription);
    });
}

function updateChatRoomInfo(messageOutput, subscription) {
    const chatId = messageOutput.chatId;

    const chatRoomElement = document.getElementById(`chat-${chatId}`).closest('a');
    const userId = Number(document.getElementById('user-id').value);

    if ((messageOutput.type === 'leave' && messageOutput.senderId === userId) || messageOutput.type === 'delete') {
        chatRoomElement.remove();
        subscription.unsubscribe();
        return;
    }


    if (chatRoomElement) {
        const lastChattingElement = chatRoomElement.querySelector('.last-chatting');
        const lastChattingTimeElement = chatRoomElement.querySelector('.last-chatting-time');
        lastChattingElement.textContent = messageOutput.text.length > 50 ? messageOutput.text.substring(0, 50) + '...' : messageOutput.text; // 메시지 내용 업데이트
        lastChattingTimeElement.textContent = messageOutput.sendTime; // 메시지 시간 업데이트
        if (messageOutput.isRead === true || messageOutput.senderId === userId) {
            chatRoomElement.querySelector(`.new-message`).style.display = 'none';
        } else {
            chatRoomElement.querySelector(`.new-message`).style.display = 'block';
        }

        const chatListElement = $(".chat-room-list")
        chatListElement.prepend(chatRoomElement);
    }
}

function loadChatList() {
    const userId = (Number)(document.getElementById('user-id').value);
    const chatListElement = $(".chat-room-list")

    $.ajax({
        url: '/api/chats',
        type: 'GET',
        success: function (response) {
            response.chats.forEach(function (chat) {
                const formattedLastMessageTime = formatDateTime(chat.lastMessageTime);

                const newMessageElement = (chat.isRead === true || chat.senderId === userId)
                    ? '<div class="new-message" style="display: none;">NEW</div>'
                    : '<div class="new-message">NEW</div>';

                const chatHtml = `
                    <a href="/chats/${chat.id}" onclick="return openChat('/chats/${chat.id}', ${chat.id})">
                        <div class="chat-room-info" id="chat-${chat.id}">
                            <div class="top">
                                <h3 class="chat-name">${chat.name}</h3>
                                ${newMessageElement}                  
                            </div>
                            <div class="last-chatting left">${chat.lastMessage}</div>
                            <div class="last-chatting-time right">${formattedLastMessageTime}</div>
                        </div>
                    </a>
                `;

                chatListElement.append(chatHtml);

                subscribeToChat(chat.id);
            })
        }
    });
}

function formatDateTime(dateTimeStr) {
    const date = new Date(dateTimeStr);

    const year = String(date.getFullYear()).slice(-2); // 연도의 마지막 두 자리를 가져옴
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}.${month}.${day} ${hours}:${minutes}`;
}

document.addEventListener('DOMContentLoaded', function () {
    loadChatList();
    connect();

    // 나중에 질문페이지로 옳길 예정
    $('button').click(function () {
        const questionId = $(this).val();
        $.ajax({
            url: '/api/chats', // 서버의 엔드포인트 URL
            type: 'POST',
            data: {
                'questionId': questionId
            },
            success: function (response) {
            }
        });
    });
});

function openChat(url, id) {
    window.open(url, 'chatPopup' + id, 'width=500,height=600');
    return false;
}

