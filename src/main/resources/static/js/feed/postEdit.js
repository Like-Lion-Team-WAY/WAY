$(document).ready(function() {
    var postId = $('#postId').val();

    $("#editPostBtn").click(function() {
        $("#postTitle, #postContent").hide();
        $("#editPostForm").show();
        $("#editPostTitle").val($("#postTitle").text());
        $("#editPostContent").val($("#postContent").text());
    });

    $("#cancelPostEditBtn").click(function() {
        $("#editPostForm").hide();
        $("#postTitle, #postContent").show();
    });

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

