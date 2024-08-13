const pathSegments = window.location.pathname.split('/');
const postId = pathSegments[pathSegments.length - 1];

document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const likeAction = document.getElementById('likeAction');
    const scrapAction = document.getElementById('scrapAction');

    returnPage.addEventListener('click', () => {
        window.location.href = '/boards';
    });

    likeAction.addEventListener('click', likePost);
    scrapAction.addEventListener('click', scrapPost);

    fetchPostDetails();
});

function fetchPostDetails() {
    fetch(`/api/v1/boards/posts/details/${postId}`)
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success) {
                throw new Error(apiResponse.message || 'Error fetching post details');
            }

            const data = apiResponse.data;

            document.querySelector('.post-date').textContent = new Date(data.postCreatedAt).toLocaleDateString();
            document.querySelector('.post-title').textContent = data.postTitle;
            document.querySelector('.post-content').textContent = data.postContent;
            document.querySelector('.likes').textContent = `👍 ${data.postLikes}`;
            document.querySelector('.comments').textContent = `💬 ${data.postComments}`;
            document.querySelector('.scraps').textContent = `⭐ ${data.postScraps}`;
            // 댓글 목록을 업데이트합니다.
            updateComments(data.boardPostCommentsList);
        })
        .catch(error => console.error('Error fetching post details:', error));
}

function likePost() {
    fetch(`/api/v1/boards/posts/likes/${postId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success) {
                throw new Error(apiResponse.message || 'Error liking post');
            }
            fetchPostDetails();
        })
        .catch(error => console.error('Error liking post:', error));
}

function scrapPost() {
    fetch(`/api/v1/boards/posts/scraps/${postId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success) {
                throw new Error(apiResponse.message || 'Error scraping post');
            }
            fetchPostDetails();
        })
        .catch(error => console.error('Error scraping post:', error));
}

function submitComment(preCommentId) {
    const commentBox = preCommentId === 0
        ? document.querySelector('.comment-box')
        : document.querySelector(`#reply-input-${preCommentId} .reply-box`);

    const anonymousCheckbox = preCommentId === 0
        ? document.getElementById('anonymousPermission')
        : document.getElementById(`anonymousPermission-${preCommentId}`);

    const commentText = commentBox.value;
    const anonymousPermission = anonymousCheckbox.checked;

    if (commentText.trim() === '') {
        alert('댓글을 입력하세요.');
        return;
    }

    const commentData = {
        content: commentText,
        preCommentId: preCommentId,
        anonymousPermission: anonymousPermission
    };

    fetch(`/api/v1/boards/posts/comments/${postId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(commentData),
    })
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success) {
                throw new Error(apiResponse.message || 'Error posting comment');
            }
            commentBox.value = ''; // 댓글 제출 후 입력창 내용 비우기
            fetchPostDetails(); // 댓글이 성공적으로 저장된 후 게시글 세부 정보 업데이트
        })
        .catch(error => console.error('Error posting comment:', error));
}

function updateComments(comments) {
    const commentsList = document.querySelector('.comments-list');
    commentsList.innerHTML = ''; // 기존 댓글 목록 초기화

    const commentMap = new Map();

    // 먼저 댓글을 그룹화하여 저장
    comments.forEach(comment => {
        if (comment.preCommentId === 0) {
            // 원댓글인 경우
            commentMap.set(comment.commentId, {comment: comment, replies: []});
        } else {
            // 대댓글인 경우
            if (commentMap.has(comment.preCommentId)) {
                commentMap.get(comment.preCommentId).replies.push(comment);
            }
        }
    });

    // 댓글과 대댓글을 렌더링
    commentMap.forEach((value, key) => {
        const commentHtml = createCommentHtml(value.comment);
        commentsList.insertAdjacentHTML('beforeend', commentHtml);

        // 대댓글을 원댓글 아래에 삽입
        value.replies.forEach(reply => {
            const replyHtml = createReplyHtml(reply);
            commentsList.insertAdjacentHTML('beforeend', replyHtml);
        });
    });
}

function createCommentHtml(comment) {
    return `
        <div class="comment" id="comment-${comment.commentId}">
            <div class="comment-content">${comment.commentContent}</div>
            <div class="comment-meta">
                <span class="comment-username">${comment.commentUsername}</span>
                <span class="comment-date">${new Date(comment.commentCreatedAt).toLocaleDateString()}</span>
            </div>
            <button class="reply-button" onclick="showReplyInput(${comment.commentId})">대댓글</button>
            <div class="reply-input" id="reply-input-${comment.commentId}" hidden>
                <input type="text" placeholder="대댓글을 입력하세요..." class="reply-box">
                    <input type="checkbox" id="anonymousPermission-${comment.commentId}" class="anonymous-checkbox">
                    익명으로 작성
                <button class="send-button" onclick="submitComment(${comment.commentId})">➤</button>
            </div>
        </div>
    `;
}

function createReplyHtml(reply) {
    return `
        <div class="reply comment" id="comment-${reply.commentId}">
            <div class="comment-content">${reply.commentContent}</div>
            <div class="comment-meta">
                <span class="comment-username">${reply.commentUsername}</span>
                <span class="comment-date">${new Date(reply.commentCreatedAt).toLocaleDateString()}</span>
            </div>
        </div>
    `;
}

function showReplyInput(commentId) {
    const replyInput = document.getElementById(`reply-input-${commentId}`);
    replyInput.hidden = false;
}
