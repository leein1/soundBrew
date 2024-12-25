export function renderTagsFromSearch(data) {
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
    const container = document.getElementById("render-sound-one-container");
    container.innerHTML = '';

    const html = `
        <div class="content-header-info">
            <img class="sound-image" src="${data.albumArtPath}" alt="sound Image">
            <div class="sound-info">
                <div class="sound-title font-size-large">${data.musicTitle}</div>
                <div class="artist-name font-size-medium">
                    <span>Album</span> <a href="/album/go" class="album-link">${data.musicTitle}</a>,
                    <span>Artist</span> <a href="/artist/jonsi" class="artist-link">${data.nickname}</a>
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
    container.innerHTML= '';

    const html = `
        <div class="content-header-info">
            <img class="sound-image" src="${data.dtoList[0].albumArtPath}" alt="sound Image">
            <div class="sound-info">
                <span>Artist</span><div class="sound-title font-size-large">${data.dtoList[0].nickname}</div>
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
                ${data.dtoList[0].albumDescription}
            </p>
        </div>
    `;

    container.innerHTML = html;
}

//<div id="render-sounds-container" class="content-body"></div>
export function renderTotalSounds(data) {
    const container = document.getElementById("render-sounds-container");
    container.innerHTML = ''; // 기존 내용 초기화

    data.forEach((sound) => {
        const musicItem = document.createElement('div');
        musicItem.classList.add('music-item');

        musicItem.innerHTML = `
            <div class="music-item-left">
                <img alt="앨범 이미지" class="music-album-img" src="${sound.albumArtPath}">
                <div class="music-play-btn">
                    <img src="/images/play_circle_50dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="재생">
                </div>
                <div class="music-info">
                    <h3>${sound.albumName}</h3>
                    <p>${sound.musicTitle}</p>
                </div>
                <div class="music-info-time">
                    <p>${sound.musicDuration || '0:00'}</p>
                </div>
            </div>

            <div class="music-item-center">
                <div class="music-info-tag">
                    <span>${sound.instrumentTagName || '기타'}</span>
                    <span>${sound.moodTagName || '없음'}</span>
                    <span>${sound.genreTagName || '기타'}</span>
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

export function renderSort() {
    const container = document.getElementById("render-result-sort-container");
    container.innerHTML='';

    const item = document.createElement('div');
    item.classList.add('music-sort');

    item.innerHTML = `
        <div class="music-sort">
            <span class="music-sort-left" id="sortKeyword">정렬
                <img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정렬" id="sortIcon">
            </span>
            <!-- 정렬 드롭다운 -->
            <div class="music-sort-menu" id="musicSortMenu">
                <ul>
                    <li data-sort="download">다운로드순</li>
                    <li data-sort="name">이름순</li>
                    <li data-sort="length">길이순</li>
                </ul>
            </div>
        </div>
    `;

    container.appendChild(item);
}

export function renderManageAlbums(data) {
    const container = document.getElementById("render-albums-manage-container");
    const noAlbumsMessage = document.getElementById("no-albums-message");

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
                <td>${manage.albumId}</td>
                <td>${manage.nickname}</td>
                <td>
                    <span class="current-value" data-field="albumName">${manage.albumName}</span>
                    <input type="text" class="editable-field" data-field="albumName" value="${manage.albumName}" style="display: none;">
                </td>
                <td>
                    <span class="current-value" data-field="description">${manage.albumDescription}</span>
                    <input type="text" class="editable-field" data-field="description" value="${manage.albumDescription}" style="display: none;">
                </td>
                <td>${manage.create_date}</td>
                <td>${manage.modify_date}</td>
                <td>
                    <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
                    <button class="apply-button" style="display: none;" onclick="applyChanges(this, ${manage.albumId})">적용</button>
                    <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                </td>
            </tr>
        `).join('');
        container.innerHTML = manageHTML;
    }
}

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
    const container = document.getElementById("render-albums-container");
    container.innerHTML = ''; // 기존 내용 초기화

    const albumListHTML = `
        <div class="list-albums">
            <h2 class="list-albums-title">${data.length}개의 검색결과</h2>
            <h1 class="list-albums-title">정렬 : 다운로드 순</h1>
            <div class="list-albums-list">
                ${data.map(album => `
                    <div class="list-album-item">
                        <img class="list-album-image" src="${album.albumArtPat || '/images/sample-album-imge.png'}" alt="Album Image">
                        <div class="list-album-name">${album.albumName}</div>
                        <div class="list-album-artist">${album.nickname}</div>
                    </div>
                `).join('')}
            </div>
        </div>
    `;

    container.innerHTML = albumListHTML;
}
