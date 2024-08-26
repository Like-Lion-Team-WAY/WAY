document.addEventListener('DOMContentLoaded', function () {
    $('.comment-input').on('submit', function(event) {
        event.preventDefault();

        var postId = $(this).find('input[name="postId"]').val();
        var userId = $(this).find('input[name="userId"]').val();
        var postCommentContent = $(this).find('input[name="postCommentContent"]').val();

        $.ajax({
            url: `/posts/comments/${postId}`,
            type: 'POST',
            data: {
                userId: userId,
                postCommentContent: postCommentContent
            },
            success: function(response) {
                alert('댓글이 성공적으로 저장되었습니다.');
                location.reload(); // 페이지 새로고침
            },
            error: function(xhr, status, error) {
                alert('댓글 저장에 실패했습니다.');
                console.error(xhr.responseText);
            }
        });
    });

    // 답글 작성 폼 표시 및 숨기기
    $('.reply-comment-btn').click(function() {
        var commentId = $(this).data('comment-id');
        $('#replyCommentForm-' + commentId).show();
    });

    $('.cancel-reply-btn').click(function() {
        var commentId = $(this).data('comment-id');
        $('#replyCommentForm-' + commentId).hide();
    });

    $('.submit-reply-btn').click(function(event) {
        event.preventDefault();
        var commentId = $(this).data('comment-id');
        var replyContent = $('#replyCommentContent-' + commentId).val();
        var postId = $('input[name="postId"]').val();
        var userId = $('input[name="userId"]').val();

        $.ajax({
            type: 'POST',
            url: '/posts/comments/pre/' + postId,
            data: {
                postCommentContent: replyContent,
                postId: postId,
                userId: userId,
                parentCommentPreCommentId: commentId
            },
            success: function(response) {
                location.reload();
            },
            error: function(error) {
                console.log('Error:', error);
            }
        });
    });

    // Edit button click handler for both comments and replies
    document.querySelectorAll('.edit-comment-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = button.getAttribute('data-comment-id');
            const editFormComment = document.getElementById(`editCommentForm-${commentId}`);
            const editFormReply = document.getElementById(`editReplyForm-${commentId}`);

            if (editFormComment) {
                editFormComment.style.display = 'block';
            } else if (editFormReply) {
                editFormReply.style.display = 'block';
            } else {
                console.error(`Edit form not found for comment ID: ${commentId}`);
            }
        });
    });

    // Cancel button click handler for both comments and replies
    document.querySelectorAll('.cancel-edit-btn, .cancel-reply-edit-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = button.getAttribute('data-comment-id');
            const editFormComment = document.getElementById(`editCommentForm-${commentId}`);
            const editFormReply = document.getElementById(`editReplyForm-${commentId}`);

            if (editFormComment) {
                editFormComment.style.display = 'none';
            } else if (editFormReply) {
                editFormReply.style.display = 'none';
            } else {
                console.error(`Edit form not found for comment ID: ${commentId}`);
            }
        });
    });

    // Save button click handler for both comments and replies
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

            const editFormComment = document.getElementById(`editCommentForm-${commentId}`);
            const editFormReply = document.getElementById(`editReplyForm-${commentId}`);

            if (editFormComment) {
                editFormComment.style.display = 'none';
            } else if (editFormReply) {
                editFormReply.style.display = 'none';
            } else {
                console.error(`Edit form not found for comment ID: ${commentId}`);
            }
        });
    });

    // Delete button click handler for both comments and replies
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

    // 신고 버튼 이벤트
    document.querySelectorAll('.report-comment-btn').forEach(function (button) {
        button.addEventListener('click', function () {
            const commentId = button.getAttribute('data-comment-id');
            $.ajax({
                url: '/api/report',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ type: 'COMMENT', id: commentId }),
                success: function (result) {
                    alert('신고가 접수되었습니다.');
                    window.location.reload();
                },
                error: function (err) {
                    console.log(err);
                    if (err.status === 401) {
                        alert('로그인이 필요합니다.');
                        window.location.href = '/user/login';
                    } else if (err.status === 500) {
                        alert('서버 오류가 발생했습니다.');
                    } else {
                        alert('신고 접수에 실패했습니다. 잠시후 다시 시도해주세요.');
                    }
                }
            });
        });
    });
});
