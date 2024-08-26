document.addEventListener("DOMContentLoaded", function () {
    // 검색 버튼 클릭 시 이벤트 핸들러
    document.getElementById("search-btn").addEventListener("click", function () {
        const filter = document.getElementById("filter").value;
        const sort = document.getElementById("sort").value;
        const reported = document.getElementById("search-id").value;

        // 필터 및 정렬 값을 변환
        const apiFilter = convertFilterValue(filter);
        const apiSort = convertSortValue(sort);

        // REST API 호출
        fetchReports(apiFilter, reported, apiSort);
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
        <button class="btn-delete">게시글 삭제</button>
        <button class="btn-complete">처리 완료</button>
      </div>
    `;

        // 신고 박스를 컨테이너에 추가
        reportContainer.appendChild(reportBox);
    });
}
