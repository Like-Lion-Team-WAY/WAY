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

                boardTitle.textContent = `${posts[0].boardName} 게시판`;
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
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <th scope="row">${index + 1 + (currentPage - 1) * postsPerPage}</th>
                            <td>${post.postTitle}</td>
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
