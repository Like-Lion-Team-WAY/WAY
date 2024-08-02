let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages/1', function (messageOutput) {
            updateLastMessage(JSON.parse(messageOutput.body));
        });
        stompClient.subscribe('/topic/messages/2', function (messageOutput) {
            updateLastMessage(JSON.parse(messageOutput.body));
        });
    });
}

function updateLastMessage(messageOutput) {
    const chatId = messageOutput.chatId;
    const chatRoomElement = document.getElementById(`chat-${chatId}`);
    if (chatRoomElement) {
        const lastChattingElement = chatRoomElement.querySelector('.last-chatting');
        const lastChattingTimeElement = chatRoomElement.querySelector('.last-chatting-time');
        lastChattingElement.textContent = messageOutput.text; // 메시지 내용 업데이트
        lastChattingTimeElement.textContent = messageOutput.createdAt; // 메시지 시간 업데이트
    }
}

document.addEventListener('DOMContentLoaded', function () {
    connect();
});

function openChat(url, id) {
    window.open(url, 'chatPopup' + id, 'width=400,height=600');
    return false;
}