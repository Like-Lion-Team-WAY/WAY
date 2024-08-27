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
                alert('파란 배지 신청이 완료되었습니다.');
            },
            error: function (error) {
                alert('신청 중 오류가 발생했습니다.');
            }
        });
    });
});
