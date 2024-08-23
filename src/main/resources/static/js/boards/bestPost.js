document.addEventListener('DOMContentLoaded', () => {

    // Fetch board list from API
    fetch('/api/v1/boards/best')
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success) {
                throw new Error(apiResponse.message || 'Error fetching board list');
            }
            const data = apiResponse.data;
            const boardList = document.getElementById('boardList');
            boardList.innerHTML = ''; // Clear the placeholder

            if (data.length === 0) {
                const placeholder = document.createElement('div');
                placeholder.className = 'board-item-placeholder';
                placeholder.textContent = '게시판 목록이 없습니다.';
                boardList.appendChild(placeholder);
            } else {
                data.forEach(bestPost => {
                    const maxLength = 20;
                    let displayName = bestPost.boardTitle;
                    if (bestPost.boardTitle.length > maxLength) {
                        displayName = bestPost.boardTitle.substring(0, maxLength) + '...';
                    }
                    const boardItem = document.createElement('div');
                    boardItem.className = 'board-item';
                    boardItem.innerHTML = `<span>${displayName} 👍 ${bestPost.likes} 🔥🔥🔥</span><span>➔</span>`;
                    boardList.appendChild(boardItem);

                    // 클릭 이벤트 리스너 추가
                    boardItem.addEventListener('click', () => {
                        // 클릭 시 /boards/posts로 이동하며 boardId를 쿼리 파라미터로 포함
                        window.location.href = `/boards/posts/${bestPost.boardId}/${bestPost.postId}`;
                    });
                });
            }
        })
        .catch(error => {
            console.error('Error fetching board list:', error);
            alert('게시판 목록을 가져오는 데 실패했습니다.');
        });
});
