$(document).ready(function() {

    // 답변 수정 버튼 클릭
    $(document).on('click', '.edit-button', function() {
        $('.answer-text').hide();
        $('.edit-button').hide();
        $('#editForm').show();
    });

    // 수정된 답변 저장
    $('#editForm').on('submit', function(event) {
        event.preventDefault();

        const form = $(this);
        const url = form.attr('action');
        const editedAnswer = form.find('input[name="editedAnswer"]').val();

        $.ajax({
            type: 'PATCH',
            url: url,
            data: { answer: editedAnswer },
            success: function(response) {
                $('#editForm').hide();
                window.location.reload();
            },
            error: function() {
                alert('답변 수정에 실패했습니다.');
            }
        });
    });

    // 취소 버튼 클릭
    $(document).on('click', '.cancel-button', function() {
        $('#editForm').hide();
        $('.answer-text').show();
        $('.edit-button').show();
    });
});