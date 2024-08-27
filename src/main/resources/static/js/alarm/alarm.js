let page = 0;
const size = 20;
let isLoading = false;

// 새로운 전체 삭제 버튼 클릭 이벤트 함수
async function deleteAllAlarms() {
    try {
        // REST API 호출 (DELETE)
        const apiResponse = await fetch(`/api/alarm`, {
            method: 'DELETE'
        });

        if (!apiResponse.ok) {
            throw new Error(`Failed to delete all alarms: ${apiResponse.status}`);
        }
        document.getElementById('alarm-container').innerHTML = '';
        document.getElementById('no-alarms').style.display = 'block'; // 알림 없음 메시지 표시
        updateBellBadge(0);
    } catch (error) {
        console.error('전체 알림 삭제 실패:', error);
    }
}

// 기존 loadAlarmList 함수
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

// 스크롤 이벤트에 따른 알림 목록 로드
window.addEventListener('scroll', function () {
    if (window.scrollY + window.innerHeight >= document.documentElement.scrollHeight - 100) {
        loadAlarmList();
    }
});

// DOM이 로드되었을 때 첫 번째 알림 목록 로드
document.addEventListener('DOMContentLoaded', function () {
    loadAlarmList();

    // '알림 전체 삭제' 버튼 클릭 이벤트 리스너 추가
    const deleteAllButton = document.getElementById('delete-all-alarms-btn');
    if (deleteAllButton) {
        deleteAllButton.addEventListener('click', deleteAllAlarms);
    }
});
