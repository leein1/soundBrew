// 플레이어 보이기/숨기기 트리거 (예: 다른 요소에 연결)
document.querySelector('.play-trigger')?.addEventListener('click', function() {
    console.log("click play");
    var musicPlayer = document.getElementById('musicPlayer');
    musicPlayer.classList.toggle('visible');
});

// 재생/정지 버튼 토글
document.getElementById('playButton').addEventListener('click', function() {
    var playButton = document.querySelector('#playButton img');
    
    if (playButton.src.includes('play_circle')) {
        playButton.src = 'pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
        playButton.alt = '정지';
    } else {
        playButton.src = 'play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
        playButton.alt = '플레이';
    }
});

document.getElementById('copyLinkButton').addEventListener('click', function() {
    // 복사할 링크 예시
    const linkToCopy = 'https://example.com/music/track';

    // 클립보드에 텍스트 복사
    navigator.clipboard.writeText(linkToCopy).then(function() {
        // 복사 완료 후 메시지 표시
        const notification = document.getElementById('copyNotification');
        notification.style.display = 'block';
        
        // 2초 후 메시지 숨기기
        setTimeout(function() {
            notification.style.display = 'none';
        }, 2000);
    }).catch(function(error) {
        console.error('복사 실패:', error);
    });
});
