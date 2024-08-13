document.addEventListener('DOMContentLoaded', () => {
    const scrapList = document.getElementById('scrapList');
    const pagination = document.getElementById('pagination');

    let currentPage = 1;
    const postsPerPage = 8;

    function fetchBoardPosts(page) {
        fetch(`/api/v1/boards/scraps?page=${page}&size=${postsPerPage}`)
            .then(response => response.json())
            .then(apiResponse => {
                if (!apiResponse.success) {
                    throw new Error(apiResponse.message || 'Error fetching board posts');
                }

                const data = apiResponse.data;
                const scraps = data.content;
                const totalPages = data.totalPages;

                scrapList.innerHTML = '';
                pagination.innerHTML = '';

                if (scraps.length === 0) {
                    const placeholder = document.createElement('tr');
                    const td = document.createElement('td');
                    td.colSpan = 4;
                    td.textContent = '게시글이 없습니다.';
                    td.className = 'text-center';
                    placeholder.appendChild(td);
                    scrapList.appendChild(placeholder);
                } else {
                    scraps.forEach((scrap, index) => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${index + 1 + (currentPage - 1) * postsPerPage}</td>
                            <td>${scrap.title}</td>
                            <td>${scrap.author}</td>
                            <td>${new Date(scrap.createdAt).toLocaleDateString()}</td>
                        `;
                        scrapList.appendChild(row);

                        // 게시글 클릭 시 상세보기 요청
                        row.addEventListener('click', () => {
                            window.location.href = `/boards/posts/${scrap.boardId}/${scrap.boardPostId}`;
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
