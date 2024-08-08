// header.js

document.addEventListener("DOMContentLoaded", function() {
    const bellBadge = document.getElementById("bellBadge");
    const sseUrl = `/sse/subscribe`; // sse 연결 주소

    function connectSSE() {
        // SSE 연결 설정
        const eventSource = new EventSource(sseUrl);
        console.log("[SSE] 이벤트 연결 시도");

        eventSource.addEventListener('count', function (event) {
            console.log("[SSE] count 데이터 수신");
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
            if (eventSource.readyState === EventSource.CLOSED) {
                connectSSE();
            } else {
                eventSource.close();
                setTimeout(connectSSE, 10 * 1000); // 재연결 시도
            }
        };
    }

    function updateBellBadge(count) {
        console.log("[SSE] update bell badge : " + count);

        if (count > 0) {
            bellBadge.style.display = "block";
            bellBadge.textContent = count;
            console.log("[SSE] alarm badge count : " +  bellBadge.textContent);
        } else {
            bellBadge.style.display = "none";
            console.log("[SSE] alarm badge has no count");
        }
    }

    connectSSE();
});