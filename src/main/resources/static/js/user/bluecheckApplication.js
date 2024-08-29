$(document).ready(function () {
    $('#bluecheckApplicationForm').submit(function (e) {
        e.preventDefault();

        const userId = $('input[name="userId"]').val();

        $.ajax({
            url: '/api/bluecheck/application',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({userId: userId}),
            success: function (response) {
                if (response.success) {
                    alert('파란 배지 신청이 완료되었습니다..');
                } else {
                    alert('이미 파란 배지를 신청했거나 신청할 수 없는 사용자입니다.');
                }
            }
        });
    });
});
