document.addEventListener('DOMContentLoaded', () => {
    const returnPage = document.getElementById('returnPage');
    const submitButton = document.getElementById('submitButton');
    const viewRulesButton = document.getElementById('viewRulesButton');
    const uploadImageButton = document.getElementById('uploadImageButton');
    const imageUpload = document.getElementById('imageUpload');
    const postContent = document.getElementById('postContent');

    const pathSegments = window.location.pathname.split('/');
    const boardId = pathSegments[pathSegments.length - 2]; // boardId
    const postId = pathSegments[pathSegments.length - 1]; // postId

    // 기존 게시글 데이터 불러오기
    fetch(`/api/v1/boards/posts/details/${postId}`)
        .then(response => response.json())
        .then(apiResponse => {
            if (!apiResponse.success) {
                alert('게시물 정보를 불러오는데 실패했습니다.');
                return;
            }
            const data = apiResponse.data;

            document.getElementById('postTitle').value = data.postTitle;
            postContent.value = data.postContent;

        })
        .catch(error => console.error('Error fetching post data:', error));

    returnPage.addEventListener('click', () => {
        window.history.back();
    });

    submitButton.addEventListener('click', () => {
        const postTitle = document.getElementById('postTitle').value;
        const postContentValue = postContent.value;

        const postData = {
            title: postTitle,
            content: postContentValue,
        };

        fetch(`/api/v1/boards/posts/edit/${postId}`, {
            method: 'PUT',  // PUT 메서드를 사용하여 기존 게시물 수정
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('게시물이 수정되었습니다.');
                    window.location.href = `/boards/${boardId}`;
                } else {
                    alert('게시물 수정에 실패했습니다.');
                }
            })
            .catch(error => console.error('Error:', error));
    });

    uploadImageButton.addEventListener('click', () => {
        const file = imageUpload.files[0];
        if (!file) {
            alert("이미지를 선택하세요.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        fetch('/img/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json()) // JSON 응답으로 변환
            .then(apiResponse => {
                if (apiResponse.success) { // 성공 여부 확인
                    const url = apiResponse.data; // URL은 data 필드에 포함
                    // 게시글 내용에 이미지 URL을 HTML <img> 태그로 삽입
                    postContent.value += `<img src="${url}" alt="이미지 설명">\n`;
                } else {
                    alert('이미지 업로드에 실패했습니다. 사유: ' + apiResponse.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('이미지 업로드 중 오류가 발생했습니다.');
            });
    });

    viewRulesButton.addEventListener('click', () => {
        window.location.href = '/admin/customerService';
    });
});
