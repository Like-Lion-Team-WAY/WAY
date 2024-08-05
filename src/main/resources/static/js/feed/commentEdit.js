document.addEventListener('DOMContentLoaded', function () {
    // Edit button click handler
    document.querySelectorAll('.edit-comment-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = button.getAttribute('data-comment-id');
            const editForm = document.getElementById(`editCommentForm-${commentId}`);

            if (editForm) {
                editForm.style.display = 'block';
            } else {
                console.error(`Edit form not found for comment ID: ${commentId}`);
            }
        });
    });

    // Cancel button click handler
    document.querySelectorAll('.cancel-edit-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = button.getAttribute('data-comment-id');
            const editForm = document.getElementById(`editCommentForm-${commentId}`);

            if (editForm) {
                editForm.style.display = 'none';
            } else {
                console.error(`Edit form not found for comment ID: ${commentId}`);
            }
        });
    });

    // Save button click handler
    document.querySelectorAll('.save-comment-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = button.getAttribute('data-comment-id');
            const updatedContent = document.getElementById(`editCommentContent-${commentId}`).value;
            console.log(commentId, updatedContent);
            $.ajax({
                url: `/posts/comments/`+commentId,
                method: 'PATCH',
                contentType: 'application/json',
                data: JSON.stringify({updatedContent: updatedContent}),
                success: function (result) {
                    alert('댓글이 수정되었습니다.');
                    window.location.reload();
                },
                error: function (err) {
                    console.log(err);
                    alert('댓글 수정에 실패했습니다. 다시 시도해주세요.');
                }
            });

            const editForm = document.getElementById(`editCommentForm-${commentId}`);
            if (editForm) {
                editForm.style.display = 'none';
            } else {
                console.error(`Edit form not found for comment ID: ${commentId}`);
            }
        });
    });

    // Delete button click handler
    document.querySelectorAll('.delete-comment-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = button.getAttribute('data-comment-id');
            if (confirm('댓글을 삭제하시겠습니까?')) {
                $.ajax({
                    url: `/posts/comments/${commentId}`,
                    method: 'DELETE',
                    success: function (result) {
                        alert('댓글이 삭제되었습니다.');
                        window.location.reload();
                    },
                    error: function (err) {
                        console.log(err);
                        alert('댓글 삭제에 실패했습니다. 다시 시도해주세요.');
                    }
                });
            }
        });
    });
});
