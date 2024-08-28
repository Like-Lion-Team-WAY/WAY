document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const boardForm = document.getElementById('boardForm');

    returnPage.addEventListener('click', () => {
        window.location.href = '/boards';
    });

    boardForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const name = document.getElementById('name').value;
        const introduction = document.getElementById('introduction').value;

        const data = { name, introduction};


        fetch('/api/v1/boards/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data),
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    window.history.back();
                } else {
                    alert('게시판 생성에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('게시판 생성에 실패했습니다.');
            });
    });
});
