document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const updateBoardButton = document.getElementById('updateBoardButton');
    const deleteBoardButton = document.getElementById('deleteBoardButton');

    const pathSegments = window.location.pathname.split('/');
    const boardId = pathSegments[pathSegments.length - 1];

    returnPage.addEventListener('click', () => {
        window.history.back();
    });

    updateBoardButton.addEventListener('click', () => {
        const newBoardName = document.getElementById('boardName').value;
        const boardDescription = document.getElementById('boardDescription').value;
        const allowAnonymous = document.getElementById('allowAnonymous').checked;

        const updateData = {
            name: newBoardName,
            introduction: boardDescription,
            anonymousPermission: allowAnonymous
        };

        fetch(`/api/v1/boards/update/${boardId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updateData)
        })
            .then(response => response.json())
            .then(apiResponse => {
                if (apiResponse.success) {
                    alert(apiResponse.message);
                    window.location.href = `/boards/${boardId}`;
                } else {
                    throw new Error(apiResponse.message || 'Error fetching board posts');
                }

            })
            .catch(error => console.error('Error:', error));
    });

    deleteBoardButton.addEventListener('click', () => {
        if (confirm('정말로 이 게시판을 삭제하시겠습니까?')) {
            fetch(`/api/v1/boards/delete/${boardId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => response.json())
                .then(apiResponse => {
                    if (apiResponse.success) {
                        alert(apiResponse.message);
                        window.location.href = '/boards';
                    } else {
                        alert(apiResponse.message || '게시판 삭제에 실패했습니다.');
                    }
                })
                .catch(error => console.error('Error:', error));
        }
    });
});
