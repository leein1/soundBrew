import {router} from "/js/adminSoundRouter.js";
import {serializeFormToJSON} from '/js/serialize/formToJson.js';
import {inputHandler} from '/js/check/inputHandler.js';
import {axiosPatch, axiosPost, axiosDelete} from '/js/fetch/standardAxios.js';
import {formatDate} from "/js/formatDate.js";

// 전역 함수로 enableEditing
window.enableEditing = function(button) {
    const row = button.closest('tr');

    // 기존 값 감추기
    row.querySelectorAll('.current-value').forEach(span => {
        span.style.display = 'none';
    });

    // 수정 필드 보이기
    row.querySelectorAll('.editable-field').forEach(input => {
        input.style.display = 'inline-block';
    });

    // 버튼 상태 변경
    button.style.display = 'none';
    row.querySelector('.apply-button').style.display = 'inline-block';

    const deleteButton =  row.querySelector('.delete-button');
    if(deleteButton)deleteButton.style.display = 'inline-block';

    const watchButton = row.querySelector('.watch-button');
    if(watchButton)watchButton.style.display='inline-block';

    row.querySelector('.cancel-button').style.display = 'inline-block';
}

// 전역 함수로 updateUI
window.updateUI = function(row, updatedData, button) {
    // 수정된 데이터 UI 반영
    row.querySelectorAll('.current-value').forEach(span => {
        const field = span.dataset.field;
        span.textContent = updatedData[field];
        span.style.display = 'inline-block';
    });

    // 입력 필드 숨기기
    row.querySelectorAll('.editable-field').forEach(input => {
        input.style.display = 'none';
    });

    // 버튼 상태 복원
    row.querySelector('.edit-button').style.display = 'inline-block';
    button.style.display = 'none';
    row.querySelector('.cancel-button').style.display = 'none';
}

// 전역 함수로 createFormData
window.createFormData = function(row) {
    const form = document.createElement('form');
    form.id = 'myForm';

    // 수정된 데이터 수집 후, 폼에 hidden input으로 추가
    row.querySelectorAll('.editable-field').forEach(input => {
        const name = input.dataset.field;
        const value = input.value;

        // hidden input을 만들어서 폼에 추가
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';  // hidden 타입으로 설정
        hiddenInput.name = name;  // 필드명
        hiddenInput.value = value;  // 입력된 값

        form.appendChild(hiddenInput);
    });

    return form;
}

window.applyAdminAlbumsChanges = async function(button, albumId) {
    const row = button.closest('tr');
    const container = document.getElementById("render-update");
    container.innerHTML = '';  // 기존 폼 비우기

    const formData = createFormData(row);  // 폼 데이터 생성

    // 폼을 body에 추가
    container.appendChild(formData);

    // 서버에 데이터 전송
    await sendAdminAlbumsUpdateRequest(albumId, formData);
}

window.applyAlbumsDelete = async function(button, albumId){
    const handle ={
        onSuccess:() =>{
            alert("요청한 앨범을 삭제하였습니다.");
            router.navigate("/sounds/tracks");
        },
        onBadRequest:()=>{
            alert("앨범을 삭제하지못했습니다.");
            router.navigate("/admin/albums");
        }
    }

    await axiosDelete({endpoint: '/api/admin/albums/' + albumId, handle});
}

window.applyTrackDelete = async function(button, musicId){
    alert(musicId);

    const handle ={
        onSuccess:() =>{
            alert("요청한 트랙을 삭제하였습니다.");
            router.navigate("/admin/tracks");
        },
        onBadRequest:()=>{
            alert("트랙을 삭제하지못했습니다.");
            router.navigate("/admin/tracks");
        }
    }

    await axiosDelete({ endpoint:'/api/admin/tracks/'+musicId, handle });
}

