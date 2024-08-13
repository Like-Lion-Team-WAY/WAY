let page = 0;
const size = 20;
let isLoading = false;

async function handleButtonClick(alarm) {
    try {
        // REST API 호출
        const apiResponse = await fetch(`/api/alarm/${alarm.id}`, {
            method: 'DELETE'
        });

        if (!apiResponse.ok) {
            throw new Error(`Failed to call API : ${apiResponse.status}`);
        }

        // 성공적으로 API 호출이 완료된 후 링크 이동
        window.location.href = alarm.url;
    } catch (error) {
        console.error('API 호출 실패:', error);
    }
}

async function loadAlarmList() {
    if (isLoading) return; // 이미 로딩 중이면 추가 요청 방지
    isLoading = true;
    console.log('loading......');

    try {
        const response = await fetch(`/api/alarm?page=${page}&size=${size}`);
        if (!response.ok) {
            throw new Error(`Failed to load alarm list : ${response.status}`);
        }
        const alarmMessagePages = await response.json();
        const alarms = alarmMessagePages.content;

        if (alarms.length === 0 && page === 0) {
            // 알림이 없고 첫 페이지인 경우, "알림이 없습니다" 메시지 표시
            document.getElementById('no-alarms').style.display = 'block';
            console.log('알림이 없습니다 표시');
        } else {
            // "알림이 없습니다" 메시지 숨기기
            document.getElementById('no-alarms').style.display = 'none';
            console.log('알림이 없습니다 숨김');
        }

        alarms.forEach(alarm => {
            console.log(alarm);
            const container = document.createElement('div');
            container.className = 'alarm';

            container.innerHTML = `
                        <p>${alarm.message}</p>
                        <button class="btn-copy">보러 가기</button>
                    `;
            document.getElementById('alarm-container').appendChild(container);

            const button = container.querySelector('.btn-copy');
            button.addEventListener('click', function() {
                handleButtonClick(alarm); // 클릭 시 handleButtonClick 함수 호출
            });
        });

        // loading
        if (alarms.length > 0) {
            page++;
        }
        isLoading = false;
    } catch (error) {
        console.error(error);
        isLoading = false;
    }
}

window.addEventListener('scroll', function () {
    if (window.scrollY + window.innerHeight >= document.documentElement.scrollHeight - 100) {
        loadAlarmList();
    }
});

document.addEventListener('DOMContentLoaded', function () {
    loadAlarmList();
});
