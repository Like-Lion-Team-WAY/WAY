document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const editBoardButton = document.getElementById('editBoardButton');
    const createBoardButton = document.getElementById('createBoardButton');
    const boardTitle = document.getElementById('boardTitle');
    const boardPostList = document.getElementById('boardPostList');
    const pagination = document.getElementById('pagination');

    const pathSegments = window.location.pathname.split('/');
    const boardName = pathSegments[pathSegments.length - 1];

    returnPage.addEventListener('click', () => {
        window.history.back();
    });

    editBoardButton.addEventListener('click', () => {
        window.location.href = '/boards/setting';
    });

    createBoardButton.addEventListener('click', () => {
        window.location.href = `/boards/posts/create/${boardName}`;
    });



    let currentPage = 1;
    const postsPerPage = 8;

    function fetchBoardPosts(page) {
        fetch(`/api/v1/boards/posts/${boardName}?page=${page}&size=${postsPerPage}`)
            .then(response => response.json())
            .then(data => {
                boardTitle.textContent = `${boardName} 게시판`;
                boardPostList.innerHTML = '';
                pagination.innerHTML = '';

                if (data.posts.length === 0) {
                    const placeholder = document.createElement('tr');
                    const td = document.createElement('td');
                    td.colSpan = 4;
                    td.textContent = '게시글이 없습니다.';
                    td.className = 'text-center';
                    placeholder.appendChild(td);
                    boardPostList.appendChild(placeholder);
                } else {
                    data.posts.forEach((post, index) => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <th scope="row">${post.postNum}</th>
                            <td>${post.postTitle}</td>
                            <td>${post.author}</td>
                            <td>${new Date(post.created_at).toLocaleDateString()}</td>
                        `;
                        boardPostList.appendChild(row);
                    });
                }

                const totalPages = Math.ceil(data.totalPosts / postsPerPage);

                for (let i = 1; i <= totalPages; i++) {
                    const pageItem = document.createElement('li');
                    pageItem.className = `page-item ${i === page ? 'active' : ''}`;
                    pageItem.innerHTML = `<a class="page-link" href="#">${i}</a>`;
                    pageItem.addEventListener('click', (e) => {
                        e.preventDefault();
                        currentPage = i;
                        fetchBoardPosts(i);
                    });
                    pagination.appendChild(pageItem);
                }
            })
            .catch(error => console.error('Error fetching board details:', error));
    }

    fetchBoardPosts(currentPage);
});
