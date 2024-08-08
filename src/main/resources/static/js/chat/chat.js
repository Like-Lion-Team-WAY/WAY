let stompClient = null;

// 나중에 질문페이지에도 적용해야함
function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/messages/0', function (messageOutput) {// 채팅방 생성시 0번으로 메세지 보냄
            addNewChatRoomInfo(JSON.parse(messageOutput.body));
        });
    });
}

function addNewChatRoomInfo(messageOutput) {
    const userId = (Number)(document.getElementById('user-id').value);

    if (messageOutput.senderId === userId || messageOutput.receiverId === userId) {
        const chatListElement = $(".chat-room-list");
        const chatId = messageOutput.type.substring(6);

        const chatHtml = `
                    <a href="/chats/${chatId}" onclick="return openChat('/chats/${chatId}', ${chatId})">
                        <div class="chat-room-info center" id="chat-${chatId}">
                            <h3 class="chat-name left">${messageOutput.chatName}</h3>
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
    const userId = document.getElementById('user-id').value;

    if ((messageOutput.type === 'leave' && messageOutput.senderId === Number(userId)) || messageOutput.type === 'delete') {
        chatRoomElement.remove();
        subscription.unsubscribe();
        return;
    }

    if (chatRoomElement) {
        const lastChattingElement = chatRoomElement.querySelector('.last-chatting');
        const lastChattingTimeElement = chatRoomElement.querySelector('.last-chatting-time');
        lastChattingElement.textContent = messageOutput.text.length > 50 ? messageOutput.text.substring(0, 50) + '...' : messageOutput.text; // 메시지 내용 업데이트
        lastChattingTimeElement.textContent = messageOutput.sendTime; // 메시지 시간 업데이트

        const chatListElement = $(".chat-room-list")
        chatListElement.prepend(chatRoomElement);
    }
}

function loadChatList() {
    const chatListElement = $(".chat-room-list")

    $.ajax({
        url: '/api/chats',
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
        }
    });
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

