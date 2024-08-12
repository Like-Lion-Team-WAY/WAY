document.addEventListener("DOMContentLoaded", function() {
    checkFollowStatus();
});

function checkFollowStatus() {
    const followButton = document.getElementById("followButton");
    const username = followButton.dataset.username;

    fetch(`/follow/followCheck?username=${username}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.text())
        .then(data => {
            if (data === "following") {
                followButton.textContent = "언팔로우";
                followButton.dataset.action = "unfollow";
            } else {
                followButton.textContent = "팔로우";
                followButton.dataset.action = "follow";
            }
        })
        .catch(error => console.error('Error:', error));
}

function toggleFollow() {
    const followButton = document.getElementById("followButton");
    const action = followButton.dataset.action;
    const username = followButton.dataset.username;
    const url = action === "follow" ? "/follow/following" : "/follow/unFollowing";

    fetch(`${url}?username=${username}`, {
        method: action === "follow" ? 'POST' : 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.text())
        .then(data => {
            if (data === "success") {
                checkFollowStatus(); // 상태 업데이트
                window.location.reload();
            } else {
                console.error('Follow/Unfollow action failed');
            }
        })
        .catch(error => console.error('Error:', error));
}
