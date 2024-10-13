// 플레이어 보이기/숨기기 트리거 (예: 다른 요소에 연결)
// .play-trigger 요소에 클릭 이벤트 리스너 추가
// 해당 트리거를 클릭하면 음악 플레이어의 'visible' 클래스를 토글하여 플레이어를 보이거나 숨김
document.querySelector('.play-trigger')?.addEventListener('click', function() {
    console.log("click play");  // 클릭 시 콘솔에 "click play" 출력
    var musicPlayer = document.getElementById('musicPlayer'); // 음악 플레이어 요소 선택
    musicPlayer.classList.toggle('visible');  // 'visible' 클래스를 토글하여 플레이어의 보이기/숨기기 조절
    //classList.toggle('visible')는 해당 클래스가 없을 때는 추가하고, 이미 있으면 제거하는 동작을 합니다.
});

// 재생/정지 버튼 토글
// #playButton 요소에 클릭 이벤트 리스너 추가
// 클릭할 때마다 재생/정지 아이콘을 변경
document.getElementById('playButton').addEventListener('click', function() {
    var playButton = document.querySelector('#playButton img');  // 재생/정지 버튼의 이미지 요소 선택
    
    // 현재 이미지가 'play_circle'인 경우 재생 중이므로 'pause' 이미지로 변경
    if (playButton.src.includes('play_circle')) {
        playButton.src = 'pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';  // 정지 아이콘으로 변경
        playButton.alt = '정지';  // 대체 텍스트를 '정지'로 설정
    } else {
        // 그렇지 않으면 'play_circle' 이미지로 다시 변경하여 재생 버튼으로 전환
        playButton.src = 'play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';  // 재생 아이콘으로 변경
        playButton.alt = '플레이';  // 대체 텍스트를 '플레이'로 설정
    }
});

// 링크 복사 기능
// #copyLinkButton 요소에 클릭 이벤트 리스너 추가
// 버튼을 클릭하면 링크를 클립보드에 복사하고, 성공 시 알림 메시지를 표시
document.getElementById('copyLinkButton').addEventListener('click', function() {
    // 복사할 링크 예시 (실제 사용 시 링크를 동적으로 생성하거나 변경 가능)
    const linkToCopy = 'https://example.com/music/track'; 

    // 클립보드에 텍스트 복사
    navigator.clipboard.writeText(linkToCopy).then(function() {
        // 복사 성공 후 사용자에게 알림 메시지 표시
        const notification = document.getElementById('copyNotification');  // 알림 메시지 요소 선택
        notification.style.display = 'block';  // 메시지를 화면에 보이게 설정
        
        // 2초 후 알림 메시지 숨기기
        setTimeout(function() {
            notification.style.display = 'none';  // 메시지를 다시 숨김
        }, 2000);  // 2초 대기
    }).catch(function(error) {
        // 클립보드 복사 실패 시 에러 로그 출력
        console.error('복사 실패:', error);  // 복사 실패 시 콘솔에 오류 메시지 출력
    });
});
