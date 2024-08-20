let stompClient = null;
let chatId = null;
let isLoading = false;
let observer = null;
let lastLoadMessageId = "";

/////////
function noActiveSetting() {
    document.getElementById("change-name-btn").remove();
    document.getElementById("report-btn").remove();
    const requestBtn = document.getElementById("nickname-request-btn");
    if (requestBtn) {
        requestBtn.remove();
    }
    const requestBanner = document.querySelector('.request-banner');
    if (requestBanner) {
        requestBanner.remove();
        document.getElementById('chat-container').classList.remove('with-banner');
    }
    document.getElementById("message-input").disabled = true;
    document.getElementById("submit-button").disabled = true;
}

function removeNicknameRequestBtn() {
    document.getElementById("nickname-request-btn").remove();
}

function nicknameRequestingSetting(isRequesting) {
    const isQuestioner = document.getElementById('is-questioner').value;

    if (isRequesting === true) {
        addNicknameRequestingBanner(isQuestioner);
        if (isQuestioner === 'false') {
            const requestBtn = document.getElementById("nickname-request-btn");
            requestBtn.textContent = "닉네임 요청 취소";
            requestBtn.value = "cancel";
        }
    } else {
        removeNicknameRequestingBanner();
        if (isQuestioner === 'false') {
            const requestBtn = document.getElementById("nickname-request-btn");
            requestBtn.textContent = "닉네임 요청";
            requestBtn.value = "request";
        }
    }
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
        sendMessage('open', 'open');
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
    if (messageOutput.type === 'close') {
        return;
    }

    const chatBody = document.getElementById('chat-container');

    const isScrolledToBottom = chatBody.scrollHeight - chatBody.clientHeight <= chatBody.scrollTop + 1;
    const userId = Number(document.getElementById('user-id').value);

    if (messageOutput.type === 'open') {
        const unreadElements = document.querySelectorAll('.unread');
        unreadElements.forEach(element => element.remove());
        return;
    }

    const messageDiv = document.createElement('div');
    if (messageOutput.type !== "message") {
        messageDiv.classList.add('system-message');
        messageDiv.innerHTML = `
            <div class="text">${messageOutput.text}</div>
        `;

        if (messageOutput.type === 'change') {
            updateNameField(messageOutput.chatName);
            updateTitle(messageOutput.chatName);
        } else if (messageOutput.type === 'leave') {
            noActiveSetting();
        } else if (messageOutput.type === 'request') {
            nicknameRequestingSetting(true);
        } else if (messageOutput.type === 'cancel' || messageOutput.type === 'reject') {
            nicknameRequestingSetting(false);
        } else if (messageOutput.type === 'accept') {
            updateAcceptNickname(messageOutput.userNickname);
        }

    } else if (messageOutput.senderId === userId) {
        messageDiv.classList.add('user-message');
        messageDiv.innerHTML = `
            <div class="text-group">
                ${!messageOutput.isRead ? '<div class="unread">안읽음</div>' : ''}
                <div class="text">${messageOutput.text}</div>
            </div>
            <div class="message-time">${messageOutput.sendTime}</div>        
       `;
    } else {
        messageDiv.classList.add('partner-message');
        messageDiv.innerHTML = `
            <div class="user-nickname">${messageOutput.userNickname}</div>
                <div class="text-group">
                    <div class="text">${messageOutput.text}</div>
                    <div class="report-chat-btn">신고</div>                        
                </div>
            <div class="message-time">${messageOutput.sendTime}</div>
        `;
        messageDiv.querySelector('.report-chat-btn').setAttribute('value', messageOutput.id);
    }

    chatBody.appendChild(messageDiv);

    if (isScrolledToBottom || messageOutput.senderId === Number(userId)) {
        scrollToBottom();
    }
}

