$(document).ready(function() {
    var postId = $('#postId').val();
    //수정
    $("#editPostBtn").click(function() {
        $("#postTitle, #postContent").hide();
        $("#editPostForm").show();
        $("#editPostTitle").val($("#postTitle").text());
        $("#editPostContent").val($("#postContent").text());
    });
    //취소
    $("#cancelPostEditBtn").click(function() {
        $("#editPostForm").hide();
        $("#postTitle, #postContent").show();
    });
    //신고
    $("#reportPostBtn").click(function (){
        $.ajax({
            url: "/api/report",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ type: "POST", id: postId }),
            success: function(result) {
                alert("신고가 접수되었습니다.");
                window.location.reload();
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
    })
    //수정 내용 저장
    $("#savePostBtn").click(function() {
        var newTitle = $("#editPostTitle").val();
        var newContent = $("#editPostContent").val();
        var postId = $('#postId').val();

        console.log(postId,newTitle,newContent);

        $.ajax({
            url: "/posts/" + postId,
            method: "PATCH",
            data: {
                title: newTitle,
                content: newContent
            },
            success: function(result) {
                $("#postTitle").text(newTitle);
                $("#postContent").text(newContent);
                $("#editPostForm").hide();
                $("#postTitle, #postContent").show();
            },
            error: function(err) {
                console.log(err);
                alert("Failed to update post. Please try again.");
            }
        });
    });
    //피드(포스트) 삭제
    $("#deletePostBtn").click(function() {
        var postId = $('#postId').val();
        if (confirm("게시글을 삭제하시겠습니까?")) {
            $.ajax({
                url: "/posts",
                method: "DELETE",
                data: { id: postId },
                success: function(result) {
                    alert("Post deleted successfully.");
                    window.location.href = "/posts"; // Redirect to a success page or refresh
                },
                error: function(err) {
                    console.log(err);
                    alert("Failed to delete post. Please try again.");
                }
            });
        }
    });
});

