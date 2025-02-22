import {router} from '/js/router.js';
import {serializeFormToJSON} from '/js/serialize/formToJson.js';
import {inputHandler} from '/js/check/inputHandler.js';
import {axiosPatch, axiosPost} from '/js/fetch/standardAxios.js';
import { formatDate} from "/js/formatDate.js";

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

window.applyAlbumsChanges = async function(button, albumId) {
    const row = button.closest('tr');
    const container = document.getElementById("render-update");
    container.innerHTML = '';  // 기존 폼 비우기

    const formData = createFormData(row);  // 폼 데이터 생성

    // 폼을 body에 추가
    container.appendChild(formData);

    // 서버에 데이터 전송
    await sendAlbumsUpdateRequest(albumId, formData);
}

window.applyTracksChanges = async function(button, albumId) {
    const row = button.closest('tr');
    const container = document.getElementById("render-update");
    container.innerHTML = '';  // 기존 폼 비우기

    const formData = createFormData(row);  // 폼 데이터 생성

    // 폼을 body에 추가
    container.appendChild(formData);

    // 서버에 데이터 전송
    await sendTracksUpdateRequest(albumId, formData);
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
}

// 폼을 서버로 전송하는 함수
window.sendAlbumsUpdateRequest = async function(albumId, formData) {
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
        await axiosPatch({endpoint: '/api/me/albums/' + albumId, body: processedData,handle});
    }

}

// 폼을 서버로 전송하는 함수
window.sendTracksUpdateRequest = async function (musicId, formData) {
    const response = serializeFormToJSON(formData);

    const {errors, processedData} = inputHandler(response, formData);

    const handle = {
        onBadRequest: () => {
            alert("입력한 정보에서 오류가 발생했습니다.");
            router.navigate("/me/sounds/tracks");
        },
        onSuccess: () => {
            alert("입력한 정보로 수정했습니다.")
            router.navigate("/me/sounds/tracks");
        },
    }

    if (!errors) {
        await axiosPatch({endpoint: '/api/me/tracks/' + musicId, body: processedData, handle});
    }
};

