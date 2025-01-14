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
    const html = `
        <div class="content-header-info">
            <img class="sound-image" src="${data.albumArtPath}" alt="음원 이미지" onerror="this.src='/images/album-default-image-01.jpeg'">
            <div class="sound-info">
                <div class="sound-title font-size-large">${data.musicDTO.title}</div>
                <div class="artist-name font-size-medium">
                    <span>Artist</span> <a href="/artist/jonsi" class="artist-link">${data.musicDTO.nickname}</a>
                </div>
                <div class="sound-info-reaction">
                    <button class="btn sound-btn">Play sound</button>
                    <button class="btn sound-btn">Get sound</button>
                    <button class="btn sound-btn">Share sound</button>
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
}

export function renderAlbumOne(data){
    const container = document.getElementById("render-album-info-container");
    // const container = document.getElementById("content-body");
    container.innerHTML= '';

    const html = `
        <div class="content-header-info">
            <img class="sound-image" src="${data.dtoList[0].albumDTO.albumArtPath}" alt="음원 이미지" onerror="this.src='/images/album-default-image-01.jpeg'">
            <div class="sound-info">
                <span>Artist</span><div class="sound-title font-size-large">${data.dtoList[0].albumDTO.nickname}</div>
                <div class="artist-name font-size-medium">
                </div>
                <div class="sound-info-reaction">
                    <button class="btn sound-btn">Play sound</button>
                    <button class="btn sound-btn">Get sound</button>
                    <button class="btn sound-btn">Share sound</button>
                </div>
            </div>
        </div>
        <div class="album-info-text">
            <p class="album-description">
                ${data.dtoList[0].albumDTO.description}
            </p>
            <button class="album-btn show-more-btn">더보기</button>
        </div>
    `;

    container.innerHTML = html;

    const showMoreBtn = document.querySelector('.show-more-btn');
    const albumInfoText = document.querySelector('.album-info-text');
    const albumDescription = document.querySelector('.album-description');

    // 텍스트 높이 측정하여 더보기 버튼 표시 여부 결정
    function checkTextOverflow() {
        const fullHeight = albumDescription.scrollHeight;
        console.log(fullHeight);
        const clampHeight = albumInfoText.clientHeight;
        console.log(clampHeight);
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
}

//<div id="render-sounds-container" class="content-body"></div>
export function renderTotalSounds(data) {
    // const container = document.getElementById("render-sounds-container");
    const container = document.getElementById("content-body");

    container.innerHTML = ''; // 기존 내용 초기화
    data.forEach((sound) => {
        const musicItem = document.createElement('div');
        musicItem.classList.add('music-item');

        musicItem.innerHTML = `
            <div class="music-item-left">
                <img alt="앨범 이미지" class="music-album-img" src="${sound.albumDTO.albumArtPath}" onerror="this.src='/images/album-default-image-01.jpeg'">
                <div class="music-play-btn" data-sound-id="${sound.musicDTO.filePath}" data-sound-album="${sound.albumDTO.albumName}" data-sound-title="${sound.musicDTO.title}" data-sound-art="${sound.albumDTO.albumArtPath}">
                    <img src="/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="재생">
                </div>
                <div class="music-info">
                        <h3 class="album-name" data-album-name="${sound.albumDTO.albumName}" data-nickname="${sound.albumDTO.nickname}">
                            ${sound.albumDTO.albumName}
                        </h3>
                        <p class="track-title" data-track-title="${sound.musicDTO.title}" data-nickname="${sound.albumDTO.nickname}">
                            ${sound.musicDTO.title}
                        </p>
                </div>
                <div class="music-info-time">
                    <p>${sound.musicDTO.musicDuration || '0:00'}</p>
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
                    <img src="/images/link_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="공유">
                </div>
            </div>
        `;

        container.appendChild(musicItem);
    });
}

// export function renderManageAlbums(data) {
//     const container = document.getElementById("render-albums-manage-container");
//     const noAlbumsMessage = document.getElementById("no-albums-message");
//
//     // 기존 데이터 초기화
//     container.innerHTML = '';
//
//     if (data.length === 0) {
//         // 데이터가 없으면 메시지 표시
//         noAlbumsMessage.style.display = 'table-row';
//     } else {
//         // 데이터가 있으면 메시지 숨김
//         noAlbumsMessage.style.display = 'none';
//
//         // 데이터 렌더링
//         const manageHTML = data.map(manage => `
//             <tr>
//                 <td>${manage.albumId}</td>
//                 <td>${manage.nickname}</td>
//                 <td>
//                     <span class="current-value" data-field="albumName">${manage.albumName}</span>
//                     <input type="text" class="editable-field" data-field="albumName" value="${manage.albumName}" style="display: none;">
//                 </td>
//                 <td>
//                     <span class="current-value" data-field="description">${manage.albumDescription}</span>
//                     <input type="text" class="editable-field" data-field="description" value="${manage.albumDescription}" style="display: none;">
//                 </td>
//                 <td>${manage.create_date}</td>
//                 <td>${manage.modify_date}</td>
//                 <td>
//                     <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
//                     <button class="apply-button" style="display: none;" onclick="applyChanges(this, ${manage.albumId})">적용</button>
//                     <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
//                 </td>
//             </tr>
//         `).join('');
//         container.innerHTML = manageHTML;
//     }
// }

