$(document).ready(function() {
    // 페이지 로드 시 로컬 스토리지에서 상태 복원
    const activeTab = localStorage.getItem('activeTab') || 'questionTab';

    function showTab(tabId) {
        $('#questionTab').hide();
        $('#communityTab').hide();
        $('#no-loginTab').hide();
        $('#adminTab').hide();
        $('#' + tabId).show();
    }

    // 현재 URL 확인
    const currentPath = window.location.pathname;

    // 특정 URL일 경우 adminTab을 보여줌
    if (currentPath.startsWith('/admin')) {
        showTab('adminTab');
    } else if (activeTab === 'communityTab') {
        showTab('communityTab');
    } else {
        showTab('questionTab');
    }

    // 질문 탭 클릭 시
    $('#question').click(function() {
        localStorage.setItem('activeTab', 'questionTab');
    });

    // 커뮤니티 탭 클릭 시
    $('#community').click(function() {
        localStorage.setItem('activeTab', 'communityTab');
    });


    function getCookie(name) {
        const cookies = document.cookie.split(';');
        for (let i = 0; i < cookies.length; i++) {
            const cookie = cookies[i].trim();
            if (cookie.indexOf(name + '=') === 0) {
                return cookie.substring(name.length + 1, cookie.length);
            }
        }
        return null;
    }

    const accessToken = getCookie('accessToken');
    if (!accessToken) {
        showTab('no-loginTab');
        $('#question').hide();
        $('#community').hide();
        $('#alarm').hide();
        $('#chatIcon').hide();
    }

    $('#logout').click(function(){
        localStorage.removeItem('activeTab');
        window.location.href="/user/logout";
    });
});
