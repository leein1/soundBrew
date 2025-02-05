// 사이드바 전체 토글 (기존 코드)
document.querySelector('.menu').addEventListener('click', function() {
    var sidebar = document.querySelector('.sidebar');
    var menuIcon = document.querySelector('.menu-icon');
    sidebar.classList.toggle('expanded');

    if (sidebar.classList.contains('expanded')) {
        menuIcon.src = '/images/close_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
    } else {
        menuIcon.src = '/images/menu_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
        // 사이드바가 닫힐 때, 내부 펼침도 닫음
        var userInfoSection = document.querySelector('.sidebar-my-info');
        if(userInfoSection && userInfoSection.classList.contains('open')) {
            userInfoSection.classList.remove('open');
        }
    }
});

// 모바일 환경용 토글 (기존 코드)
document.querySelector('.navigation-menu').addEventListener('click', function() {
    var sidebar = document.querySelector('.sidebar');
    var menuIcon = document.querySelector('.menu-icon');
    sidebar.classList.toggle('expanded');

    if (sidebar.classList.contains('expanded')) {
        menuIcon.src = '/images/close_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
    } else {
        menuIcon.src = '/images/menu_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
        // 사이드바가 닫힐 때 내부 펼침 닫기
        var userInfoSection = document.querySelector('.sidebar-my-info');
        if(userInfoSection && userInfoSection.classList.contains('open')) {
            userInfoSection.classList.remove('open');
        }
    }
});

// .sidebar-my-info 클릭 시 내부 내용 토글 (사이드바가 열린 상태에서만)
document.querySelector('.sidebar-my-info').addEventListener('click', function(e) {
    // 만약 사이드바가 닫혀 있다면 아무 작업도 하지 않음
    var sidebar = document.querySelector('.sidebar');
    if (!sidebar.classList.contains('expanded')) {
        return;
    }

    // 제목 영역(클릭 시 펼치기) 외의 다른 클릭 이벤트가 있다면(e.target) 예외 처리 가능
    // 예: 내부의 링크 클릭 시 확장 토글이 발생하지 않도록 할 수 있음

    // 토글: open 클래스가 있으면 제거, 없으면 추가
    this.classList.toggle('open');
});
