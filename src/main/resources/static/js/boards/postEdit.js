document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const submitButton = document.getElementById('submitButton');
    const viewRulesButton = document.getElementById('viewRulesButton');

    const pathSegments = window.location.pathname.split('/');
    const boardId = pathSegments[pathSegments.length - 2]; // boardId
    const postId = pathSegments[pathSegments.length - 1]; // postId

    // 기존 게시글 데이터 불러오기
    fetch(`/api/v1/boards/posts/details/${postId}`)
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success){
                alert('게시물 정보를 불러오는데 실패했습니다.');
            }
            const data = apiResponse.data;

            document.getElementById('postTitle').value = data.postTitle;
            document.getElementById('postContent').value = data.postContent;

        })
        .catch(error => console.error('Error fetching post data:', error));

    returnPage.addEventListener('click', () => {
        window.history.back();
    });

    submitButton.addEventListener('click', () => {
        const postTitle = document.getElementById('postTitle').value;
        const postContent = document.getElementById('postContent').value;

        const postData = {
            title: postTitle,
            content: postContent,
        };

        fetch(`/api/v1/boards/posts/edit/${postId}`, {
            method: 'PUT',  // PUT 메서드를 사용하여 기존 게시물 수정
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('게시물이 수정되었습니다.');
                    window.location.href = `/boards/${boardId}`;
                } else {
                    alert('게시물 수정에 실패했습니다.');
                }
            })
            .catch(error => console.error('Error:', error));
    });

    viewRulesButton.addEventListener('click', () => {
        window.location.href = '/admin/customerService';
    });
});
