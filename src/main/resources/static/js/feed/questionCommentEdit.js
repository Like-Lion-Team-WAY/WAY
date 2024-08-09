$(document).ready(function() {

    // 답변 수정 버튼 클릭
    $(document).on('click', '.edit-button', function() {
        // 현재 클릭된 버튼과 연관된 폼만 처리
        const form = $(this).closest('.form-group').next('.editForm');

        // 기존 답변 텍스트와 수정 버튼 숨기기
        $(this).closest('.form-group').prev('.answer-text').hide();
        $(this).hide();

        // 수정 폼 보여주기
        form.show();
    });

    // 수정된 답변 저장
    $(document).on('submit', '.editForm', function(event) {
        event.preventDefault();

        const form = $(this);
        const url = form.attr('action');
        const editedAnswer = form.find('input[name="editedAnswer"]').val();

        $.ajax({
            type: 'PATCH',
            url: url,
            data: { answer: editedAnswer },
            success: function(response) {
                form.hide();
                window.location.reload();
            },
            error: function() {
                alert('답변 수정에 실패했습니다.');
            }
        });
    });

    // 취소 버튼 클릭
    $(document).on('click', '.cancel-button', function() {
        const form = $(this).closest('.editForm');
        form.hide();
        form.prev('.form-group').find('.edit-button').show();
        form.prev('.form-group').prev('.answer-text').show();
    });
});
