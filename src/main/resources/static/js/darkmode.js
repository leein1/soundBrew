document.addEventListener('DOMContentLoaded', function() {
    const darkModeToggle = document.getElementById('darkModeToggle');
    const sidebar = document.querySelector('.sidebar');
    const musicPlayer = document.querySelector('.music-player');
    const navigation = document.querySelector('.navigation');

    // 다크 모드 상태를 토글하는 함수
    function toggleDarkMode() {
        // body에 'dark-mode' 클래스를 토글하여 다크 모드 적용
        document.body.classList.toggle('dark-mode');
        sidebar.classList.toggle('dark-mode');
        musicPlayer.classList.toggle('dark-mode');
        navigation.classList.toggle('dark-mode');

         // 로컬 스토리지에 현재 다크 모드 상태를 저장
         const isDarkMode = document.body.classList.contains('dark-mode');
         localStorage.setItem('darkMode', isDarkMode ? 'enabled' : 'disabled');
    }
    
    // 저장된 다크 모드 상태를 확인하여 적용
    const savedDarkMode = localStorage.getItem('darkMode');
    if (savedDarkMode === 'enabled') {
        document.body.classList.add('dark-mode');
        sidebar.classList.add('dark-mode');
        musicPlayer.classList.add('dark-mode');
        navigation.classList.add('dark-mode');
        darkModeToggle.checked = true; // 토글 버튼 상태도 동기화
    }

    // 다크 모드 토글 버튼 클릭 시 다크 모드 상태를 변경
    darkModeToggle.addEventListener('click', toggleDarkMode);
});



