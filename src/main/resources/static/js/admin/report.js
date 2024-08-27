document.addEventListener("DOMContentLoaded", function () {
    // 검색 버튼 클릭 시 이벤트 핸들러
    document.getElementById("search-btn").addEventListener("click", function () {
        currentFilter = document.getElementById("filter").value;
        currentSort = document.getElementById("sort").value;
        currentReported = document.getElementById("search-id").value;

        // 필터 및 정렬 값을 변환
        const apiFilter = convertFilterValue(currentFilter);
        const apiSort = convertSortValue(currentSort);

        // REST API 호출
        fetchReports(apiFilter, currentReported, apiSort);
    });
});

// 필터 값을 변환하는 함수
function convertFilterValue(filter) {
    switch (filter) {
        case "전체 목록":
            return "ALL";
        case "질문 신고 목록":
            return "QUESTION";
        case "게시글 신고 목록":
            return "POST";
        case "댓글 신고 목록":
            return "COMMENT";
        case "채팅 신고 목록":
            return "CHAT";
        default:
            return "ALL";
    }
}

// 정렬 값을 변환하는 함수
function convertSortValue(sort) {
    switch (sort) {
        case "신고 일자 오름차순":
            return "ASC";
        case "신고 일자 내림차순":
            return "DESC";
        default:
            return "DESC";
    }
}

function fetchReports(type, reported, sortDirection) {
    // REST API URL
    const url = `/api/report?type=${encodeURIComponent(type)}&reported=${encodeURIComponent(reported)}&sortDirection=${encodeURIComponent(sortDirection)}`;

    // Fetch API 사용하여 GET 요청
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            // 데이터를 받은 후 DOM 업데이트
            createReportBoxes(data.data);
        })
        .catch((error) => {
            console.error("Error fetching reports:", error);
        });
}

function createReportBoxes(reports) {
    const reportContainer = document.querySelector(".report-container");
    reportContainer.innerHTML = ""; // 기존 내용을 지우고 새로 추가

    reports.forEach((report) => {
        // 신고 박스 HTML 생성
        const reportBox = document.createElement("div");
        reportBox.classList.add("report-list");

        let deleteButtonHtml = "";
        let deleteFunction = null;

        // report 타입에 따라 "게시글 삭제" 버튼 추가 및 삭제 API 호출 설정
        if (report.type === "POST") {
            deleteButtonHtml = `<button class="btn-delete">게시글 삭제</button>`;
            deleteFunction = () => deletePost(report.contentId);
        } else if (report.type === "COMMENT") {
            deleteButtonHtml = `<button class="btn-delete">댓글 삭제</button>`;
            deleteFunction = () => deleteComment(report.contentId);
        }

        reportBox.innerHTML = `
          <table class="report-table">
            <tr>
              <th class="header-column">신고 ID</th>
              <td class="wide-column"><span class="highlight-text">${report.id}</span></td>
              <th class="header-column">신고 일자</th>
              <td class="wide-column"><span class="highlight-text">${new Date(report.createdAt).toLocaleString()}</span></td>
              <th class="header-column">신고 타입</th>
              <td class="wide-column"><span class="highlight-text">${report.type}</span></td>
              <th class="header-column">신고자</th>
              <td class="wide-column"><span class="highlight-text">${report.reporter}</span></td>
            </tr>
            <tr>
              <th class="header-column">내용</th>
              <td colspan="7" class="full-width"><span class="highlight-text">${report.content}</span></td>
            </tr>
            <tr>
              <th class="header-column">신고 대상자</th>
              <td class="wide-column"><span class="highlight-text">${report.reported}</span></td>
              <td class="header-column"><button class="btn-account">계정 정지</button></td>
            </tr>
          </table>
          <div class="btn-group">
            ${deleteButtonHtml}
            <button class="btn-complete">처리 완료</button>
          </div>
        `;

        // 삭제 버튼이 존재할 경우 클릭 이벤트 추가
        if (deleteFunction) {
            const deleteButton = reportBox.querySelector(".btn-delete");
            deleteButton.addEventListener("click", deleteFunction);
        }

        // 처리 완료 버튼 클릭 시 이벤트 추가
        const completeButton = reportBox.querySelector(".btn-complete");
        completeButton.addEventListener("click", () => deleteReport(report.id, reportBox));

        // 계정 정지 버튼 클릭 시 이벤트 추가
        const accountButton = reportBox.querySelector(".btn-account");
        accountButton.addEventListener("click", () => addRoleLimited(report.reported));

        // 신고 박스를 컨테이너에 추가
        reportContainer.appendChild(reportBox);
    });
}

// 게시글 삭제 API 호출
function deletePost(postId) {
    fetch(`/posts?id=${postId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                alert("게시글이 성공적으로 삭제되었습니다.");
                // 필요한 경우, 삭제 후 UI 갱신 로직 추가
            } else {
                alert("게시글 삭제에 실패했습니다. 이미 삭제된 게시글일 수 있습니다.");
            }
        })
        .catch(error => console.error("Error deleting post:", error));
}

// 댓글 삭제 API 호출
function deleteComment(commentId) {
    fetch(`/posts/comments/${commentId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                alert("댓글이 성공적으로 삭제되었습니다.");
                // 필요한 경우, 삭제 후 UI 갱신 로직 추가
            } else {
                alert("댓글 삭제에 실패했습니다. 이미 삭제된 댓글일 수 있습니다.");
            }
        })
        .catch(error => console.error("Error deleting comment:", error));
}

// 신고 처리 완료 API 호출 및 해당 신고 박스 삭제
function deleteReport(reportId, reportBox) {
    fetch(`/api/report?id=${reportId}`, {
        method: "DELETE",
    })
        .then(response => {
            if (response.ok) {
                alert("신고가 성공적으로 처리되었습니다.");
                // 신고 박스 DOM에서 제거
                reportBox.remove();
            } else {
                alert("신고 처리에 실패했습니다.");
            }
        })
        .catch(error => console.error("Error deleting report:", error));
}

// 계정 정지
function addRoleLimited(username) {
    fetch(`/api/role/limited?username=${username}`, {
        method: "POST"
    })
        .then((response) => {
            if (response.ok) {
                alert("계정이 성공적으로 정지되었습니다.");
            } else {
                alert("계정 정지에 실패했습니다.");
            }
        })
        .catch((error) => console.error("Error updating user role:", error));
}
