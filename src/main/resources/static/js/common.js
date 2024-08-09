$(document).ready(function() {
    // 페이지 로드 시 로컬 스토리지에서 상태 복원
    const activeTab = localStorage.getItem('activeTab') || 'questionTab';
    if (activeTab === 'communityTab') {
        $('#questionTab').hide();
        $('#communityTab').show();
    } else {
        $('#questionTab').show();
        $('#communityTab').hide();
    }

    // 질문 탭 클릭 시
    $('#question').click(function() {
        localStorage.setItem('activeTab', 'questionTab');
    });

    // 커뮤니티 탭 클릭 시
    $('#community').click(function() {
        localStorage.setItem('activeTab', 'communityTab');
    });
});