document.addEventListener('DOMContentLoaded', () => {
    const createBoardButton = document.getElementById('createBoardButton');
    const searchInput = document.getElementById('search-board');
    const boardList = document.getElementById('boardList');

    // 게시판 생성 버튼 클릭 이벤트
    createBoardButton.addEventListener('click', () => {
        window.location.href = '/boards/create'; // GET 요청을 통해 페이지 이동
    });

    // 게시판 목록을 가져와서 표시하는 함수
    function fetchBoardList(keyword = '') {
        let url = '/api/v1/boards';

        // 검색어가 있으면 검색 API를 호출
        if (keyword) {
            url = '/api/v1/boards/search';
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ keyword: keyword })
            })
                .then(response => response.json())
                .then(apiResponse => {
                    displayBoardList(apiResponse);
                })
                .catch(error => {
                    console.error('Error fetching search results:', error);
                    alert('게시판 검색에 실패했습니다.');
                });
        } else {
            // 검색어가 없으면 전체 목록을 가져옴
            fetch(url)
                .then(response => response.json())
                .then(apiResponse => {
                    displayBoardList(apiResponse);
                })
                .catch(error => {
                    console.error('Error fetching board list:', error);
                    alert('게시판 목록을 가져오는 데 실패했습니다.');
                });
        }
    }

    // 게시판 목록을 화면에 표시하는 함수
    function displayBoardList(apiResponse) {
        if (!apiResponse.success) {
            throw new Error(apiResponse.message || 'Error fetching board list');
        }

        const data = apiResponse.data;
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

                // 최대 글자수 제한
                const maxLength = 20;
                let displayName = board.name;
                if (board.name.length > maxLength) {
                    displayName = board.name.substring(0, maxLength) + '...';
                }

                boardItem.innerHTML = `<span>${displayName}</span><span>➔</span>`;
                boardList.appendChild(boardItem);

                // 클릭 이벤트 리스너 추가
                boardItem.addEventListener('click', () => {
                    // 클릭 시 /boards/posts로 이동하며 boardId를 포함
                    window.location.href = `/boards/${board.boardId}`;
                });
            });
        }
    }

    // 초기 목록을 로드
    fetchBoardList();

    // 검색 입력 필드에 이벤트 리스너 추가
    searchInput.addEventListener('input', function() {
        const keyword = searchInput.value.trim();
        fetchBoardList(keyword); // 검색어를 이용해 게시판 목록을 가져옴
    });
});