export function renderManageTracks(data){
    const container = document.getElementById("render-tracks-manage-container");
    const noAlbumsMessage = document.getElementById("no-tracks-message");

    // 기존 데이터 초기화
    container.innerHTML = '';

    if (data.length === 0) {
        // 데이터가 없으면 메시지 표시
        noAlbumsMessage.style.display = 'table-row';
    } else {
        // 데이터가 있으면 메시지 숨김
        noAlbumsMessage.style.display = 'none';

        // 데이터 렌더링
        const manageHTML = data.map(manage => `
            <tr>
                <td>${manage.musicId}</td>
                <td>${manage.nickname}</td>
                <td>
                    <span class="current-value" data-field="title">${manage.musicTitle}</span>
                    <input type="text" class="editable-field" data-field="title" value="${manage.musicTitle}" style="display: none;">
                </td>
                <td>
                    <span class="current-value" data-field="description">${manage.musicDescription}</span>
                    <input type="text" class="editable-field" data-field="description" value="${manage.musicDescription}" style="display: none;">
                </td>
                <td>${manage.create_date}</td>
                <td>${manage.modify_date}</td>
                <td>
                    <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
                    <button class="apply-button" style="display: none;" onclick="applyChanges(this, ${manage.musicId})">적용</button>
                    <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                </td>
            </tr>
        `).join('');
        container.innerHTML = manageHTML;
    }
}

//export function renderManageTags(data){
//    const container = document.getElementById("render-tags-manage-container");
//    const noAlbumsMessage = document.getElementById("no-tags-message");
//
//    // 기존 데이터 초기화
//    container.innerHTML = '';
//
//    if (data.length === 0) {
//        // 데이터가 없으면 메시지 표시
//        noAlbumsMessage.style.display = 'table-row';
//    } else {
//        // 데이터가 있으면 메시지 숨김
//        noAlbumsMessage.style.display = 'none';
//
//        // 데이터 렌더링
//        const manageHTML = data.map(manage => `
//            <tr>
//                <td>${manage.musicId}</td>
//                <td>${manage.nickname}</td>
//                <td>${manage.musicTitle}</td>
//                <td>
//                    <span class="current-value" data-field="instTags">Guitar, Piano</span>
//                </td>
//                <td>
//                    <span class="current-value" data-field="moodTags">Happy, Chill</span>
//                </td>
//                <td>
//                    <span class="current-value" data-field="genreTags">Rock, Jazz</span>
//                </td>
//                <td>${manage.create_date}</td>
//                <td>${manage.modify_date}</td>
//                <td>
//                    <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
//                    <button class="apply-button" style="display: none;" onclick="applyChanges(this, ${manage.musicId})">적용</button>
//                <button type="button" class="open-modal">태그 찾기</button>
//                    <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
//                </td>
//            </tr>
//        `).join('');
//        container.innerHTML = manageHTML;
//    }
//}

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
                        <img class="list-album-image" src="${album.albumDTO.albumArtPath}" alt="Album Image" onerror="this.src='/images/album-default-image-01.jpeg'">
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
            alert("!!");
            const albumName = item.dataset.albumName;
            const nickname = item.dataset.nickname;
            // 페이지 이동
            const newURL = `/sounds/albums/one?nickname=${nickname}&albumName=${albumName}`;

            router.navigate(newURL);
            //const response = await axiosGet({endpoint:'/api/sounds/albums/'+ nickname +'/title/'+ albumName +'}'  });
        });
    });
}
