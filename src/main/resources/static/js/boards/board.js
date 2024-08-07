document.addEventListener('DOMContentLoaded', () => {
    const createBoardButton = document.getElementById('createBoardButton');

    createBoardButton.addEventListener('click', () => {
        window.location.href = '/boards/create'; // GET 요청을 통해 페이지 이동
    });

    // Fetch board list from API
    fetch('/api/v1/boards')
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
                data.forEach(board => {
                    const boardItem = document.createElement('div');
                    boardItem.className = 'board-item';
                    boardItem.innerHTML = `<span>${board.name}</span><span>➔</span>`;
                    boardList.appendChild(boardItem);

                    // 클릭 이벤트 리스너 추가
                    boardItem.addEventListener('click', () => {
                        // 클릭 시 /boards/posts로 이동하며 boardId를 쿼리 파라미터로 포함
                        window.location.href = `/boards/${board.boardId}`;
                    });
                });
            }
        })
        .catch(error => {
            console.error('Error fetching board list:', error);
            alert('게시판 목록을 가져오는 데 실패했습니다.');
        });
});
