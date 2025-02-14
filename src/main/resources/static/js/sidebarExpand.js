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

// 사이드바 요소가 아닌 다른 외부를 클릭했을때 닫히게 하는 코드
document.addEventListener('click', function(e) {
    var sidebar = document.querySelector('.sidebar');
    var menuButton = document.querySelector('.menu');
    var navMenuButton = document.querySelector('.navigation-menu');
    var menuIcon = document.querySelector('.menu-icon');

    // 사이드바가 열려있는 경우에만 동작
    if (sidebar.classList.contains('expanded')) {
        // 클릭한 요소가 사이드바, 메뉴 버튼, 또는 모바일 메뉴 버튼 내부에 없다면
        if (
            !sidebar.contains(e.target) &&
            !menuButton.contains(e.target) &&
            !navMenuButton.contains(e.target)
        ) {
            // 사이드바 닫기
            sidebar.classList.remove('expanded');
            menuIcon.src = '/images/menu_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';

            // 열린 내부 메뉴(예: .sidebar-my-info, .sidebar-admin-info)도 닫기
            var openSections = sidebar.querySelectorAll('.open');
            openSections.forEach(function(section) {
                section.classList.remove('open');
            });
        }
    }
});

// .sidebar-my-info 클릭 시 내부 내용 토글 (사이드바가 열린 상태에서만)
var sidebarMyInfo = document.querySelector('.sidebar-my-info');
if (sidebarMyInfo) {
    sidebarMyInfo.addEventListener('click', function(e) {
        // 만약 사이드바가 닫혀 있다면 아무 작업도 하지 않음
        var sidebar = document.querySelector('.sidebar');
        if (!sidebar.classList.contains('expanded')) {
            return;
        }

        // 토글: open 클래스가 있으면 제거, 없으면 추가
        this.classList.toggle('open');
    });
}

// 어드민용
var sidebarAdminInfo = document.querySelector('.sidebar-admin-info');
if (sidebarAdminInfo) {
    sidebarAdminInfo.addEventListener('click', function(e) {
        var sidebar = document.querySelector('.sidebar');
        if (!sidebar.classList.contains('expanded')) {
            return;
        }
        // 토글: open 클래스가 있으면 제거, 없으면 추가
        this.classList.toggle('open');
    });
}
