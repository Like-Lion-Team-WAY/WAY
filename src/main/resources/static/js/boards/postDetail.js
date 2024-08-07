document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const likeAction = document.getElementById('likeAction');
    const scrapAction = document.getElementById('scrapAction');

    const pathSegments = window.location.pathname.split('/');
    const boardName = pathSegments[pathSegments.length - 2]; //boardId로 수정 예정
    const postTitle = pathSegments[pathSegments.length - 1]; //postId로 수정 예정

    // const postId = pathSegments[pathSegments.length - 1];
    const postId = 4; //임시 테스트용으로 postId 직접 부여

    returnPage.addEventListener('click', () => {
        window.location.href = '/boards';
    });

    // 게시글 데이터를 가져오는 함수
    function fetchPostDetails() {
        // 실제 API 요청으로 대체
        fetch(`/api/v1/boards/posts/${boardName}/${postTitle}`)
            .then(response => response.json())
            .then(apiResponse => {
                if (!apiResponse.success) {
                    throw new Error(apiResponse.message || 'Error fetching post details');
                }

                const data = apiResponse.data;
                document.querySelector('.post-date').textContent = new Date(data.created_at).toLocaleDateString();
                document.querySelector('.post-title').textContent = data.postTitle;
                document.querySelector('.post-content').textContent = data.content;
                document.querySelector('.likes').textContent = `👍 ${data.likes}`;
                document.querySelector('.comments').textContent = `💬 ${data.comments}`;
                document.querySelector('.stars').textContent = `⭐ ${data.stars}`;
            })
            .catch(error => console.error('Error fetching post details:', error));
    }

    likeAction.addEventListener('click', () => {
        fetch(`/api/v1/boards/posts/likes/${postTitle}`, { // 경로 수정
            method: 'POST', // 좋아요 요청을 POST로 전송
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(apiResponse => {
                if (!apiResponse.success) {
                    throw new Error(apiResponse.message || 'Error liking post');
                }
                const data = apiResponse.data;
                document.querySelector('.likes').textContent = `👍 ${data.likes}`; // 새로운 좋아요 수 업데이트
            })
            .catch(error => console.error('Error liking post:', error));
    });

    scrapAction.addEventListener('click', () => {
        fetch(`/api/v1/boards/posts/scraps/${postId}`, { // 경로 수정
            method: 'POST', // 좋아요 요청을 POST로 전송
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(apiResponse => {
                if (!apiResponse.success) {
                    throw new Error(apiResponse.message || 'Error liking post');
                }
                const data = apiResponse.data;
                document.querySelector('.scraps').textContent = `⭐ ${data.scraps}`;
            })
            .catch(error => console.error('Error Scrap post:', error));
    })

    // 댓글을 입력하는 함수
    function submitComment() {
        const commentBox = document.querySelector('.comment-box');
        const commentText = commentBox.value;

        if (commentText.trim() === '') {
            alert('댓글을 입력하세요.');
            return;
        }

        // 실제 API 요청으로 대체
        fetch(`/api/v1/boards/posts/${boardName}/${postTitle}/comments`, { // 경로 수정
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ content: commentText })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(apiResponse => {
                if (apiResponse.success) {
                    alert('댓글이 성공적으로 등록되었습니다.');
                    commentBox.value = '';
                    // 댓글 목록을 다시 불러오거나 업데이트하는 로직 추가 필요
                } else {
                    alert('댓글 등록에 실패했습니다.');
                }
            })
            .catch(error => console.error('Error submitting comment:', error));
    }

    // 이벤트 리스너 설정
    document.querySelector('.send-button').addEventListener('click', submitComment);

    // 게시글 상세 정보를 불러옵니다.
    fetchPostDetails();
});