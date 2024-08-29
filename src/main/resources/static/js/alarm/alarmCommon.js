// 기존 handleButtonClick 함수
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

function updateBellBadge(count) {
    console.log("[SSE] update bell badge : " + count);

    if (count > 0) {
        bellBadge.style.display = "block";
        bellBadge.textContent = count;
    } else {
        bellBadge.style.display = "none";
    }
}