export async function renderMyTracks(data){
    try{
        const container = document.getElementById("content-body");
        container.innerHTML = '';

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
                                        <button class="apply-button" style="display: none;" onclick="applyTracksChanges(this, ${manage.musicDTO.musicId})">적용</button>
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

export async function renderMyAlbums(data) {
    try {
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
                                <th>아티스트Id</th>
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
                                        <button class="apply-button" style="display: none;" onclick="applyAlbumsChanges(this, ${item.albumDTO.albumId})">적용</button>
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

    } catch (error) {
        console.error('Error occurred while rendering:', error);
    }
}

export async function renderMyTags(data){
    const container = document.getElementById("content-body");
    // 기존 데이터 초기화
    container.innerHTML = '';

    // 데이터 렌더링
    const manageHTML =`
        <div id="tag-modal" class="modal hidden">
            <div class="modal-content">
                <h2>태그 선택</h2>
                <input type="text" id="tag-search" placeholder="태그 검색" />
                <ul id="tag-list">
                    <!-- 태그 리스트가 여기에 동적으로 추가됩니다. -->
                </ul>
                <button type="button" class="close-modal">닫기</button>
            </div>
            <section class="uplaod-section">
                <form id="myForm">
                    <h2>곡에 적절한 태그를 선택해주세요.</h2>
                    <ul id="selected-tags">
                        <!-- 선택된 태그가 여기에 표시됩니다. -->
                    </ul>
                    <button type="button" class="reset-tags">태그 초기화</button>
                    <button type="submit">수정 확정</button>
                </form>
            </section>
        </div>
        <h3>태그 정보 수정</h3>
        <div class="table-wrapper">
            <table class="table-container">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>아티스트</th>
                        <th>타이틀</th>
                        <th>악기 태그</th>
                        <th>무드 태그</th>
                        <th>장르 태그</th>
                        <th>생성일</th>
                        <th>수정일</th>
                        <th>작업</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.dtoList.map(manage => `
                        <tr>
                            <td>${manage.musicDTO.musicId}</td>
                            <td>${manage.albumDTO.nickname}</td>
                            <td>${manage.musicDTO.title}</td>
                            <td>
                                <span class="current-value" data-field="instTags">${manage.tagsStreamDTO.instrumentTagName}</span>
                            </td>
                            <td>
                                <span class="current-value" data-field="moodTags">${manage.tagsStreamDTO.moodTagName}</span>
                            </td>
                            <td>
                                <span class="current-value" data-field="genreTags">${manage.tagsStreamDTO.genreTagName}</span>
                            </td>
                            <td>${formatDate(manage.musicDTO.createDate)}</td>
                            <td>${formatDate(manage.musicDTO.modifyDate)}</td>
                            <td>
                                <button type="button" class="open-modal">태그 찾기</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
    container.innerHTML = manageHTML;

    let allTags = [];
    let currentMusicId = null; // 현재 수정 대상 음악 ID를 저장
    const modal = document.getElementById("tag-modal");
    const tagListElement = document.getElementById("tag-list");
    const selectedTagsElement = document.getElementById("selected-tags");
    const resetTagsButton = document.querySelector(".reset-tags");
    const form = document.getElementById("myForm");
    const tagSearchInput = document.getElementById("tag-search");

    tagSearchInput.addEventListener("input", (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const filteredTags = allTags.filter(({ tag }) =>
            tag.toLowerCase().includes(searchTerm)
        );
        renderTagList(filteredTags);
    });

    // 모달 열기
    document.addEventListener("click", (e) => {
        if (e.target.classList.contains("open-modal")) {
            const row = e.target.closest("tr");
            currentMusicId = row.querySelector("td:first-child").textContent; // 첫 번째 열의 ID 가져오기

            modal.classList.remove("hidden");
            fetchTags();
        }
    });

    // 모달 닫기
    document.querySelector(".close-modal").addEventListener("click", () => {
        modal.classList.add("hidden");
    });

    function selectTag(tag, type) {
        if (!Array.from(selectedTagsElement.children).some(li => li.textContent === tag)) {
            const selectedLi = document.createElement("li");
            selectedLi.textContent = tag;
            selectedTagsElement.appendChild(selectedLi);

            const input = document.createElement("input");
            input.type = "text";
            input.name = `${type}`;
            input.value = tag;
            input.hidden = true;
            form.appendChild(input);
        }
    }

    // 태그 불러오기
    async function fetchTags() {
        try {
            const response = await axios.get('/api/sounds/tags');
            const { dtoList } = response.data;

            allTags = [
                ...dtoList[0].instrument.map(tag => ({ tag, type: 'instrument' })),
                ...dtoList[0].mood.map(tag => ({ tag, type: 'mood' })),
                ...dtoList[0].genre.map(tag => ({ tag, type: 'genre' }))
            ].filter(item => item.tag);

            renderTagList(allTags);

        } catch (error) {
            console.error("Error fetching tags:", error);
        }
    }

    // 태그 리스트 렌더링
    function renderTagList(tags) {
        tagListElement.innerHTML = ""; // 초기화

        tags.forEach(({ tag, type }) => {
            const li = document.createElement("li");
            li.textContent = tag;
            li.addEventListener("click", () => selectTag(tag, type));
            tagListElement.appendChild(li);
        });
    }

    // 태그 초기화
    resetTagsButton.addEventListener("click", () => {
        selectedTagsElement.innerHTML = "";
    });

    // 폼 제출
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const form = document.getElementById("myForm");
        console.log(form);
        // 태그 검증 로직
        const hasInstrumentTag = Array.from(form.elements).some(input => input.name.startsWith("instrument"));
        const hasMoodTag = Array.from(form.elements).some(input => input.name.startsWith("mood"));
        const hasGenreTag = Array.from(form.elements).some(input => input.name.startsWith("genre"));

        if (!hasInstrumentTag || !hasMoodTag || !hasGenreTag) {
            alert("태그를 하나 이상 선택해야 합니다: 악기, 무드, 장르 중 하나를 선택해주세요.");
            return; // 제출 중단
        }
        const jsonData = serializeFormToJSON(form);
        const {errors,processedData} = inputHandler(jsonData, form);

        const handle= {
            onBadRequest: ()=>{
                alert("입력한 정보에서 오류가 발생했습니다.");
                router.navigate("/me/sounds/tags");
            },
            onSuccess:()=>{
                alert("입력한 정보로 수정했습니다.")
                router.navigate("/me/sounds/tags")
            },
        }

        if(!errors){
            await axiosPost({endpoint: `/api/me/tracks/`+currentMusicId+`/tags`, body:processedData,handle });
        }
    });
}

