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
                body: JSON.stringify({keyword: keyword})
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
                let displayDescription = board.introduction;
                if (board.name.length > maxLength) {
                    displayName = board.name.substring(0, maxLength) + '...';
                }

                // 북마크 상태에 따라 아이콘 설정
                const bookmarkClass = board.bookmark ? 'fas fa-bookmark' : 'far fa-bookmark';

                boardItem.innerHTML = `
                <i class="bookmark-icon ${bookmarkClass}" data-board-id="${board.boardId}"></i>
                <span>${displayName}</span>
                <div class="underline">
                    <p>${displayDescription}</p>
                </div>
                <span class="arrow-icon">➔</span>`;

                boardList.appendChild(boardItem);

                // 게시판 클릭 이벤트 리스너 추가
                boardItem.querySelector('span').addEventListener('click', () => {
                    // 클릭 시 /boards/posts로 이동하며 boardId를 포함
                    window.location.href = `/boards/${board.boardId}`;
                });

                // 북마크 아이콘 클릭 이벤트 리스너 추가
                const bookmarkIcon = boardItem.querySelector('.bookmark-icon');
                bookmarkIcon.addEventListener('click', (event) => {
                    event.stopPropagation(); // 이벤트 전파를 막아서 클릭 시 페이지 이동 방지
                    toggleBookmark(bookmarkIcon, board.boardId);
                });
            });
        }
    }


    // 초기 목록을 로드
    fetchBoardList();

    // 검색 입력 필드에 이벤트 리스너 추가
    searchInput.addEventListener('input', function () {
        const keyword = searchInput.value.trim();
        fetchBoardList(keyword); // 검색어를 이용해 게시판 목록을 가져옴
    });

    function toggleBookmark(icon, boardId) {
        fetch(`/api/v1/boards/bookmark/${boardId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(apiResponse => {
                if (apiResponse.success) {
                    // 현재 아이콘 클래스를 토글 (fas -> far, far -> fas)
                    icon.classList.toggle('fas');
                    icon.classList.toggle('far');
                } else {
                    alert('북마크 변경에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error toggling bookmark:', error);
                alert('북마크 변경 중 오류가 발생했습니다.');
            });
    }


});