window.applyAdminTracksChanges = async function(button, albumId) {
    const row = button.closest('tr');
    const container = document.getElementById("render-update");
    container.innerHTML = '';  // 기존 폼 비우기

    const formData = createFormData(row);  // 폼 데이터 생성

    // 폼을 body에 추가
    container.appendChild(formData);

    // 서버에 데이터 전송
    await sendAdminTracksUpdateRequest(albumId, formData);
}

window.applyAlbumsVerify = async function(button, albumId){
    const handle ={
        onSuccess:()=>{
          alert("대기중인 앨범을 성공적으로 확인했습니다.");
        },
    }

    await axiosPatch({ endpoint:`/api/admin/albums/${albumId}/verify`, handle});
}

// 전역 함수로 cancelChanges
window.cancelChanges = function(button) {
    const row = button.closest('tr');

    // 입력 필드 초기화
    row.querySelectorAll('.editable-field').forEach(input => {
        const field = input.dataset.field;
        const currentValue = row.querySelector(`.current-value[data-field="${field}"]`).textContent;
        input.value = currentValue;
        input.style.display = 'none';
    });

    // 기존 값 보이기
    row.querySelectorAll('.current-value').forEach(span => {
        span.style.display = 'inline-block';
    });

    // 버튼 상태 복원
    row.querySelector('.edit-button').style.display = 'inline-block';
    button.style.display = 'none';
    row.querySelector('.apply-button').style.display = 'none';

    const deleteButton =  row.querySelector('.delete-button');
    if(deleteButton)deleteButton.style.display = 'none';

    const watchButton = row.querySelector('.watch-button');
    if(watchButton)watchButton.style.display='none';
}

// 폼을 서버로 전송하는 함수
window.sendAdminAlbumsUpdateRequest = async function(albumId, formData) {
    const response = serializeFormToJSON(formData);

    const { errors, processedData } = inputHandler(response,formData);

    const handle= {
        onBadRequest: ()=>{
            alert("입력한 정보에서 오류가 발생했습니다.");
            router.navigate("/me/sounds/albums");
        },
        onSuccess:()=>{
            alert("입력한 정보로 수정했습니다.")
            router.navigate("/me/sounds/albums");
        },
    }

    if (!errors) {
        await axiosPatch({endpoint: '/api/admin/albums/' + albumId, body: processedData,handle});
    }

}

// 폼을 서버로 전송하는 함수
window.sendAdminTracksUpdateRequest = async function(musicId, formData) {
    const response = serializeFormToJSON(formData);

    const { errors, processedData } = inputHandler(response,formData);

    const handle= {
        onBadRequest: ()=>{
            alert("입력한 정보에서 오류가 발생했습니다.");
            router.navigate("/me/sounds/tracks");
        },
        onSuccess:()=>{
            alert("입력한 정보로 수정했습니다.")
            router.navigate("/me/sounds/tracks");
        },
    }

    if (!errors) {
        await axiosPatch({ endpoint: '/api/admin/tracks/' + musicId, body: processedData,handle });
    }
}

window.viewVerify = function(button,id, uid){
    console.log("id : "+ id);
    console.log("name : "+uid);

    router.navigate(`/admin/albums/one/verify?id=${id}&uid=${uid}`);
}

