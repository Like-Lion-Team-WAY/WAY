document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const boardForm = document.getElementById('boardForm');

    returnPage.addEventListener('click', () => history.back());

    boardForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const name = document.getElementById('name').value;
        const introduction = document.getElementById('introduction').value;
        const toggle = document.getElementById('toggle').checked;

        const data = { name, introduction, anonymousPermission: toggle };

        fetch('/api/v1/boards/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(message => {
                alert(message);
                window.history.back();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('게시판 생성에 실패했습니다.');
            });
    });
});
