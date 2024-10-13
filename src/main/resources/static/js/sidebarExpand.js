document.querySelector('.menu').addEventListener('click', function() {
    var sidebar = document.querySelector('.sidebar');
    var menuIcon = document.querySelector('.menu-icon');
    
    sidebar.classList.toggle('expanded');

    if (sidebar.classList.contains('expanded')) {
        menuIcon.src = '../images/close_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
    } else {
        menuIcon.src = '../images/menu_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
    }
});
 
 // 모바일 환경에서 navigation-menu 클릭 시 sidebar 확장
 document.querySelector('.navigation-menu').addEventListener('click', function() {
    var sidebar = document.querySelector('.sidebar');
    var menuIcon = document.querySelector('.menu-icon');

    // 사이드바 확장/축소 토글
    sidebar.classList.toggle('expanded');

    // 확장 상태에 따라 메뉴 아이콘 변경
    if (sidebar.classList.contains('expanded')) {
        menuIcon.src = '../images/close_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
    } else {
        menuIcon.src = '../images/menu_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg';
    }
});