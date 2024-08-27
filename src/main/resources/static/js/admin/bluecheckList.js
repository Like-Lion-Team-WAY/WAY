// DOM이 로드된 후 실행되도록 보장하기 위해 jQuery의 ready 함수 사용
$(document).ready(function () {
    // 모든 .bluecheckBtn 클래스의 버튼에 클릭 이벤트 리스너 추가
    $('.bluecheckBtn').on('click', function (event) {
        // 기본 폼 제출 동작을 막음
        event.preventDefault();

        // 클릭된 버튼에서 username 값을 가져옴
        var username = $(this).val();

        // addRoleBluecheck 함수 호출
        addRoleBluecheck(username);
    });
});

function addRoleBluecheck(username) {
    fetch(`/api/role/bluecheck?username=${username}`, {
        method: "POST"
    })
        .then((response) => {
            if (response.ok) {
                alert("파란 배지가 정상적으로 지급되었습니다.");
                removeRecord(username);
            } else {
                alert("배지 지급에 실패했습니다.");
            }
        })
        .catch((error) => console.error("Error updating user role:", error));
}

function removeRecord(username) {
    fetch(`/api/bluecheck?username=${username}`, {
        method: "DELETE"
    })
        .then((response) => {
            if (!response.ok)
                alert("bluecheck application에서 삭제 실패");

        })
        .catch((error) => console.error(error));
}