export function renderMyMain(){
    const container = document.getElementById("content-body");
    container.innerHTML='';

    const html = `
    <div
        <div id="question-list" class="question-list">
            <div class="linkDiv">
                <div class="statistics-section">
                    <div class="Statistics">
                        사이트 통계 자료 - 방문자 수, 트래픽양, 전체 곡 수 등 자료
                    </div>
                </div>
                <div class="frequently-list">
                    <li id="upload-sound">
                        <span class="main-text">음원 업로드</span><br>
                        <span class="sub-text">Album, Music, Instrument Tag, Mood Tag, Genre Tag</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                    <li id="edit-album">
                        <span class="main-text">앨범 정보 수정</span><br>
                        <span class="sub-text">Title, Description</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                    <li id="edit-music">
                        <span class="main-text">음원 정보 수정</span><br>
                        <span class="sub-text">Title, Description, soundType</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                    <li id="edit-tags">
                        <span class="main-text">태그 정보 수정</span><br>
                        <span class="sub-text">Instrument Tag, Mood Tag, Genre Tag</span>
                        <img src="/images/chevron_right_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.svg">
                    </li>
                </div>
            </div>
        </div>
    `;

    container.innerHTML = html;

    document.getElementById("upload-sound").addEventListener("click", function () {
        alert("음원 업로드 페이지로 이동합니다.");
        router.navigate(`/me/sounds/upload`);
    });

    document.getElementById("edit-album").addEventListener("click", function () {
        alert("앨범 정보 수정 페이지로 이동합니다.");
        router.navigate('/me/sounds/albums');
    });

    document.getElementById("edit-music").addEventListener("click", function () {
        alert("음원 정보 수정 페이지로 이동합니다.");
        router.navigate(`/me/sounds/tracks`);
    });

    document.getElementById("edit-tags").addEventListener("click", function () {
        alert("태그 정보 수정 페이지로 이동합니다.");
        router.navigate('/me/sounds/tags');
    });
}

