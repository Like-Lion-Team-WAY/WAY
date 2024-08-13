$(document).ready(function() {
    $(".reportBtn").click(function (){
        var questionId = $(this).closest('.form-group').find('input[name="questionId"]').val();
        $.ajax({
            url: "/api/report",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ type: "QUESTION", id: questionId }),
            success: function(result) {
                alert("신고가 접수되었습니다.");
                $.ajax({
                    url: "/questions/delete",
                    method: "POST",
                    data: { questionId: questionId }, // JSON 대신 쿼리 파라미터 형식으로 전송
                    success: function(result) {
                        console.log(result);
                        window.location.href = "/questions/rejected";
                    },
                    error: function(err) {
                        console.log(err);
                    }
                });

            },
            error: function(err) {
                console.log(err);
                if (err.status === 401) {
                    alert('로그인이 필요합니다.');
                    window.location.href = '/user/login';
                } else if (err.status === 500) {
                    alert('서버 오류가 발생했습니다.');
                } else {
                    alert("신고 접수에 실패했습니다. 잠시후 다시 시도해주세요.");
                }
            }
        });
    });
});
