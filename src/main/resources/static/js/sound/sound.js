import {router} from "/js/router.js";

export function renderTagsFromSearch(data) {
    // const container = document.getElementById("render-tags-sort-container");
    const container = document.getElementById("render-tags-sort-container");
    container.innerHTML = ''; // 기존 내용 초기화

    const html = `
        <div class="music-tag-sort">
            <div class="music-tag-sort-list">
                <img src="/images/label_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="태그">

                <!-- 악기 태그 -->
                <span class="music-tag-sort-toggle" data-target="instrument-section">악기</span>
                <img src="/images/arrow_drop_down_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="늘리기">
                <div class="tag-section hidden" id="instrument-section">
                    ${data.dto.instrument
                        .map(instrument => `<span class="tag-item">${instrument}</span>`)
                        .join('')}
                </div>

                <!-- 분위기 태그 -->
                <span class="music-tag-sort-toggle" data-target="mood-section">분위기</span>
                <img src="/images/arrow_drop_down_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="늘리기">
                <div class="tag-section hidden" id="mood-section">
                    ${data.dto.mood
                        .map(mood => `<span class="tag-item">${mood}</span>`)
                        .join('')}
                </div>

                <!-- 장르 태그 -->
                <span class="music-tag-sort-toggle" data-target="genre-section">장르</span>
                <img src="/images/arrow_drop_down_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="늘리기">
                <div class="tag-section hidden" id="genre-section">
                    ${data.dto.genre
                        .map(genre => `<span class="tag-item">${genre}</span>`)
                        .join('')}
                </div>
            </div>
            <div class="music-tag-display"></div>
        </div>
    `;

    container.innerHTML = html;

}

export function renderSoundOne(data, data2){
    // const container = document.getElementById("render-sound-one-container");
    const container = document.getElementById("content-body");
    container.innerHTML = '';
console.log(data);

    console.log(data);
    const html = `
        <div id="copy-alert" class="copy-alert">링크가 복사되었습니다!</div>
        <div class="content-header-info">
            <img class="sound-image" src="https://soundbrew.storage.s3.ap-northeast-2.amazonaws.com/${data.albumDTO.albumArtPath}" alt="음원 이미지" onerror="this.src='/images/album-default-image-01.jpeg'">
            <div class="sound-info">
                <div class="sound-title font-size-large">${data.musicDTO.title}</div>
                <div class="artist-name font-size-medium">
                    <span>Artist</span> <a href="javascript:void(0);" id="artist-link" class="artist-link">${data.musicDTO.nickname}</a>
                </div>
                <div class="sound-info-reaction">
                    <button class="btn sound-btn">Get sound</button>
                    <button class="btn sound-btn share-btn" data-nickname="${data.albumDTO.nickname}" data-title="${data.musicDTO.title}">Share sound</button>
                </div>
            </div>
        </div>
        <hr style="border: 1px solid #eee;">
        <div class="sound-tag-list">
            <div class="tag-category">
                <h3>Mood</h3>
                <div class="tag-items">
                    ${data2.dto.mood.map( mood => `<span>${mood}</span>` ).join('')}
                </div>
            </div>

            <div class="tag-category">
                <h3>Instrument</h3>
                <div class="tag-items">
                    ${data2.dto.instrument.map( instrument => `<span>${instrument}</span>` ).join('')}
                </div>
            </div>

            <div class="tag-category">
                <h3>Genre</h3>
                <div class="tag-items">
                    ${data2.dto.genre.map( genre => `<span>${genre}</span>` ).join('')}
                </div>
            </div>
        </div>
    `;
    container.innerHTML = html;

    // 이벤트 리스너를 동적으로 추가
    const artistLink = document.getElementById("artist-link");
    artistLink.addEventListener("click", () => {
        const newUrl = `/sounds/albums?keyword=${encodeURIComponent(data.musicDTO.nickname)}&type=n`;
        router.navigate(newUrl);
    });

    // 공유 버튼 클릭 이벤트
    document.querySelectorAll('.share-btn').forEach((shareBtn) => {
        shareBtn.addEventListener('click', (event) => {
            const shareInfo = event.target.closest('.share-btn');
            if (shareInfo) {
                const nickname = shareInfo.dataset.nickname;
                const title = shareInfo.dataset.title;

                const url = window.location.origin;
                // 닉네임과 타이틀을 포함한 완성된 경로
                const shareText = `${url}/sounds/tracks/one?nickname=${encodeURIComponent(nickname)}&title=${encodeURIComponent(title)}`;

                // 클립보드에 텍스트 복사
                navigator.clipboard.writeText(shareText)
                    .then(() => {
                        showCopyAlert();
                    })
                    .catch((err) => {
                        console.error('복사 실패:', err);
                    });
            }
        });
    });

    // 복사 성공 알림 표시 함수
    function showCopyAlert() {
        const alertBox = document.getElementById('copy-alert');
        alertBox.textContent = '링크가 복사되었습니다!';
        alertBox.classList.add('show');

        // 2초 후 알림 숨기기
        setTimeout(() => {
            alertBox.classList.remove('show');
        }, 2000);
    }
}