export async function renderSoundUpload(){
    const container = document.getElementById('content-body');
    container.innerHTML ='';

    const renderHTML= `
        <h1>음악 업로드 페이지</h1>
        <form id="myImage" class="myImage">
            <section class="upload-section">
                <h2>앨범 이미지를 선택해주세요.</h2>
                <input type="file" id="file-upload" name="file" accept=".jpg,.jpeg,.png">
                <input type="text" id="title" name="title" placeholder="제목 (자동 입력됨)" readonly hidden>
                <button type="submit" class="upload">업로드</button>
            </section>
        </form>
        
        <form id="myTrack" class="myTrack">
            <section class="upload-section">
                <h2>음원 파일을 선택해주세요.</h2>
                <input type="file" id="file-upload" name="file" accept=".mp3,.wav">
                <input type="text" id="title" name="title" placeholder="제목 (자동 입력됨)" readonly hidden>
                <button type="submit" class="upload">업로드</button>
            </section>
        </form>
        
        <form id="myForm" class="myForm">
            <!-- 싱글/앨범 선택 -->
            <section class="upload-section">
                <h2>싱글인지 앨범인지 선택해주세요.</h2>
                <div class="select-div">
                    <select id="group-type" name="groupType">
                        <option value="single">싱글</option>
                        <!--<option value="album">앨범</option>-->
                    </select>
                </div>
            </section>
    
            <!-- 앨범 정보 입력 -->
            <section class="upload-section">
                <h2>앨범의 이름을 정해주세요.</h2>
                <input type="text" id="group-name" name="albumDTO.albumName" placeholder="그룹 이름">
<!--                <h2>앨범 이미지를 업로드해주세요.</h2>-->
                <input type="text" id="group-image" name="albumDTO.albumArtPath" readonly hidden>
                <h2>앨범 설명을 작성해주세요.</h2>
                <textarea id="group-description" name="albumDTO.description" placeholder="앨범 설명"></textarea>
            </section>
    
            <!-- 곡 정보 입력 -->
            <section class="upload-section">
                <h2>곡 제목을 입력해주세요.</h2>
                <input type="text" id="song-title" name="musicDTO.title" placeholder="곡 제목">
<!--                <h2>업로드할 곡 파일을 선택해주세요.</h2>-->
                <input type="text" id="file-upload" name="musicDTO.filePath" readonly hidden>
                <h2>곡 설명을 작성해주세요.</h2>
                <textarea id="song-description" name="musicDTO.description" placeholder="곡 설명"></textarea>
            </section>
    
            <!-- 태그 선택 -->
            <section class="upload-section">
                <h2>곡에 적절한 태그를 선택해주세요.</h2>
                <button type="button" class="open-modal">태그 찾기</button>
    
                <ul id="selected-tags">
                    <!-- 선택된 태그가 여기에 표시됩니다. -->
                </ul>
                <button type="button" class="reset-tags">태그 초기화</button>
            </section>
    
            <!-- Modal 구조 -->
            <div id="tag-modal" class="modal hidden">
                <div class="modal-content">
                    <h2>태그 선택</h2>
                    <input type="text" id="tag-search" placeholder="태그 검색" />
                    <ul id="tag-list">
                        <!-- 태그 리스트가 여기에 동적으로 추가됩니다. -->
                    </ul>
                    <button type="button" class="close-modal">닫기</button>
                </div>
            </div>
    
            <!-- 최종 확인 및 제출 -->
            <section class="upload-section">
                <h2>최종적으로 작성한 정보를 확인해주세요.</h2>
                <div id="song-info-list"></div>
                <button type="submit" class="upload">업로드</button>
            </section>
        </form>
    `;
    container.innerHTML=renderHTML;

    const form = document.getElementById("myForm");
    const imageForm = document.getElementById("myImage");
    const trackForm = document.getElementById("myTrack");
    const tagModal = document.getElementById("tag-modal");
    const openModalBtn = document.querySelector(".open-modal");
    const closeModalBtn = document.querySelector(".close-modal");
    const tagList = document.getElementById("tag-list");
    const tagSearch = document.getElementById("tag-search");
    const selectedTags = document.getElementById("selected-tags");
    const resetTagsBtn = document.querySelector(".reset-tags");

    let uploadImage='';
    let uploadTrack='';

    let allTags = [];

    openModalBtn.addEventListener("click", openTagModal);
    closeModalBtn.addEventListener("click", closeTagModal);
    resetTagsBtn.addEventListener("click", resetTags);

    function selectTag(tag, type) {
        if (!Array.from(selectedTags.children).some(li => li.textContent === tag)) {
            const selectedLi = document.createElement("li");
            selectedLi.textContent = tag;
            selectedTags.appendChild(selectedLi);

            const input = document.createElement("input");
            input.type = "text";
            input.name = `tagsDTO.${type}`;
            input.value = tag;
            input.hidden = true;
            form.appendChild(input);
        }
    }

    async function openTagModal() {
        tagModal.classList.remove("hidden");

        try {
            const response = await axios.get('/api/sounds/tags');
            const { dtoList } = response.data;

            allTags = [
                ...dtoList[0].instrument.map(tag => ({ tag, type: 'instrument' })),
                ...dtoList[0].mood.map(tag => ({ tag, type: 'mood' })),
                ...dtoList[0].genre.map(tag => ({ tag, type: 'genre' }))
            ].filter(item => item.tag);

            renderTagList(allTags);
        } catch (error) {
            console.error("태그 데이터를 가져오는 중 오류 발생:", error);
        }
    }

    function renderTagList(tags) {
        tagList.innerHTML = "";

        tags.forEach(({ tag, type }) => {
            const li = document.createElement("li");
            li.textContent = tag;
            li.addEventListener("click", () => selectTag(tag, type));
            tagList.appendChild(li);
        });
    }

    tagSearch.addEventListener("input", () => {
        const searchQuery = tagSearch.value.toLowerCase();
        const filteredTags = allTags.filter(({ tag }) => tag.toLowerCase().includes(searchQuery));
        renderTagList(filteredTags);
    });

    function closeTagModal() {
        tagModal.classList.add("hidden");
    }

    function resetTags() {
        selectedTags.innerHTML = "";
        const inputs = document.querySelectorAll("input[name^='tagsDTO.']");
        inputs.forEach(input => input.remove());
    }

    imageForm.addEventListener("submit", async function (e) {
        e.preventDefault(); // 기본 동작 막기

        const uploadButton = imageForm.querySelector('button.upload');
        uploadButton.disabled = true;

        // 파일 입력 필드 가져오기
        const fileInput = imageForm.querySelector('input[type="file"]');
        const selectedFiles = fileInput.files; // 선택된 파일 리스트

        // 파일 개수 확인
        if (selectedFiles.length === 0) {
            alert("이미지를 업로드해주세요.");
            return;
        }

        if (selectedFiles.length > 1) {
            alert("이미지는 한 번에 하나만 업로드할 수 있습니다.");
            return;
        }

        const selectedFile = selectedFiles[0]; // 첫 번째 파일 가져오기

        // 파일 크기 제한 (예: 2MB)
        const maxFileSize = 2 * 1024 * 1024; // 2MB
        if (selectedFile.size > maxFileSize) {
            alert("이미지 크기는 2MB를 초과할 수 없습니다.");
            return;
        }

        // 파일 확장자 제한 (예: JPG, PNG만 허용)
        const allowedExtensions = ["jpg", "jpeg", "png"];
        const fileExtension = selectedFile.name.split(".").pop().toLowerCase();
        if (!allowedExtensions.includes(fileExtension)) {
            alert(`허용된 파일 형식은 ${allowedExtensions.join(", ")}입니다.`);
            return;
        }

        // 파일 이름을 제목 필드에 자동으로 설정
        const titleInput = imageForm.querySelector('input[name="title"]');
        titleInput.value = selectedFile.name; // 파일 이름을 제목에 설정

        // 유효성 검증 통과 후 서버 요청
        const formData = new FormData(imageForm); // FormData 객체로 전송

        const handle = {
            onSuccess: (data) => {
                alert('이미지가 성공적으로 업로드되었습니다!');
            },
            onBadRequest: ()=> {
                alert("업로드가 실패했습니다.");
                imageForm.querySelectorAll('input, button').forEach(element => {
                    element.disabled = false;
                });
            },
        };

        uploadImage = await axiosPost({endpoint: "/api/files/albums", body: formData, handle});
    });

    trackForm.addEventListener("submit", async function (e) {
        e.preventDefault(); // 기본 동작 막기

        const uploadButton = trackForm.querySelector('button.upload');
        uploadButton.disabled = true;

        // 파일 입력 필드 가져오기
        const fileInput = trackForm.querySelector('input[type="file"]');
        const selectedFiles = fileInput.files; // 선택된 파일 리스트

        // 파일 개수 확인
        if (selectedFiles.length === 0) {
            alert("파일을 업로드해주세요.");
            return;
        }

        if (selectedFiles.length > 1) {
            alert("파일은 한 번에 하나만 업로드할 수 있습니다.");
            return;
        }

        const selectedFile = selectedFiles[0]; // 첫 번째 파일 가져오기

        // 파일 크기 제한 (예: 5MB)
        const maxFileSize = 20 * 1024 * 1024; // 20MB
        if (selectedFile.size > maxFileSize) {
            alert("파일 크기는 20MB를 초과할 수 없습니다.");
            return;
        }

        // 파일 확장자 제한 (예: MP3, WAV만 허용)
        const allowedExtensions = ["mp3", "wav"];
        const fileExtension = selectedFile.name.split(".").pop().toLowerCase();
        if (!allowedExtensions.includes(fileExtension)) {
            alert(`허용된 파일 형식은 ${allowedExtensions.join(", ")}입니다.`);
            return;
        }

        // 파일 이름을 제목 필드에 자동으로 설정
        const titleInput = trackForm.querySelector('input[name="title"]');
        titleInput.value = selectedFile.name; // 파일 이름을 제목에 설정

        // 유효성 검증 통과 후 서버 요청
        const formData = new FormData(trackForm); // FormData 객체로 전송

        const handle = {
            onSuccess: (data) => {
                alert('음원이 성공적으로 업로드되었습니다!');
                const trackForm = document.getElementById("myTrack");
            },
            onBadRequest: ()=> {
                alert("업로드가 실패했습니다.");
                trackForm.querySelectorAll('input, button').forEach(element => {
                    element.disabled = false;
                });
            },
        };

        uploadTrack = await axiosPost({endpoint: "/api/files/tracks", body: formData, handle});
    });

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        // 태그 검증 로직
        const hasInstrumentTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDTO.instrument"));
        const hasMoodTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDTO.mood"));
        const hasGenreTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDTO.genre"));

        if (!hasInstrumentTag) {
            alert("태그를 하나 이상 선택해야 합니다: 악기를 선택해주세요.");
            return; // 제출 중단
        }

        if (!hasMoodTag) {
            alert("태그를 하나 이상 선택해야 합니다: 무드를 선택해주세요.");
            return; // 제출 중단
        }

        if (!hasGenreTag) {
            alert("태그를 하나 이상 선택해야 합니다: 장르를 선택해주세요.");
            return; // 제출 중단
        }

        form.querySelector('input[name="albumDTO.albumArtPath"]').value=uploadImage;
        form.querySelector('input[name="musicDTO.filePath"]').value=uploadTrack;

        const jsonData = serializeFormToJSON(form);

        const { errors, processedData } = inputHandler(jsonData, form);

        const handle = {
            onSuccess: (data) => {
                alert('음원이 성공적으로 업로드되었습니다!');
                router.navigate("/me/sounds");
            },
            onBadRequest: ()=> {
                alert("업로드가 실패했습니다.");
                return null;
            },
        };

        if(!errors) {
            const response = await axiosPost({endpoint: '/api/me/sounds', body: processedData, handle: handle});
        }
    });
}

