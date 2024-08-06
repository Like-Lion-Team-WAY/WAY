// header.js

document.addEventListener("DOMContentLoaded", function() {
    const bellBadge = document.getElementById("bellBadge");
    const userId = 1; // todo
    const sseUrl = `/sse/subscribe/${userId}`; // sse 연결 주소

    // SSE 연결 설정
    const eventSource = new EventSource(sseUrl);
    console.log("[SSE] 이벤트 연결 시도");

    eventSource.addEventListener('subscribe', function (event) {
        const data = event.data;
        console.log("[SSE] subscribe : " + data);
    });

    eventSource.addEventListener('count', function (event) {
        const count = event.data;
        updateBellBadge(count);
    });

    // 이벤트 리스너 설정
    eventSource.onmessage = function (event) {
        const data = JSON.parse(event.data);
        updateBellBadge(data);
    };

    eventSource.onerror = function (event) {
        console.error("[SSE] connection error");
        eventSource.close();
    };

    function updateBellBadge(count) {
        console.log("[SSE] update bell badge : " + count);
        if (count > 0) {
            bellBadge.style.display = "block";
            bellBadge.innerText = count;
        } else {
            bellBadge.style.display = "none";
        }
    }
});