export function renderAlbumOne(data) {
    const container = document.getElementById("render-album-info-container");
    container.innerHTML = '';

    const html = `
        <div id="copy-alert" class="copy-alert">링크가 복사되었습니다!</div>
        <div class="content-header-info">
            <img class="sound-image" src="https://soundbrew.storage.s3.ap-northeast-2.amazonaws.com/${data.dtoList[0].albumDTO.albumArtPath}" alt="음원 이미지" onerror="this.src='/images/album-default-image-01.jpeg'">
            <div class="sound-info">    
                <span>Artist</span>
                <div class="sound-title font-size-large">
                    <a href="javascript:void(0);" id="artist-link" class="artist-link" style="text-decoration: none; transition: color 0.3s; color: #0056b3;">${data.dtoList[0].albumDTO.nickname}</a>
                </div>
                <div class="artist-name font-size-medium"></div>
                <div class="sound-info-reaction">
                    <button class="btn sound-btn">get..</button>
                    <button class="btn sound-btn share-album-btn" data-title="${data.dtoList[0].albumDTO.albumName}" data-nickname="${data.dtoList[0].albumDTO.nickname}">share album</button>
                </div>
            </div>
        </div>
        <div class="album-info-text">
            <p class="album-description">
                ${data.dtoList[0].albumDTO.description}
            </p>
            <button class="album-btn show-more-btn" style="display: none;">더보기</button>
        </div>
    `;

    container.innerHTML = html;

    const showMoreBtn = document.querySelector('.show-more-btn');
    const albumDescription = document.querySelector('.album-description');
    const artistLink = document.getElementById("artist-link");

    artistLink.addEventListener("click", () => {
        const newUrl = `/sounds/albums?keyword=${encodeURIComponent(data.dtoList[0].albumDTO.nickname)}&type=n`;
        router.navigate(newUrl);
    });

    function checkTextOverflow() {
        // 더보기 버튼 표시 여부 판단
        if (albumDescription.scrollHeight > albumDescription.clientHeight) {
            showMoreBtn.style.display = 'block';
        } else {
            showMoreBtn.style.display = 'none';
        }
    }

    window.addEventListener('load', checkTextOverflow);
    window.addEventListener('resize', checkTextOverflow);

    showMoreBtn.addEventListener('click', function() {
        albumDescription.classList.toggle('expanded');
        showMoreBtn.textContent = albumDescription.classList.contains('expanded') ? '접기' : '더보기';
    });

    // 공유 버튼 클릭 이벤트
    document.querySelector('.share-album-btn').addEventListener('click', (event) => {
        const shareInfo = event.target.closest('.share-album-btn');
        console.log(shareInfo);
        if (shareInfo) {
            const nickname = shareInfo.dataset.nickname;
            const title = shareInfo.dataset.title;

            const url = window.location.origin;
            const shareText = `${url}/sounds/albums/one?nickname=${encodeURIComponent(nickname)}&albumName=${encodeURIComponent(title)}`;

            // 클립보드에 텍스트 복사
            navigator.clipboard.writeText(shareText)
                .then(() => {
                    showCopyAlert();
                })
                .catch((err) => {
                    console.error('복사 실패:', err);
                });
        }
    });

    // 복사 성공 알림 표시 함수
    function showCopyAlert() {
        const alertBox = document.getElementById('copy-alert');
        alertBox.textContent = '링크가 복사되었습니다!';
        alertBox.classList.add('show');

        // 2초 후 알림 숨기기
        setTimeout(() => {
            alertBox.classList.remove('show');
        }, 2000);
    }
}

