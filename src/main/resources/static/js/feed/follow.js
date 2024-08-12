document.addEventListener('DOMContentLoaded', function() {
    const followButton = document.querySelector('.follow-button');
    const username = followButton.getAttribute('data-username');

    // 초기 팔로우 상태 확인
    fetch(`/follow/user/followcheck?username=${username}`, {
        method: 'GET',
    })
        .then(response => response.json())
        .then(isFollowing => {
            followButton.setAttribute('data-following', isFollowing ? 'true' : 'false');
            const icon = followButton.querySelector('i.bi');
            const span = followButton.querySelector('span');

            if (isFollowing) {
                icon.classList.remove('bi-person-plus-fill');
                icon.classList.add('bi-person-dash-fill');
                span.textContent = '언팔로우';
            } else {
                icon.classList.remove('bi-person-dash-fill');
                icon.classList.add('bi-person-plus-fill');
                span.textContent = '팔로우';
            }
        })
        .catch(error => console.error('Error:', error));

    // 팔로우 버튼 클릭 이벤트
    followButton.addEventListener('click', function() {
        const isFollowing = this.getAttribute('data-following') === 'true';
        const url = isFollowing ? '/follow/unFollowing' : '/follow/following';
        const method = isFollowing ? 'DELETE' : 'POST';

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({ 'username': username })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Toggle follow state
                    this.setAttribute('data-following', isFollowing ? 'false' : 'true');
                    const icon = this.querySelector('i.bi');
                    const span = this.querySelector('span');

                    if (isFollowing) {
                        icon.classList.remove('bi-person-dash-fill');
                        icon.classList.add('bi-person-plus-fill');
                        span.textContent = '팔로우';
                    } else {
                        icon.classList.remove('bi-person-plus-fill');
                        icon.classList.add('bi-person-dash-fill');
                        span.textContent = '언팔로우';
                    }
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    });
});
