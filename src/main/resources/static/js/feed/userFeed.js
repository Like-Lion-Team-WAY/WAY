function btnClick() {
    $('button').click(function () {
        if ($(this).hasClass('chat-btn')) {
            console.log("hi");
            const questionId = $(this).val();
            console.log(questionId);
            $.ajax({
                url: '/api/chats', // 서버의 엔드포인트 URL
                type: 'POST',
                data: {
                    'questionId': questionId
                },
                success: function (response) {
                    const chatId = response.chatId;
                    openChatRoom('/chats/' + chatId, chatId);
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