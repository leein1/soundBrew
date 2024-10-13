const showMoreBtn = document.querySelector('.show-more-btn');
const albumInfoText = document.querySelector('.album-info-text');
const albumDescription = document.querySelector('.album-description');

// 텍스트 높이 측정하여 더보기 버튼 표시 여부 결정
function checkTextOverflow() {
    const fullHeight = albumDescription.scrollHeight;
    const clampHeight = albumInfoText.clientHeight;

    if (fullHeight > clampHeight) {
        showMoreBtn.style.display = 'block'; // 글이 잘리면 더보기 버튼 보이기
    } else {
        showMoreBtn.style.display = 'none'; // 글이 짧으면 더보기 버튼 숨기기
    }
}

// 페이지 로드 후 더보기 버튼 체크
window.addEventListener('load', checkTextOverflow);
window.addEventListener('resize', checkTextOverflow); // 창 크기 조정 시에도 체크

showMoreBtn.addEventListener('click', function() {
    albumInfoText.classList.toggle('expanded');
    
    if (albumInfoText.classList.contains('expanded')) {
        showMoreBtn.textContent = '접기';
    } else {
        showMoreBtn.textContent = '더보기';
    }
});
