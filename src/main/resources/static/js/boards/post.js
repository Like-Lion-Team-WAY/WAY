document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const editBoardButton = document.getElementById('editBoardButton');
    const createBoardButton = document.getElementById('createBoardButton');
    const boardTitle = document.getElementById('boardTitle');
    const boardPostList = document.getElementById('boardPostList');
    const pagination = document.getElementById('pagination');

    const pathSegments = window.location.pathname.split('/');
    const boardId = pathSegments[pathSegments.length - 1];

    returnPage.addEventListener('click', () => {
        window.location.href = '/boards';
    });

    editBoardButton.addEventListener('click', () => {
        window.location.href = `/boards/setting/${boardId}`;
    });

    createBoardButton.addEventListener('click', () => {
        window.location.href = `/boards/posts/create/${boardId}`;
    });

    let currentPage = 1;
    const postsPerPage = 8;

    // 페이지가 로드될 때 게시판 제목을 가져옵니다.
    fetch(`/api/v1/boards/${boardId}`)
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success) {
                throw new Error(apiResponse.message || 'Error fetching board title');
            }

            // 게시판 제목 설정
            boardTitle.textContent = `${apiResponse.data.name} 게시판`;

            // userOwnerMatch가 true일 때만 '게시판 수정'과 '게시글 생성' 버튼을 표시
            if (apiResponse.data.userOwnerMatch) {
                document.getElementById('editBoardButton').style.display = 'block';
            } else {
                document.getElementById('editBoardButton').style.display = 'none';
            }
        })
        .catch(error => console.error('Error fetching board title:', error));


    function fetchBoardPosts(page) {
        fetch(`/api/v1/boards/posts/${boardId}?page=${page}&size=${postsPerPage}`)
            .then(response => response.json())
            .then(apiResponse => {
                if (!apiResponse.success) {
                    throw new Error(apiResponse.message || 'Error fetching board posts');
                }

                const data = apiResponse.data;
                const posts = data.content;  // assuming data.content contains the list of posts
                const totalPages = data.totalPages;  // assuming data.totalPages contains the total number of pages

                boardPostList.innerHTML = '';
                pagination.innerHTML = '';

                if (posts.length === 0) {
                    const placeholder = document.createElement('tr');
                    const td = document.createElement('td');
                    td.colSpan = 4;
                    td.textContent = '게시글이 없습니다.';
                    td.className = 'text-center';
                    placeholder.appendChild(td);
                    boardPostList.appendChild(placeholder);
                } else {
                    posts.forEach((post, index) => {

                        const maxLength = 10;
                        let displayName = post.postTitle;
                        if (post.postTitle.length > maxLength) {
                            displayName = post.postTitle.substring(0, maxLength) + '...';
                        }

                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${index + 1 + (currentPage - 1) * postsPerPage}</td>
                            <td>${displayName}</td>
                            <td>${post.author}</td>
                            <td>${new Date(post.created_at).toLocaleDateString()}</td>
                        `;
                        boardPostList.appendChild(row);

                        // 게시글 클릭 시 상세보기 요청
                        row.addEventListener('click', () => {
                            window.location.href = `/boards/posts/${boardId}/${post.boardPostId}`;
                        });
                    });
                }

                for (let i = 1; i <= totalPages; i++) {
                    const pageItem = document.createElement('li');
                    pageItem.className = `page-item ${i === page ? 'active' : ''}`;
                    pageItem.innerHTML = `<a class="page-link" href="#">${i}</a>`;
                    pageItem.addEventListener('click', (e) => {
                        e.preventDefault();
                        currentPage = i;
                        fetchBoardPosts(currentPage);
                    });
                    pagination.appendChild(pageItem);
                }
            })
            .catch(error => console.error('Error fetching board details:', error));
    }

    fetchBoardPosts(currentPage);
});
