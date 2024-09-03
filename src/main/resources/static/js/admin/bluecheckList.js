$(document).ready(function () {
    // 모든 .bluecheckBtn 클래스의 버튼에 클릭 이벤트 리스너 추가
    $('.bluecheckBtn').on('click', function (event) {
        // 기본 폼 제출 동작을 막음
        event.preventDefault();

        // 클릭된 버튼에서 username 값을 가져옴
        var username = $(this).val();

        // 클릭된 버튼 요소를 변수에 저장
        var buttonElement = $(this);

        // addRoleBluecheck 함수 호출
        addRoleBluecheck(username, buttonElement); // 버튼 요소도 전달
    });
});

function addRoleBluecheck(username, buttonElement) {
    fetch(`/api/role/bluecheck?username=${username}`, {
        method: "POST"
    })
        .then((response) => {
            if (response.ok) {
                alert("파란 배지가 정상적으로 지급되었습니다.");
                removeRecord(username, buttonElement); // 클릭된 버튼 요소도 전달
            } else {
                alert("배지 지급에 실패했습니다.");
            }
        })
        .catch((error) => console.error("Error updating user role:", error));
}

function removeRecord(username, buttonElement) {
    fetch(`/api/bluecheck?username=${username}`, {
        method: "DELETE"
    })
        .then((response) => {
            if (response.ok) {
                buttonElement.closest('.form-group').remove();
            } else {
                alert("bluecheck application에서 삭제 실패");
            }
        })
        .catch((error) => alert(error));
}
