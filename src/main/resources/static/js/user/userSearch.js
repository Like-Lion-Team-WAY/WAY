async function fetchUsers() {
    try {
        const response = await fetch('/user/all');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const users = await response.json();
        const userList = document.getElementById('user-list');
        userList.innerHTML = ''; // 기존 내용을 비우기

        users.forEach(user => {
            const a = document.createElement('a');
            a.href = `/posts/${user.username}`; // 사용자 상세 페이지로 링크
            a.className = 'user-item';
            a.innerHTML = `
                        <img src="${user.imageUrl ? `/display?filename=${user.imageUrl}` : '/image/image.jpg'}" alt="${user.username} profile picture">
                        <span class="username"> ${user.username}</span>
                    `;
            userList.appendChild(a);
        });
    } catch (error) {
        console.error('Error fetching users:', error);
    }
}

async function searchUsers() {
    const query = document.getElementById('search-input').value;
    try {
        const response = await fetch(`/user/search?username=${query}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const users = await response.json();
        const userList = document.getElementById('user-list');
        userList.innerHTML = ''; // 기존 내용을 비우기

        users.forEach(user => {
            const a = document.createElement('a');
            a.href = `/posts/${user.username}`; // 사용자 상세 페이지로 링크
            a.className = 'user-item';
            a.innerHTML = `
                        <img src="${user.imageUrl ? `/display?filename=${user.imageUrl}` : '/image/image.jpg'}" alt="${user.username} profile picture">
                        <span class="username"> ${user.username}</span>
                    `;
            userList.appendChild(a);
        });
    } catch (error) {
        console.error('Error searching users:', error);
    }
}

// 페이지 로드 시 모든 사용자 불러오기
window.onload = fetchUsers;