////////// 이전 메세지 로드
function loadMessages(firstCall) {
    console.log("hi");
    if (isLoading) return;
    isLoading = true; // 데이터 로드 시작

    const observerElement = $("#elementToObserve")

    $.ajax({
        url: '/api/messages/' + chatId + '?lastLoadMessageId=' + lastLoadMessageId,
        type: 'GET',
        success: function (response) {
            response.messages.forEach(function (message) {
                console.log(message.isRead + ' ' + message.id);

                if (message.isRead) {
                    console.log("안녕");
                }
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
                        <div class="text-group">
                            ${!message.isRead ? '<div class="unread">안읽음</div>' : ''}
                            <div class="text">${message.text}</div>
                        </div>
                        <div class="message-time">${message.sendTime}</div>           
                    `;
                } else {
                    messageDiv.classList.add('partner-message');
                    messageDiv.innerHTML = `
                        <div class="user-nickname">${message.userNickname}</div>
                        <div class="text-group">
                            <div class="text">${message.text}</div>
                            <div class="report-chat-btn">신고</div>                        
                        </div>
                        <div class="message-time">${message.sendTime}</div>
                    `;
                    messageDiv.querySelector('.report-chat-btn').setAttribute('value', message.id);
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
        type: 'PATCH',
        success: function (response) {
            sendMessage(response.text, response.result);
            window.open('', '_self').close();
        }
    });
}

/////// 채팅방 이름 변경
function changNameBtn() {
    $("#change-name-btn").click(function () {

        $(".chat-name h3").hide();
        $("#change-name-btn").hide();
        const oldName = $(".chat-name h3").text();

        const changeNameHtml = `
            <form id="edit-chat-name">
                <input type="text" id="new-chat-name" value="${oldName}" placeholder="새 이름 입력" autocomplete="off">
                <button type=submit id="edit-name-btn">수정</button>
            </form>
            <button id="change-name-cancel-btn">취소</button>
        `;

        $(".chat-name").append(changeNameHtml);

        submitEditName(oldName);
        changeNameCancelBtn(oldName);
    });
}

function submitEditName(oldName) {
    $("#edit-chat-name").submit(function (event) {
        event.preventDefault(); // 폼 제출의 기본 동작 방지
        const newName = document.getElementById('new-chat-name').value;
        editName(newName, oldName);
    });
}

function editName(newName, oldName) {
    $.ajax({
        url: '/api/chats/name/' + chatId,
        type: 'PATCH',
        data: {
            'newName': newName
        },
        success: function (response) {
            if (response.result === 'noChange') {
                updateNameField(oldName);
            } else {
                sendMessage(response.text, response.result);
            }
        }
    });
}

function changeNameCancelBtn(oldName) {
    $("#change-name-cancel-btn").click(function () {
        updateNameField(oldName);
    });

}

function updateNameField(name) {
    $(".chat-name h3").text(name).show();
    $(".title").text(name);
    $("#change-name-btn").show();
    $("#edit-chat-name").remove();
    $("#change-name-cancel-btn").remove();
}

function updateTitle(name) {
    $('title').text('채팅방 [' + name + ']');
}

////// 닉네임 요청
function nicknameRequestBtn() {
    $("#nickname-request-btn").click(function () {
        if (this.value === 'request') {
            confirmNickname("상대에게 닉네임을 요청하시겠습니까?", 'request');
        } else {
            confirmNickname("상대 닉네임 요청을 취소하시겠습니까?", 'cancel');
        }
    });
}

function requestBtns() {
    document.body.addEventListener('click', function (event) {
        if (event.target) {
            if (event.target.id === 'cancel-request') {
                confirmNickname("상대 닉네임 요청을 취소하시겠습니까?", 'cancel');
            } else if (event.target.id === 'accept-request') {
                confirmNickname("닉네임 요청을 수락하시겠습니까?", 'accept');
            } else if (event.target.id === 'reject-request') {
                confirmNickname("닉네임 요청을 거절하시겠습니까?", 'reject');
            }
        }
    });
}

function confirmNickname(message, type) {
    const confirmation = confirm(message);

    if (confirmation) {
        callNicknameApi(type);
    }
}

function callNicknameApi(type) {
    let url;
    if (type === 'request' || type === 'cancel') {
        url = 'request';
    } else {
        url = 'response';
    }

    $.ajax({
        url: '/api/chats/nickname-' + url + '/' + chatId,
        type: 'PATCH',
        data: {
            'type': type
        },
        success: function (response) {
            sendMessage(response.text, response.result);
        }
    });
}

function addNicknameRequestingBanner(isQuestioner) {
    let bannerHtml;

    if (isQuestioner === 'true') {
        bannerHtml = `
            <div class="request-banner">
                <div class="request-stat" id="requested-user">
                    <div>상대방이 닉네임을 요청하였습니다</div>
                    <div class="button-container">
                        <button id="accept-request">수락</button>
                        <button id="reject-request">거절</button>
                    </div>
                </div>
            </div>
        `;

    } else {
        bannerHtml = `
            <div class="request-banner">
                <div class="request-stat" id="request-user">
                    <div>상대방에게 닉네임을 요청하였습니다</div>
                    <div class="button-container">
                        <button id="cancel-request">취소</button>
                    </div>
                </div>
            </div>
        `
    }

    document.querySelector('.open-btn').insertAdjacentHTML('afterend', bannerHtml);
    document.getElementById('chat-container').classList.add('with-banner');
}

function removeNicknameRequestingBanner() {
    document.querySelector('.request-banner').remove();
    document.getElementById('chat-container').classList.remove('with-banner');
}

function updateAcceptNickname(nickname) {
    removeNicknameRequestingBanner();
    const isQuestioner = document.getElementById('is-questioner').value;
    if (isQuestioner === 'false') {
        document.getElementById("nickname-request-btn").remove();
        const nicknameElements = document.getElementsByClassName("user-nickname");

        for (let i = 0; i < nicknameElements.length; i++) {
            nicknameElements[i].textContent = nickname;
        }
    }
}

/////// 신고하기
function reportChat() {
    document.body.addEventListener('click', function (event) {
        const target = event.target;
        if (target.classList.contains('report-chat-btn')) {
            if (confirm('해당 메세지를 신고하시겠습니까?')) {
                callReportApi(target.getAttribute('value'));
            }
        }
    });
}

function callReportApi(messageId){
    $.ajax({
        url: "/api/report",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ type: "CHATTING", id: messageId }),
        success: function(result) {
            alert("신고가 접수되었습니다.");
        },
        error: function(err) {
            console.log(err);
            if (err.status === 401) {
                alert('로그인이 필요합니다.');
                window.location.href = '/user/login';
            } else if (err.status === 500) {
                alert('서버 오류가 발생했습니다.');
            } else {
                alert("신고 접수에 실패했습니다. 잠시후 다시 시도해주세요.");
            }
        }
    });
}

/////////////////

document.addEventListener('DOMContentLoaded', function () {
    const isActive = document.getElementById('is-active').value;

    const isQuestioner = document.getElementById('is-questioner').value;
    const isNicknameOpen = document.getElementById('is-nicknameOpen').value;
    getChatIdFromUrl();
    connect(chatId);

    if (isActive === 'false') {
        noActiveSetting();
    } else {
        if (isNicknameOpen === '1') {
            nicknameRequestingSetting(true);
        }

        if (isQuestioner === 'true' || isNicknameOpen === '2') {
            removeNicknameRequestBtn();
        } else {
            nicknameRequestBtn();
        }

        requestBtns();
    }

    loadMessages(true);
    setupIntersectionObserver();
    closeSide();
    submitBtn();
    leaveBtn();
    changNameBtn();
    reportChat();
});

window.onbeforeunload = function (event) {
    sendMessage("close", "close");
};