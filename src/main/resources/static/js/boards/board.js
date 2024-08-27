document.addEventListener('DOMContentLoaded', () => {
    const createBoardButton = document.getElementById('createBoardButton');
    const searchInput = document.getElementById('search-board');
    const boardList = document.getElementById('boardList');
    const pagination = document.getElementById('pagination');

    let currentPage = 1;
    const boardsPerPage = 8;

    createBoardButton.addEventListener('click', () => {
        window.location.href = '/boards/create'; // GET 요청을 통해 페이지 이동
    });

    function fetchBoardList(page = 1, keyword = '') {
        let url = `/api/v1/boards?page=${page}&size=${boardsPerPage}`;

        if (keyword) {
            url = `/api/v1/boards/search?page=${page}&size=${boardsPerPage}`;
        }

        const options = keyword
            ? {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ keyword: keyword })
            }
            : {};

        fetch(url, options)
            .then(response => response.json())
            .then(apiResponse => {
                if (!apiResponse.success) {
                    throw new Error(apiResponse.message || 'Error fetching board list');
                }

                const data = apiResponse.data;
                const boards = data.content;
                const totalPages = data.totalPages;

                boardList.innerHTML = ''; // Clear the board list
                pagination.innerHTML = ''; // Clear the pagination

                if (boards.length === 0) {
                    const placeholder = document.createElement('div');
                    placeholder.className = 'board-item-placeholder';
                    placeholder.textContent = '게시판 목록이 없습니다.';
                    boardList.appendChild(placeholder);
                } else {
                    boards.forEach(board => {
                        const boardItem = document.createElement('div');
                        boardItem.className = 'board-item';

                        const maxLength = 20;
                        let displayName = board.name;
                        let displayDescription = board.introduction;
                        if (board.name.length > maxLength) {
                            displayName = board.name.substring(0, maxLength) + '...';
                        }

                        boardItem.innerHTML = `
                        <span>${displayName}</span>
                        <div class="underline">
                            <p>${displayDescription}</p>
                        </div>
                        <span class="arrow-icon">➔</span>`;

                        boardList.appendChild(boardItem);

                        // 게시판 클릭 이벤트 리스너 추가
                        boardItem.addEventListener('click', () => {
                            window.location.href = `/boards/${board.boardId}`;
                        });
                    });
                }

                // 페이지네이션 생성
                for (let i = 1; i <= totalPages; i++) {
                    const pageItem = document.createElement('li');
                    pageItem.className = `page-item ${i === page ? 'active' : ''}`;
                    pageItem.innerHTML = `<a class="page-link" href="#">${i}</a>`;
                    pageItem.addEventListener('click', (e) => {
                        e.preventDefault();
                        currentPage = i;
                        fetchBoardList(currentPage, searchInput.value.trim());
                    });
                    pagination.appendChild(pageItem);
                }
            })
            .catch(error => console.error('Error fetching board list:', error));
    }

    fetchBoardList();

    searchInput.addEventListener('input', function () {
        const keyword = searchInput.value.trim();
        currentPage = 1;
        fetchBoardList(currentPage, keyword);
    });
});
