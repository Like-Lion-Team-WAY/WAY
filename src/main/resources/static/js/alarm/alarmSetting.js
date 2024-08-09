document.addEventListener('DOMContentLoaded', function () {
    const switches = document.querySelectorAll('.switch input[type="checkbox"]');

    initToggles();

    switches.forEach(function (toggle) {
        toggle.addEventListener('change', function () {
            const alarmType = this.getAttribute('data-id');
            const isEnabled = this.checked;

            updateAlarmSetting(alarmType, isEnabled);
        });
    });

    function initToggles() {
        fetch('/api/alarm/setting', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 올바르지 않습니다.');
                }
                return response.json();
            })
            .then(data => {
                switches.forEach(function (toggle) {
                    const alarmType = toggle.getAttribute('data-id');
                    if (data.hasOwnProperty(alarmType)) {
                        toggle.checked = data[alarmType];
                    }
                });
            })
            .catch(error => {
                console.error('알람 설정을 가져오는 중 오류 발생:', error);
            });
    }

    function updateAlarmSetting(type, isEnabled) {
        fetch('/api/alarm/setting', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                alarmType: type,
                enabled: isEnabled
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 올바르지 않습니다.');
                }
                return response.json();
            })
            .then(data => {
                console.log('알림 설정 업데이트 성공:', data);
            })
            .catch(error => {
                console.error('알림 설정 업데이트 중 오류 발생:', error);
            });
    }
});
