// header.js

document.addEventListener("DOMContentLoaded", function() {
    // 브라우저 창마다 고유한 ID를 생성
    if (!sessionStorage.getItem('windowID')) {
        sessionStorage.setItem('windowID', 'window-' + Date.now() + '-' + Math.random());
    }
    const windowID = sessionStorage.getItem('windowID');

    const bellBadge = document.getElementById("bellBadge");
    const sseUrl = `/sse/subscribe?windowId=${windowID}`; // sse 연결 주소

    function connectSSE() {
        // SSE 연결 설정
        const eventSource = new EventSource(sseUrl);
        console.log("[SSE] 이벤트 연결 시도");

        eventSource.addEventListener('count', function (event) {
            console.log("[SSE] count 데이터 수신");
            const data = JSON.parse(event.data);
            updateBellBadge(data.count);
        });

        eventSource.addEventListener('alarm', function (event) {
            console.log("[SSE] alarm 데이터 수신");
            const data = JSON.parse(event.data);  // 데이터 파싱

            if (window.location.pathname === "/alarm") {
                addAlarmBox(data.alarm);
            }
            updateBellBadge(data.count);
        });

        eventSource.addEventListener('ping', function (event) {
            console.log("[SSE] test connection");
        });

        // 이벤트 리스너 설정
        eventSource.onmessage = function (event) {
            const data = JSON.parse(event.data);
            updateBellBadge(data.count);
        };

        eventSource.onerror = function (event) {
            console.error("[SSE] connection error");
            if (eventSource.readyState === EventSource.CLOSED) {
                setTimeout(connectSSE, 10 * 1000); // todo: 비로그인 헤더 추가하면 타임아웃 없이 바로 재시도 하도록 수정할 것
            } else {
                eventSource.close();
                setTimeout(connectSSE, 1000);
            }
        };
    }

    function updateBellBadge(count) {
        console.log("[SSE] update bell badge : " + count);

        if (count > 0) {
            bellBadge.style.display = "block";
            bellBadge.textContent = count;
        } else {
            bellBadge.style.display = "none";
        }
    }

    // alarm.js
    function addAlarmBox(alarm) {
        const alarmContainer = document.getElementById('alarm-container');
        const noAlarms = document.getElementById('no-alarms');

        const container = document.createElement('div');
        container.className = 'alarm';

        container.innerHTML = `
            <p>${alarm.message}</p>
            <button class="btn-copy">보러 가기</button>
        `;

        const button = container.querySelector('.btn-copy');
        button.addEventListener('click', function() {
            handleButtonClick(alarm);
        });

        alarmContainer.prepend(container);
        if (noAlarms.style.display === 'block') {
            noAlarms.style.display = 'none';
        }
    }

    connectSSE();
});