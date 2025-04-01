import { globalStateManager } from '/js/globalState.js';
import {router} from "/js/router.js";
import WaveSurfer from 'https://cdn.jsdelivr.net/npm/wavesurfer.js@7/dist/wavesurfer.esm.js'

// Main Function to Initialize All Event Listeners and Wavesurfer
export function initializeMusicPlayer() {
    // alert("install soundplaying");
    // Initialize Wavesurfer
    const wavesurfer = WaveSurfer.create({
        container: '#waveform',
        waveColor: '#ddd',
        progressColor: '#005da0',
        cursorColor: '#fff',
        height: 50,
        barWidth: 1,
    });

    // Global Variables
    const playPauseBtn = document.getElementById('play-pause-btn');
    const progressBar = document.getElementById('progress-bar');
    const currentTimeDisplay = document.getElementById('current-time');
    const totalTimeDisplay = document.getElementById('total-time');
    const volumeBar = document.getElementById('volume-bar');
    const clickContainer = document.getElementById('content-body');
    let loadedSoundId = '';

    // Function Definitions
    function playSound(soundId) {
        wavesurfer.load(`/api/stream/${soundId}`);
        if (loadedSoundId !== soundId) {
            loadedSoundId = soundId;
        }

        wavesurfer.on('ready', () => {
            wavesurfer.play();
            playPauseBtn.src = '/images/pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
            const duration = wavesurfer.getDuration();
            totalTimeDisplay.textContent = formatTime(duration);
            progressBar.max = Math.floor(duration);
        });

        wavesurfer.on('audioprocess', () => {
            const currentTime = wavesurfer.getCurrentTime();
            const duration = wavesurfer.getDuration();
            currentTimeDisplay.textContent = formatTime(currentTime);

            progressBar.value = Math.floor(currentTime);
            const percentage = (currentTime / duration) * 100;
            progressBar.style.background = `
                linear-gradient(to right, #005da0 ${percentage}%, #ddd ${percentage}%)
            `;
        });
    }

    function updatePlayerUI(soundData) {
        document.querySelector('.player-info h3').textContent = soundData.title;
        document.querySelector('.player-info p').textContent = soundData.albumName;
        const imageSrc = `https://d1lq7t3sqkotey.cloudfront.net/${soundData.albumArtPath}`;

        // 이미지 src 업데이트
        document.querySelector('.player-album-img').src = imageSrc;
        // alert(`Image source: ${imageSrc}`);
        document.querySelector('.player-info h2').textContent = soundData.soundId;
    }

    function updateMusicListUI(activeSoundId) {
        document.querySelectorAll('.music-play-btn img').forEach(img => {
            img.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
        });
        document.querySelectorAll('.music-item-left').forEach(item => {
            item.classList.remove('active-music');
        });

        const activePlayBtn = document.querySelector(`.music-play-btn[data-sound-id="${activeSoundId}"] img`);
        const activeMusicItem = document.querySelector(`.music-play-btn[data-sound-id="${activeSoundId}"]`).closest('.music-item-left');
        if (activePlayBtn && activeMusicItem) {
            activePlayBtn.src = '/images/pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
            activeMusicItem.classList.add('active-music');
        }
    }

    function resetMusicListUI() {
        document.querySelectorAll('.music-play-btn img').forEach(img => {
            img.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
        });
        document.querySelectorAll('.music-item-left').forEach(item => {
            item.classList.remove('active-music');
        });
    }

    function formatTime(seconds) {
        const minutes = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${minutes}:${secs < 10 ? '0' + secs : secs}`;
    }

    // Event Listeners
    playPauseBtn.addEventListener('click', () => {
        if (wavesurfer.isPlaying()) {
            wavesurfer.pause();
            playPauseBtn.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
            resetMusicListUI();
        } else {
            wavesurfer.play();
            playPauseBtn.src = '/images/pause_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
            const activeSoundId = document.querySelector('.player-info h2').textContent;
            updateMusicListUI(activeSoundId);
        }
    });

    progressBar.addEventListener('input', () => {
        const seekTime = progressBar.value;
        const percentage = (seekTime / progressBar.max) * 100;
        progressBar.style.background = `
            linear-gradient(to right, #005da0 ${percentage}%, #ddd ${percentage}%)
        `;
        wavesurfer.seekTo(seekTime / wavesurfer.getDuration());
    });

    volumeBar.addEventListener('input', () => {
        wavesurfer.setVolume(volumeBar.value / 100);
    });

    clickContainer.addEventListener('click', (event) => {
        // 앨범 이름 클릭 처리
        const albumNameElement = event.target.closest('.album-name');
        if (albumNameElement) {
            // alert("앨범 클릭, 다음에 상태 변경 확인해보기;");
            const albumName = albumNameElement.dataset.albumName;
            const nickname = albumNameElement.dataset.nickname;
            const newUrl = `/sounds/albums/one?nickname=${nickname}&albumName=${albumName}`;

            router.navigate(newUrl);
            return; // 이벤트 처리 후 종료
        }

        // 트랙 제목 클릭 처리
        const trackTitleElement = event.target.closest('.track-title');
        if (trackTitleElement) {
            const trackTitle = trackTitleElement.dataset.trackTitle;
            const nickname = trackTitleElement.dataset.nickname;
            const newUrl = `/sounds/tracks/one?nickname=${nickname}&title=${trackTitle}`;

            router.navigate(newUrl);
            return; // 이벤트 처리 후 종료
        }

        // 음악 플레이 버튼 클릭 처리
        const playButton = event.target.closest('.music-play-btn');
        if (playButton) {
            const soundId = playButton.dataset.soundId;
            const albumName = playButton.dataset.soundAlbum;
            const title = playButton.dataset.soundTitle;
            const albumArtPath = playButton.dataset.soundArt;

            if (wavesurfer.isPlaying() && loadedSoundId === soundId) {
                wavesurfer.pause();
                playPauseBtn.src = '/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg';
                resetMusicListUI();
            } else {
                playSound(soundId);
                updatePlayerUI({ albumArtPath, albumName, title, soundId });
                updateMusicListUI(soundId);
            }
        }
    });
}

// Initialize the Music Player on Page Load
initializeMusicPlayer();
