document.addEventListener('DOMContentLoaded', function() {
    const html = document.documentElement;

    const darkModeToggle = document.getElementById('darkModeToggle');
    const sidebar = document.querySelector('.sidebar');
    const navigation = document.querySelector('.navigation');
    const sitename = document.querySelector('.sitename');
    const searchBar = document.querySelector('.searchContainer');
    const content = document.querySelector('.content');
    // const viewToggleBtn = document.querySelector('.viewToggleBtn');
    const article = document.querySelector('.article');
    const musicSort = document.querySelector('.music-sort');

    const uploadMyImage =document.querySelector('.myImage');
    const uploadMyTrack =document.querySelector('.myTrack');
    const uploadMyForm =document.querySelector('.myForm');

    const audioBar = document.querySelector('.audio-player-bar');
    const tagitem = document.querySelector('.tag-item');


    // 다크 모드 상태를 토글하는 함수
    function toggleDarkMode() {
        document.body.classList.toggle('dark-mode');
        sidebar?.classList.toggle('dark-mode');
        navigation?.classList.toggle('dark-mode');
        audioBar?.classList.toggle('dark-mode');
        article?.classList.toggle('dark-mode');
        sitename?.classList.toggle('dark-mode');
        searchBar?.classList.toggle('dark-mode');
        content?.classList.toggle('dark-mode');
        musicSort?.classList.toggle('dark-mode');
        uploadMyTrack?.classList.toggle('dark-mode');
        uploadMyForm?.classList.toggle('dark-mode');
        uploadMyImage?.classList.toggle('dark-mode');
        tagitem?.classList.toggle('dark-mode');
        html?.classList.toggle('dark-mode');


        // 로컬 스토리지에 현재 다크 모드 상태를 저장
        const isDarkMode = document.body.classList.contains('dark-mode');
        localStorage.setItem('darkMode', isDarkMode ? 'enabled' : 'disabled');
    }

    // 저장된 다크 모드 상태를 확인하여 적용
    const savedDarkMode = localStorage.getItem('darkMode');
    if (savedDarkMode === 'enabled') {
        document.body.classList.add('dark-mode');
        sidebar?.classList.add('dark-mode');
        navigation?.classList.add('dark-mode');
        audioBar?.classList.add('dark-mode');
        article?.classList.add('dark-mode');
        sitename?.classList.add('dark-mode');
        searchBar?.classList.add('dark-mode');
        content?.classList.add('dark-mode');
        musicSort?.classList.add('dark-mode');
        uploadMyImage?.classList.add('dark-mode');
        uploadMyTrack?.classList.add('dark-mode');
        uploadMyForm?.classList.add('dark-mode');
        tagitem?.classList.add('dark-mode');
        html?.classList.add('dark-mode');

        darkModeToggle.checked = true; // 토글 버튼 상태도 동기화
    }

    // 다크 모드 토글 버튼 클릭 시 다크 모드 상태를 변경
    darkModeToggle.addEventListener('click', toggleDarkMode);
});