window.applyTagSpellingChanges = async function (button, originalTag, category) {
    // 버튼의 부모 tr 요소를 찾습니다.
    const row = button.closest('tr');

    // 해당 tr 안에 있는 input 필드를 찾아서 값을 가져옵니다.
    const inputField = row.querySelector('.editable-field');

    // input 필드에서 변경된 태그명
    const newTag = inputField.value;

    // 변경된 태그명 사용 (원본 태그명은 originalTag, 카테고리는 category)
    console.log('원본 태그명:', originalTag);
    console.log('수정된 태그명:', newTag);
    console.log('카테고리:', category);
    const afterNewTag = newTag.replace(/"/g, '');

    const pattern = new RegExp("^[a-z0-9.,()-_\\s]+$");
    if (pattern.test(afterNewTag)) {
        if (category === 'instrument') {
            await axiosPatch({endpoint: `/api/admin/tags/instruments/${originalTag}`,body:afterNewTag})
        }
        if (category === 'mood') {
            await axiosPatch({endpoint: `/api/admin/tags/moods/${originalTag}`,body:afterNewTag})
        }
        if (category === 'genre') {
            await axiosPatch({endpoint: `/api/admin/tags/genres/${originalTag}`,body:afterNewTag})
        }
    } else {
        alert('태그 형식이 잘못 되었습니다');
    }


}

export async function renderArtistsTracks(data){
    try{
        const container = document.getElementById("content-body");
        container.innerHTML = '';
        console.log(data);
        console.log(data.dtoList);

        // 앨범 정보가 있으면 테이블 렌더링
        if (data.dtoList && data.dtoList.length > 0) {
            // 데이터 렌더링
            const manageHTML = `
                <h3>음원 정보 수정</h3>
                <div id="render-update" class="render-update"></div>
                <div class="table-wrapper">
                    <table class="table-container">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>아티스트</th>
                                <th>음원 제목</th>
                                <th>음원 설명</th>
                                <th>업로드일</th>
                                <th>수정일</th>
                                <th>작업</th>
                            </tr>
                        </thead>
                            ${data.dtoList.map(manage => `
                                <tr>
                                    <td>${manage.musicDTO.musicId}</td>
                                    <td>${manage.albumDTO.nickname}</td>
                                    <td>
                                        <span class="current-value" data-field="title">${manage.musicDTO.title}</span>
                                        <input type="text" class="editable-field" data-field="title" value="${manage.musicDTO.title}" style="display: none;">
                                    </td>
                                    <td>
                                        <span class="current-value" data-field="description">${manage.musicDTO.description}</span>
                                        <input type="text" class="editable-field" data-field="description" value="${manage.musicDTO.description}" style="display: none;">
                                    </td>
                                    <td>${formatDate(manage.musicDTO.createDate)}</td>
                                    <td>${formatDate(manage.musicDTO.modifyDate)}</td>
                                    <td>
                                        <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
                                        <button class="apply-button" style="display: none;" onclick="applyAdminTracksChanges(this, ${manage.musicDTO.musicId})">적용</button>
                                        <button class="delete-button" style="display: none;" onclick="applyTrackDelete(this, ${manage.musicDTO.musicId})">삭제</button>
                                        <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                                    </td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            `;
            container.innerHTML=manageHTML;

        }else {
            container.innerHTML = '<p>트랙이 없습니다.</p>';
        }
    }catch (error) {
        console.error('Error occurred while rendering:', error);
    }
}

export async function renderArtistsAlbums(data) {
        // API 호출 및 렌더링 처리
        const container = document.getElementById("content-body");
        container.innerHTML = '';

        // 앨범 정보가 있으면 테이블 렌더링
        if (data.dtoList && data.dtoList.length > 0) {
            const manageHTML = `
                <h3>앨범 정보 수정</h3>
                <div id="render-update" class="render-update"></div>
                <table class="table-container">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>아티스트</th>
                            <th>앨범 제목</th>
                            <th>앨범 설명</th>
                            <th>업로드일</th>
                            <th>수정일</th>
                            <th>작업</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.dtoList.map(item => `
                            <tr>
                                <td>${item.albumDTO.albumId}</td>
                                <td>${item.albumDTO.nickname}</td>
                                <td>
                                    <span class="current-value" data-field="albumName">${item.albumDTO.albumName}</span>
                                    <input type="text" class="editable-field" data-field="albumName" value="${item.albumDTO.albumName}" style="display: none;">
                                </td>
                                <td>
                                    <span class="current-value" data-field="description">${item.albumDTO.description}</span>
                                    <input type="text" class="editable-field" data-field="description" value="${item.albumDTO.description}" style="display: none;">
                                </td>
                                <td>${formatDate(item.albumDTO.createDate)}</td>
                                <td>${formatDate(item.albumDTO.modifyDate)}</td>
                                <td>
                                    <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
                                    <button class="apply-button" style="display: none;" onclick="applyAdminAlbumsChanges(this, ${item.albumDTO.albumId})">적용</button>
                                    <button class="delete-button" style="display: none;" onclick="applyAlbumsDelete(this, ${item.albumDTO.albumId})">삭제</button>
                                    <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            `;

            container.innerHTML = manageHTML;
        } else {
            // 데이터가 없으면 메시지 표시
            container.innerHTML = '<p>앨범이 없습니다.</p>';
        }
}

export async function renderArtistsVerify(data){
    // API 호출 및 렌더링 처리
    const container = document.getElementById("content-body");
    container.innerHTML = '';

    // 앨범 정보가 있으면 테이블 렌더링
    if (data.dtoList && data.dtoList.length > 0) {
        const manageHTML = `
                <h3>앨범 정보 수정</h3>
                <div id="render-update" class="render-update"></div>
                <div class="table-wrapper">
                    <table class="table-container">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>아티스트</th>
                                <th>앨범 제목</th>
                                <th>앨범 설명</th>
                                <th>업로드일</th>
                                <th>작업</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${data.dtoList.map(item => `
                                <tr>
                                    <td>${item.albumDTO.albumId}</td>
                                    <td>${item.albumDTO.nickname}</td>
                                    <td>
                                        <span class="current-value" data-field="albumName">${item.albumDTO.albumName}</span>
                                        <input type="text" class="editable-field" data-field="albumName" value="${item.albumDTO.albumName}" style="display: none;">
                                    </td>
                                    <td>
                                        <span class="current-value" data-field="description">${item.albumDTO.description}</span>
                                        <input type="text" class="editable-field" data-field="description" value="${item.albumDTO.description}" style="display: none;">
                                    </td>
                                    <td>${formatDate(item.albumDTO.createDate)}</td>
                                    <td>
                                        <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
                                        <button class="apply-button" style="display: none;" onclick="applyAlbumsVerify(this, ${item.albumDTO.albumId})">앨범통과</button>
                                        <button class="watch-button" style="display: none" onclick="viewVerify(this, ${item.albumDTO.albumId}, '${item.albumDTO.userId}')">자세히 보기</button>
                                        <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                                    </td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            `;

        container.innerHTML = manageHTML;
    } else {
        // 데이터가 없으면 메시지 표시
        container.innerHTML = '<p>앨범이 없습니다.</p>';
    }
}

export async function renderTagsSpelling(data) {
    const container = document.getElementById("content-body");
    container.innerHTML = '';

    const dto = data.dtoList[0]; // 배열의 첫번째 객체를 선택
    const categories = [
        { category: 'instrument', tags: dto.instrument },
        { category: 'mood', tags: dto.mood },
        { category: 'genre', tags: dto.genre }
    ];

    if (categories && categories.length > 0) {
        const tagsHTML = `
            <h3>태그 관리</h3>
            <div class="table-wrapper">
                <table class="table-container">
                    <thead>
                        <tr>
                            <th>원본 태그명</th>
                            <th>분류</th>
                            <th>수정 후 태그명</th>
                            <th>작업</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${categories.map((category) => `
                            ${category.tags.map((tag) => `
                                <tr>
                                    <td>${tag}</td> <!-- 원본 태그명 -->
                                    <td>${category.category}</td>
                                    <td>
                                        <span class="current-value" data-field="tagName">${tag}</span>
                                        <input type="text" class="editable-field" data-field="tagName" value="${tag}" style="display: none;">
                                    </td>
                                    <td>
                                        <!-- 수정하기 버튼 클릭 시, 카테고리 정보도 함께 전달 -->
                                        <button class="edit-button" onclick="enableEditing(this)">수정하기</button>
                                        <button class="apply-button" style="display: none;" onclick="applyTagSpellingChanges(this, '${tag}', '${category.category}')">적용</button>
                                        <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                                    </td>
                                </tr>
                            `).join('')}
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;

        container.innerHTML = tagsHTML;
    } else {
        container.innerHTML = '<p>태그가 없습니다.</p>';
    }
}

export async function renderTagsNew(data) {
    const container = document.getElementById("content-body");
    container.innerHTML = `
        
        <form id="tag-form">
            <section class="tag-add-section">
                <h3>태그 추가</h3>
                <div id="tag-create-container">
                    <input type="text" id="new-tag-name" name="tagName" placeholder="새 태그명 입력">
                    <select id="new-tag-type" name="tagType">
                        <option value="instrument">Instrument</option>
                        <option value="mood">Mood</option>
                        <option value="genre">Genre</option>
                    </select>
                    <button type="submit">태그 추가</button>
                </div>
            </section>
        </form>
    `;

    document.getElementById("tag-form").addEventListener("submit", async function(event) {
        event.preventDefault(); // 기본 제출 동작 방지

        const tagName = document.getElementById("new-tag-name").value.trim();
        const tagType = document.getElementById("new-tag-type").value;

        if (!tagName) {
            alert("태그명을 입력해주세요.");
            return;
        }

        // TagsDTO 구조에 맞게 데이터 매핑
        const tagsDto = {
            instrument: [],
            mood: [],
            genre: []
        };

        const handle ={
            onSuccess:()=>{
                alert("태그를 정상적으로 생성했습니다.");
                router.navigate('/admin/tags/new');
            },
            onBadRequest:()=>{
                alert("태그를 정상적으로 생성하지 못했습니다.");
                router.navigate('/admin/tags/new');
            },
        }

        const jsonData = serializeFormToJSON(event.target);
        const { errors, processedData } = inputHandler(jsonData, event.target,handle);

        if(!errors){
            if (tagsDto.hasOwnProperty(tagType)) {
                tagsDto[tagType].push(processedData.tagName);
            } else {
                alert(`잘못된 태그 타입: ${tagType}`);
            }

            await axiosPost({endpoint:`/api/admin/tags`, body:tagsDto});
        }
    });
}

export async function renderArtistsVerifyOne(data) {
    // alert("앨범 정보 렌더링 호출 ");
    const container = document.getElementById("render-album-info-container");
    container.innerHTML = '';

    const html = `
        <div id="copy-alert" class="copy-alert">링크가 복사되었습니다!</div>
        <div class="content-header-info">
            <img class="sound-image" src="https://soundbrew.storage.s3.ap-northeast-2.amazonaws.com/${data.dtoList[0].albumDTO.albumArtPath}" alt="음원 이미지" onerror="this.src='/images/album-default-image-01.jpeg'">
            <div class="sound-info">    
                <span>Artist</span>
                <div class="sound-title font-size-large">${data.dtoList[0].albumDTO.nickname}</div>
                <div class="artist-name font-size-medium"></div>
                <div class="sound-info-reaction"></div>
            </div>
        </div>
        <div class="album-info-text">
            <p class="album-description">${data.dtoList[0].albumDTO.description}</p>
            <button class="album-btn show-more-btn" style="display: none;">더보기</button>
        </div>
    `;

    container.innerHTML = html;

    const showMoreBtn = document.querySelector('.show-more-btn');
    const albumDescription = document.querySelector('.album-description');

    // 텍스트 높이 측정하여 더보기 버튼 표시 여부 결정
    function checkTextOverflow() {
        if (albumDescription.scrollHeight > albumDescription.clientHeight) {
            showMoreBtn.style.display = 'block'; // 글이 잘리면 버튼 보이기
        } else {
            showMoreBtn.style.display = 'none'; // 글이 짧으면 버튼 숨기기
        }
    }

    // **HTML을 삽입한 직후 실행**
    checkTextOverflow();

    // **창 크기 조절 시 다시 체크**
    window.addEventListener('resize', checkTextOverflow);

    showMoreBtn.addEventListener('click', function () {
        albumDescription.classList.toggle('expanded');

        if (albumDescription.classList.contains('expanded')) {
            showMoreBtn.textContent = '접기';
            albumDescription.style.maxHeight = 'none'; // 전체 내용 표시
        } else {
            showMoreBtn.textContent = '더보기';
            albumDescription.style.maxHeight = ''; // 원래 상태로 복귀
        }
    });
}

export async function renderTotalSoundsVerify(data) {
    // data가 존재하고, dtoList가 배열인지 확인
    if (!data || !Array.isArray(data.dtoList)) {
        console.error("data.dtoList가 배열이 아닙니다:", data);
        return;
    }

    const container = document.getElementById("content-body");
    container.innerHTML = ''; // 기존 내용 초기화

    data.dtoList.forEach((sound) => {
        const musicItem = document.createElement('div');
        musicItem.classList.add('music-item');

        musicItem.innerHTML = `
        <div class="music-item-left">
            <img alt="앨범 이미지" class="music-album-img" 
                src="https://soundbrew.storage.s3.ap-northeast-2.amazonaws.com/${sound.albumDTO.albumArtPath}" 
                onerror="this.src='/images/album-default-image-01.jpeg'">
            <div class="music-play-btn" 
                data-sound-id="${sound.musicDTO.filePath}" 
                data-sound-album="${sound.albumDTO.albumName}" 
                data-sound-title="${sound.musicDTO.title}" 
                data-sound-art="${sound.albumDTO.albumArtPath}">
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
        </div>

        <div class="music-item-center">
            <div class="music-info-tag">
                <span>${(sound.tagsStreamDTO.instrumentTagName || '기타').replace(/,/g, " ")}</span>
                <span>${(sound.tagsStreamDTO.moodTagName || '없음').replace(/,/g, " ")}</span>
                <span>${(sound.tagsStreamDTO.genreTagName || '기타').replace(/,/g, " ")}</span>
            </div>
        </div>
    `;

        container.appendChild(musicItem);
    });
}

export async function renderSoundsAdminMain(){
    const container = document.getElementById("content-body");
    container.innerHTML='';

    const html = `
        <div id="question-list" class="question-list">
            <div class="linkDiv">
                <div class="statistics-section">
                    <div class="Statistics">
                        사이트 통계 자료 - 방문자 수, 트래픽양, 전체 곡 수 등 자료
                    </div>
                </div>
                <div class="frequently-list">
                    <li id="edit-track">
                        <span class="main-text">음원 관리자 기능</span><br>
                        <span class="sub-text">음원 삭제, 수정</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                    <li id="edit-album">
                        <span class="main-text">앨범 관리자 기능</span><br>
                        <span class="sub-text">앨범 삭제, 수정</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                    <li id="verify-album">
                        <span class="main-text">미등록 앨범 관리</span><br>
                        <span class="sub-text">음원 확인</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                    <li id="edit-tag">
                        <span class="main-text">태그 철자 수정</span><br>
                        <span class="sub-text">오탈자 수정</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                    <li id="create-tag">
                        <span class="main-text">신규 태그 등록</span><br>
                        <span class="sub-text">태그 등록</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                </div>
            </div>
        </div>
    `;

    container.innerHTML = html;

    document.getElementById("edit-track").addEventListener("click", function () {
        alert("음원 관리자 페이지로 이동합니다.");
        router.navigate(`/admin/tracks`);
    });

    document.getElementById("edit-album").addEventListener("click", function () {
        alert("앨범 관리자 페이지로 이동합니다.");
        router.navigate('/admin/albums');
    });

    document.getElementById("verify-album").addEventListener("click", function () {
        alert("미등록 앨범 관리 페이지로 이동합니다.");
        router.navigate(`/admin/albums/verify`);
    });

    document.getElementById("edit-tag").addEventListener("click", function () {
        alert("태그 철자 수정 페이지로 이동합니다.");
        router.navigate('/admin/tags/spelling');
    });

    document.getElementById("create-tag").addEventListener("click", function(){
        alert("태그 생성 페이지로 이동합니다.");
        router.navigate('/admin/tags/new');
    });
}