//<div id="render-sounds-container" class="content-body"></div>
export function renderTotalSounds(data) {
    const container = document.getElementById("content-body");

    container.innerHTML = ''; // 기존 내용 초기화

    if (!data || data.length === 0) {
        // 검색 결과가 없을 경우 메시지 렌더링
        container.innerHTML = '<span>검색결과가 없습니다</span>';
        return;
    }

    data.forEach((sound) => {
        const musicItem = document.createElement('div');
        musicItem.classList.add('music-item');

        musicItem.innerHTML = `
            <!-- 알림 메시지 -->
            <div id="copy-alert" class="copy-alert">링크가 복사되었습니다!</div>
            <div class="music-item-left">
                <img alt="앨범 이미지" class="music-album-img" src="https://soundbrew.storage.s3.ap-northeast-2.amazonaws.com/${sound.albumDTO.albumArtPath}" onerror="this.src='/images/album-default-image-01.jpeg'">
                <div class="music-play-btn" data-sound-id="${sound.musicDTO.filePath}" data-sound-album="${sound.albumDTO.albumName}" data-sound-title="${sound.musicDTO.title}" data-sound-art="${sound.albumDTO.albumArtPath}">
                    <img src="/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="재생">
                </div>
                <div class="music-info">
                        <h3 class="track-title" data-track-title="${sound.musicDTO.title}" data-nickname="${sound.albumDTO.nickname}">
                            ${sound.musicDTO.title}
                        </h3>
                        <p class="album-name" data-album-name="${sound.albumDTO.albumName}" data-nickname="${sound.albumDTO.nickname}">
                            ${sound.albumDTO.albumName}
                        </p>
                </div>
                <div class="music-info-time">
                <!--<p>${sound.musicDTO.musicDuration || '0:00'}</p>-->
                </div>
            </div>

            <div class="music-item-center">
                <div class="music-info-tag">
                    <span>${(sound.tagsStreamDTO.instrumentTagName || '기타').replace(/,/g, " ")}</span>
                    <span>${(sound.tagsStreamDTO.moodTagName || '없음').replace(/,/g, " ")}</span>
                    <span>${(sound.tagsStreamDTO.genreTagName || '기타').replace(/,/g, " ")}</span>
                </div>
            </div>

            <div class="music-item-right">
                <div class="music-actions">
                    <img src="/images/download_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="다운로드">
                    <img src="/images/shopping_bag_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="장바구니">
                    <img src="/images/link_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="공유" class="share-btn" data-nickname="${sound.albumDTO.nickname}" data-title="${sound.musicDTO.title}">
                </div>
            </div>
        `;

        container.appendChild(musicItem);
    });

    // 공유 버튼 클릭 이벤트
    document.querySelectorAll('.share-btn').forEach((shareBtn) => {
        shareBtn.addEventListener('click', (event) => {
            const shareInfo = event.target.closest('.share-btn');
            if (shareInfo) {
                const nickname = shareInfo.dataset.nickname;
                const title = shareInfo.dataset.title;

                const url = window.location.origin;
                // 닉네임과 타이틀을 포함한 완성된 경로
                const shareText = `${url}/sounds/tracks/one?nickname=${encodeURIComponent(nickname)}&title=${encodeURIComponent(title)}`;

                // 클립보드에 텍스트 복사
                navigator.clipboard.writeText(shareText)
                    .then(() => {
                        showCopyAlert();
                    })
                    .catch((err) => {
                        console.error('복사 실패:', err);
                    });
            }
        });
    });

    // 복사 성공 알림 표시 함수
    function showCopyAlert() {
        const alertBox = document.getElementById('copy-alert');
        alertBox.textContent = '링크가 복사되었습니다!';
        alertBox.classList.add('show');

        // 2초 후 알림 숨기기
        setTimeout(() => {
            alertBox.classList.remove('show');
        }, 2000);
    }
}

export function renderTotalAlbums(data) {
    // const container = document.getElementById("render-sounds-container");
    const container = document.getElementById("content-body");
    container.innerHTML = ''; // 기존 내용 초기화

    const albumListHTML = `
        <div class="list-albums">
            <h2 class="list-albums-title">${data.length}개의 검색결과</h2>
            <h1 class="list-albums-title">Album Pack Search</h1>
            <div class="list-albums-list">
                ${data.map(album => `
                    <div class="list-album-item" data-album-name="${album.albumDTO.albumName}" data-nickname="${album.albumDTO.nickname}">
                        <img class="list-album-image" src="https://soundbrew.storage.s3.ap-northeast-2.amazonaws.com/${album.albumDTO.albumArtPath}" alt="Album Image" onerror="this.src='/images/album-default-image-01.jpeg'">
                        <div class="list-album-name">${album.albumDTO.albumName}</div>
                        <div class="list-album-artist">${album.albumDTO.nickname}</div>
                    </div>
                `).join('')}
            </div>
        </div>
    `;

    container.innerHTML = albumListHTML;

    const albumItems = document.querySelectorAll('.list-album-item');
    albumItems.forEach(item => {
        item.addEventListener('click', async () => {
            const albumName = item.dataset.albumName;
            const nickname = item.dataset.nickname;
            // 페이지 이동
            const newURL = `/sounds/albums/one?nickname=${nickname}&albumName=${albumName}`;

            router.navigate(newURL);
        });
    });
}
