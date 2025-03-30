import { router } from "/js/router.js";
import { axiosGet } from"/js/fetch/standardAxios.js"

// Clipboard 복사 fallback 함수
function fallbackCopyTextToClipboard(text) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    // 화면에 보이지 않도록 설정
    textArea.style.position = "fixed";
    textArea.style.top = "0";
    textArea.style.left = "0";
    textArea.style.opacity = "0";
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();

    try {
        const successful = document.execCommand('copy');
        if (successful) {
            console.log("Fallback: 복사 성공");
        } else {
            console.error("Fallback: 복사 실패");
        }
    } catch (err) {
        console.error("Fallback: 복사 에러", err);
    }
    document.body.removeChild(textArea);
}

// 복사 성공 알림 함수 (각 함수 내에서 재사용)
function showCopyAlert() {
    const alertBox = document.getElementById('copy-alert');
    if (alertBox) {
        alertBox.textContent = '링크가 복사되었습니다!';
        alertBox.classList.add('show');
        // 2초 후 알림 숨기기
        setTimeout(() => {
            alertBox.classList.remove('show');
        }, 2000);
    }
}

export function renderSoundOne(data, data2) {
    const container = document.getElementById("content-body");
    container.innerHTML = '';
    console.log(data);

    const html = `
        <div id="copy-alert" class="copy-alert">링크가 복사되었습니다!</div>
        <div class="content-header-info">
            <img class="sound-image" src="https://d1lq7t3sqkotey.cloudfront.net/${data.albumDTO.albumArtPath}" alt="음원 이미지" onerror="this.src='/images/album-default-image-01.jpeg'">
            <div class="sound-info">
                <div class="sound-title font-size-large">${data.musicDTO.title}</div>
                <div class="artist-name font-size-medium">
                    <span>Artist</span> <a href="javascript:void(0);" id="artist-link" class="artist-link">${data.musicDTO.nickname}</a>
                </div>
                <div class="sound-info-reaction">
                    <button class="btn sound-btn download-btn" data-filePath="${data.musicDTO.filePath}">Get sound</button>
                    <button class="btn sound-btn share-btn" data-nickname="${data.musicDTO.nickname}" data-title="${data.musicDTO.title}">Share sound</button>
                </div>
            </div>
        </div>
        <hr style="border: 1px solid #eee;">
        <div class="sound-tag-list">
            <div class="tag-category">
                <h3>Mood</h3>
                <div class="tag-items">
                    ${data2.dto.mood.map(mood => `<span>${mood}</span>`).join('')}
                </div>
            </div>
            <div class="tag-category">
                <h3>Instrument</h3>
                <div class="tag-items">
                    ${data2.dto.instrument.map(instrument => `<span>${instrument}</span>`).join('')}
                </div>
            </div>
            <div class="tag-category">
                <h3>Genre</h3>
                <div class="tag-items">
                    ${data2.dto.genre.map(genre => `<span>${genre}</span>`).join('')}
                </div>
            </div>
        </div>
    `;
    container.innerHTML = html;

    // 아티스트 링크 클릭 이벤트
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
                const shareText = `${url}/sounds/tracks/one?nickname=${encodeURIComponent(nickname)}&title=${encodeURIComponent(title)}`;

                if (navigator.clipboard && navigator.clipboard.writeText) {
                    navigator.clipboard.writeText(shareText)
                        .then(() => {
                            showCopyAlert();
                        })
                        .catch((err) => {
                            console.error('복사 실패:', err);
                            fallbackCopyTextToClipboard(shareText);
                            showCopyAlert();
                        });
                } else {
                    fallbackCopyTextToClipboard(shareText);
                    showCopyAlert();
                }
            }
        });
    });

    // 다운로드 버튼 클릭 이벤트 (수정)
    document.querySelectorAll('.download-btn').forEach((downloadBtn) => {
        downloadBtn.addEventListener('click', (event) => {
            const download = event.target.closest('.download-btn');
            if (download) {
                const filePath = download.dataset.filePath;
                alert(filePath);
            }
        });
    });
}

