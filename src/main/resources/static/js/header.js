// header.js

document.addEventListener("DOMContentLoaded", function() {
    const bellBadge = document.getElementById("bellBadge");
    const userId = 1; // todo
    const sseUrl = `/sse/subscribe/${userId}`; // sse 연결 주소

    if (bellBadge) {
        // Add more logging to verify element's properties
        console.log("Initial bellBadge display style:", getComputedStyle(bellBadge).display);
    }

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
            eventSource.close();
            setTimeout(connectSSE, 3 * 1000); // 재연결 시도
        };
    }

    function updateBellBadge(count) {
        console.log("[SSE] update bell badge : " + count);

        if (count > 0) {
            bellBadge.style.display = "block";
            bellBadge.textContent = count; // innerText, setAttribute... 다 안됨 시발
            console.log("[SSE] alarm badge count : " +  bellBadge.textContent);

            // // 강제로 DOM을 업데이트
            // bellBadge.style.visibility = "hidden"; // 임시로 숨김
            // bellBadge.offsetHeight; // reflow 강제
            // bellBadge.style.visibility = "visible"; // 다시 보이게 설정

        } else {
            bellBadge.style.display = "none";
            console.log("[SSE] alarm badge has no count");
        }
    }

    connectSSE();
});