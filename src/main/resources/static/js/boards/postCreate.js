document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const submitButton = document.getElementById('submitButton');
    const viewRulesButton = document.getElementById('viewRulesButton');

    returnPage.addEventListener('click', () => {
        window.history.back();
    });

    submitButton.addEventListener('click', () => {
        const postTitle = document.getElementById('postTitle').value;
        const postContent = document.getElementById('postContent').value;
        const anonymous = document.getElementById('anonymousCheck').checked;

        const postData = {
            title: postTitle,
            content: postContent,
            anonymous: anonymous
        };

        const pathSegments = window.location.pathname.split('/');
        const boardName = pathSegments[pathSegments.length - 1];


        fetch(`/api/v1/boards/posts/${boardName}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('게시물이 등록되었습니다.');
                    window.location.href = '/boards/posts';
                } else {
                    alert('게시물 등록에 실패했습니다.');
                }
            })
            .catch(error => console.error('Error:', error));
    });

    viewRulesButton.addEventListener('click', () => {
        window.location.href = '/community/rules';
    });
});