export function renderAlbumOne(data) {
    const container = document.getElementById("render-album-info-container");
    container.innerHTML = '';

    const html = `
        <div id="copy-alert" class="copy-alert">링크가 복사되었습니다!</div>
        <div class="content-header-info">
            <img class="sound-image" src="https://d1lq7t3sqkotey.cloudfront.net/${data.dtoList[0].albumDTO.albumArtPath}" alt="음원 이미지" onerror="this.src='/images/album-default-image-01.jpeg'">
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

            if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard.writeText(shareText)
                    .then(() => {
                        showCopyAlert();
                    })
                    .catch((err) => {
                        console.error('복사 실패:', err);
                        fallbackCopyTextToClipboard(shareText);
                        showCopyAlert();
                    });
            } else {
                fallbackCopyTextToClipboard(shareText);
                showCopyAlert();
            }
        }
    });
}

export function renderTotalSounds(data) {
    const container = document.getElementById("content-body");
    container.innerHTML = '';

    if (!data || data.length === 0) {
        container.innerHTML = '<span>검색결과가 없습니다</span>';
        return;
    }

    data.forEach((sound) => {
        const musicItem = document.createElement('div');
        musicItem.classList.add('music-item');
        musicItem.innerHTML = `
            <div id="copy-alert" class="copy-alert">링크가 복사되었습니다!</div>
            <div class="music-item-left">
                <img alt="앨범 이미지" class="music-album-img" src="https://d1lq7t3sqkotey.cloudfront.net/${sound.albumDTO.albumArtPath}" onerror="this.src='/images/album-default-image-01.jpeg'">
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
                    <img src="/images/download_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" class="download-btn" data-filepath="${sound.musicDTO.filePath}" alt="다운로드">
                    <!--<img src="/images/shopping_bag_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="장바구니">-->
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
                const shareText = `${url}/sounds/tracks/one?nickname=${encodeURIComponent(nickname)}&title=${encodeURIComponent(title)}`;

                if (navigator.clipboard && navigator.clipboard.writeText) {
                    navigator.clipboard.writeText(shareText)
                        .then(() => {
                            showCopyAlert();
                        })
                        .catch((err) => {
                            console.error('복사 실패:', err);
                            fallbackCopyTextToClipboard(shareText);
                            showCopyAlert();
                        });
                } else {
                    fallbackCopyTextToClipboard(shareText);
                    showCopyAlert();
                }
            }
        });
    });

    // 다운로드 버튼 클릭 이벤트
    document.querySelectorAll('.download-btn').forEach((downloadBtn) => {
        downloadBtn.addEventListener('click', async (event) => {
            const download = event.target.closest('.download-btn');
            if (download) {
                const filePath = download.dataset.filepath;
                try{
                const response = await axiosGet({ endpoint: `/api/files/${filePath}`, useToken: true, responseType: 'blob' });
                const url = window.URL.createObjectURL(new Blob([response]));
                const a = document.createElement('a');
                a.href = url;
                a.download = filePath; // 원하는 파일명으로 지정
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);
                } catch (error) {
                  console.error("다운로드 오류:", error);
                }
            }
        });
    });
}

export function renderTotalAlbums(data) {
    const container = document.getElementById("content-body");
    container.innerHTML = '';

    const albumListHTML = `
        <div class="list-albums">
            <h2 class="list-albums-title">${data.length}개의 검색결과</h2>
            <h1 class="list-albums-title">Album Pack Search</h1>
            <div class="list-albums-list">
                ${data.map(album => `
                    <div class="list-album-item" data-album-name="${album.albumDTO.albumName}" data-nickname="${album.albumDTO.nickname}">
                        <img class="list-album-image" src="https://d1lq7t3sqkotey.cloudfront.net/${album.albumDTO.albumArtPath}" alt="Album Image" onerror="this.src='/images/album-default-image-01.jpeg'">
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
            const newURL = `/sounds/albums/one?nickname=${nickname}&albumName=${albumName}`;
            router.navigate(newURL);
        });
    });
}
