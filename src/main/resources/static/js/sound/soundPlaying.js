    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트
    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트
    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트
    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트
    const clickContainer = document.getElementById('render-sounds-container');

    // 플레이 버튼 클릭 이벤트 (정지 및 재생)
    clickContainer.addEventListener('click', (event) => {
        const playButton = event.target.closest('.music-play-btn');
        if (playButton) {
            const soundId = playButton.dataset.soundId; // 고유 ID 가져오기
            const albumName = playButton.dataset.soundAlbum;
            const title = playButton.dataset.soundTitle;
            const albumArtPath = playButton.dataset.soundArt;

            // 현재 재생 중인 음원 확인
            if (wavesurfer.isPlaying() && loadedSoundId === soundId) {
                // 재생 중이면 멈추기
                wavesurfer.pause();
                // UI 상태 업데이트 (정지)
                const playerButton = document.querySelector('.player-play-btn img'); // 첫 번째 버튼의 img 태그 선택
                playerButton.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg'; // 정지 아이콘으로 변경
                playButton.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg'; // 예시: 정지 아이콘 변경
                resetMusicListUI(); // 정지 리액션: 목록 UI 초기화
            } else {
                // 새로운 음원 재생
                playSound(soundId); // 기존 playSound 함수 호출
                // UI 업데이트
                updatePlayerUI({ albumArtPath, albumName, title, soundId });
                // 음원 리스트 UI 업데이트
                updateMusicListUI(soundId); // UI 업데이트 함수 호출
                playButton.src = '/images/pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg'; // 예시: 재생 아이콘 변경
            }
        }
    });



    function updateMusicListUI(activeSoundId) {
        // 모든 버튼 및 스타일 초기화
        document.querySelectorAll('.music-play-btn img').forEach(img => {
            img.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
        });
        document.querySelectorAll('.music-item-left').forEach(item => {
            item.classList.remove('active-music');
        });
        console.log("updateMusicListUI : "+activeSoundId);
        // 선택된 음원의 버튼과 스타일 변경
        const activePlayBtn = document.querySelector(`.music-play-btn[data-sound-id="${activeSoundId}"] img`);
        const activeMusicItem = document.querySelector(`.music-play-btn[data-sound-id="${activeSoundId}"]`).closest('.music-item-left');
        if (activePlayBtn && activeMusicItem) {
            activePlayBtn.src = '/images/pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
        }
    }

    function resetMusicListUI() {
        // 모든 버튼을 기본 재생 상태로 초기화
        document.querySelectorAll('.music-play-btn img').forEach(img => {
            img.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
        });

        // 모든 음악 아이템의 활성화 상태를 초기화
        document.querySelectorAll('.music-item-left').forEach(item => {
            item.classList.remove('active-music');
        });
    }

    // 앨범 이름 클릭 이벤트
    clickContainer.addEventListener('click', (event) => {
        const albumNameElement = event.target.closest('.album-name');
        if (albumNameElement) {
            const albumName = albumNameElement.dataset.albumName;
            const nickname = albumNameElement.dataset.nickname; // 닉네임 가져오기

            // 페이지 이동
            window.location.href = `/sounds/albums/one?nickname=${nickname}&albumName=${albumName}`;
        }
    });

    // 트랙 제목 클릭 이벤트
    clickContainer.addEventListener('click', (event) => {
        const trackTitleElement = event.target.closest('.track-title');
        if (trackTitleElement) {
            const trackTitle = trackTitleElement.dataset.trackTitle;
            const nickname = trackTitleElement.dataset.nickname; // 닉네임 가져오기

            window.location.href= `/sounds/tracks/one?nickname=${nickname}&title=${trackTitle}`;
        }
    });
    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트
    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트
    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트
    // 스트리밍, 앨범 , 트랙 관련 클릭 이벤트



    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수
    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수
    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수
    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수
   // Initialize Wavesurfer
    const wavesurfer = WaveSurfer.create({
        container: '#waveform',
        waveColor: '#ddd',
        progressColor: '#005da0',
        cursorColor: '#fff',
        height: 50,
        barWidth: 1,
    });

    // Load the audio file
    const playPauseBtn = document.getElementById('play-pause-btn');
    const progressBar = document.getElementById('progress-bar');
    const currentTimeDisplay = document.getElementById('current-time');
    const totalTimeDisplay = document.getElementById('total-time');
    const volumeBar = document.getElementById('volume-bar');



    playPauseBtn.addEventListener('click', () => {
        if (wavesurfer.isPlaying()) {
            // 재생 중일 때, 정지
            wavesurfer.pause();
            playPauseBtn.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';

            // 정지 리액션: 목록 UI 초기화
            resetMusicListUI();
        } else {
            // 정지 상태일 때, 재생
            wavesurfer.play();
            playPauseBtn.src = '/images/pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';

            // 재생 리액션: 목록 UI 업데이트
            const activeSoundId = document.querySelector('.player-info h2').textContent;
            console.log("playPauseBtn : "+activeSoundId);
            updateMusicListUI(activeSoundId);
        }
    });


    let loadedSoundId = '';

    function playSound(soundId) {
        wavesurfer.load(`/stream/${soundId}`);
        if(loadedSoundId !== soundId){
            loadedSoundId=soundId;
        }

        // Update total duration when ready
        wavesurfer.on('ready', function() {
            wavesurfer.play();
            playPauseBtn.src = '/images/pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
            const duration = wavesurfer.getDuration();
            totalTimeDisplay.textContent = formatTime(duration);
            progressBar.max = Math.floor(duration); // Set progress bar max value to duration
        });

        // Update current time, progress bar, and progress bar background during playback
        wavesurfer.on('audioprocess', function () {
            const currentTime = wavesurfer.getCurrentTime();
            const duration = wavesurfer.getDuration();
            currentTimeDisplay.textContent = formatTime(currentTime);

            // 슬라이더 업데이트
            progressBar.value = Math.floor(currentTime);

            // 진행 퍼센트 계산 및 배경 업데이트
            const percentage = (currentTime / duration) * 100;
            progressBar.style.background = `
                linear-gradient(to right, #005da0 ${percentage}%, #ddd ${percentage}%)
            `;
        });
    };

    function updatePlayerUI(soundData) {
        // 음원 플레이어에 데이터 렌더링
        document.querySelector('.player-info h3').textContent = soundData.title;
        document.querySelector('.player-info p').textContent = soundData.albumName;
        document.querySelector('.music-album-img').src = soundData.albumArtPath;
        document.querySelector('.player-info h2').textContent = soundData.soundId;
    }

    // Seek in audio using the progress bar
    progressBar.addEventListener('input', () => {
        const seekTime = progressBar.value;

        const value = progressBar.value; // 슬라이더 현재 값
        const max = progressBar.max; // 슬라이더 최대 값
        const percentage = (value / max) * 100; // 진행 퍼센트 계산

        // 배경색 업데이트
        progressBar.style.background = `
            linear-gradient(to right, #005da0 ${percentage}%, #ddd ${percentage}%)
        `;
        wavesurfer.seekTo(seekTime / wavesurfer.getDuration()); // Seek to the selected time
    });

    // Adjust volume
    volumeBar.addEventListener('input', () => {
        wavesurfer.setVolume(volumeBar.value / 100);
    });

    // Utility function to format time
    function formatTime(seconds) {
        const minutes = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${minutes}:${secs < 10 ? '0' + secs : secs}`;
    }
    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수
    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수
    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수
    //스트리밍과 그에 대한 음원 목록 리액션 관련 함수