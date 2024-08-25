function btnClick() {
    $('button').click(function () {
        if ($(this).hasClass('chat-btn')) {
            const questionId = ($(this).val());
            $.ajax({
                url: '/api/chats', // 서버의 엔드포인트 URL
                type: 'POST',
                data: {
                    'questionId': questionId
                },
                success: function (response) {
                    if (response.success) {
                        const chatId = response.data.chatId;
                        openChatRoom('/chats/' + chatId, chatId);
                    } else {
                        confirm("비회원의 질문에서 채팅은 불가합니다");
                    }
                }
            });
        }
    });
}

function openChatRoom(url, id) {
    window.open(url, 'chatPopup' + id, 'width=500,height=600');
    return false;
}

document.addEventListener('DOMContentLoaded', function () {
    btnClick();
});