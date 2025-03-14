import {router} from "/js/router.js";
import {serializeFormToJSON} from '/js/serialize/formToJson.js';
import {inputHandler} from '/js/check/inputHandler.js';
import {axiosGet, axiosPatch, axiosPost, axiosDelete} from '/js/fetch/standardAxios.js';
import {formatDate} from "/js/formatDate.js";



// 전역 함수로 enableAdminEditing
window.enableAdminEditing = function(button) {
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

// 전역 함수로 updateAdminUI
window.updateAdminUI = function(row, updatedData, button) {
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

// 전역 함수로 createAdminFormData
window.createAdminFormData = function(row) {
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

    const formData = createAdminFormData(row);  // 폼 데이터 생성

    // 폼을 body에 추가
    container.appendChild(formData);

    // 서버에 데이터 전송
    await sendAdminAlbumsUpdateRequest(albumId, formData);
}

window.applyAdminAlbumsDelete = async function(button, albumId){
    const handle = {
        success: {
            navigate: "/sounds/tracks"
        },
    };

    await axiosDelete({endpoint: '/api/admin/albums/' + albumId, handle});
}

window.applyAdminTrackDelete = async function(button, musicId){
    alert(musicId);

//    const handle ={
//        onSuccess:() =>{
//            alert("요청한 트랙을 삭제하였습니다.");
//            router.navigate("/admin/tracks");
//        },
//        onBadRequest:()=>{
//            alert("트랙을 삭제하지못했습니다.");
//            router.navigate("/admin/tracks");
//        }
//    }
    const handle ={
        success:{
            navigate:"/admin/tracks"
        },
    }

    await axiosDelete({ endpoint:'/api/admin/tracks/'+musicId, handle });
}

window.applyAdminTracksChanges = async function(button, albumId) {
    const row = button.closest('tr');
    const container = document.getElementById("render-update");
    container.innerHTML = '';  // 기존 폼 비우기

    const formData = createAdminFormData(row);  // 폼 데이터 생성

    // 폼을 body에 추가
    container.appendChild(formData);

    // 서버에 데이터 전송
    await sendAdminTracksUpdateRequest(albumId, formData);
}


// 헬퍼 함수: 정렬 방향에 따른 아이콘 반환
function getSortIcon(sortOrder) {
  if (sortOrder === 'asc') {
    return '\u25B2'; // ▲
  } else if (sortOrder === 'desc') {
    return '\u25BC'; // ▼
  }
  return '';
}

// 전역 함수: 테이블 헤더 클릭 시 정렬 파라미터 추가, 아이콘 업데이트 및 라우터 네비게이션 처리
function registerTableHeaderSort() {
  const headerCells = document.querySelectorAll(".table-container thead th");
  const url = new URL(window.location.href);

  headerCells.forEach(th => {
    let sortKey = "";
    const headerText = th.textContent.trim();

    // 클릭 가능한 열 (정렬 대상) 지정: "ID", "아티스트", "업로드일"
    if (headerText === "ID") {
      sortKey = "albumId";
    } else if (headerText === "아티스트") {
      sortKey = "userId";
    } else if (headerText === "업로드일") {
      sortKey = "createDate";
    } else {
      return; // 정렬이 필요 없는 열은 건너뜁니다.
    }

    // 원래의 헤더 텍스트를 저장 (이미 저장되어 있지 않다면)
    if (!th.dataset.originalText) {
      th.dataset.originalText = headerText;
    }

    // 클릭 가능하다는 시각적 힌트
    th.style.cursor = "pointer";

    // URL에서 해당 정렬 파라미터가 있는지 확인하여 초기 정렬 상태 설정
    const sortOrder = url.searchParams.get(`more[${sortKey}]`);
    if (sortOrder) {
      th.setAttribute("data-direction", sortOrder);
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(sortOrder)}`;
    }

    // 클릭 이벤트 리스너 등록
    th.addEventListener("click", function() {
      // 현재 정렬 방향 가져오기, 없으면 기본 asc
      let currentDirection = th.getAttribute("data-direction") || "asc";
      // 토글: asc이면 desc, 아니면 asc
      currentDirection = currentDirection === "asc" ? "desc" : "asc";
      th.setAttribute("data-direction", currentDirection);
      // 헤더 텍스트에 아이콘 업데이트
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(currentDirection)}`;

      // 현재 URL 기반으로 새 URL 생성 및 파라미터 업데이트
      const newUrl = new URL(window.location.href);
      newUrl.searchParams.set(`more[${sortKey}]`, currentDirection);
      // router.navigate(newUrl)로 페이지 이동 (router 객체는 전역에 정의되어 있어야 합니다)
      router.navigate(newUrl.toString());
    });
  });
}

// 음원 헬퍼 함수: 정렬 방향에 따른 아이콘 반환
function getSortIconForTrack(sortOrder) {
  if (sortOrder === 'asc') {
    return '\u25B2'; // ▲
  } else if (sortOrder === 'desc') {
    return '\u25BC'; // ▼
  }
  return '';
}

// 음원 전역 함수: 테이블 헤더 클릭 시 정렬 파라미터 추가, 아이콘 업데이트 및 라우터 네비게이션 처리
function registerTableHeaderSortForTrack() {
  const headerCells = document.querySelectorAll(".table-container thead th");
  const url = new URL(window.location.href);

  headerCells.forEach(th => {
    let sortKey = "";
    const headerText = th.textContent.trim();

    // 클릭 가능한 열 (정렬 대상) 지정: "ID", "아티스트", "업로드일"
    if (headerText === "ID") {
      sortKey = "musicId";
    } else if (headerText === "업로드일") {
      sortKey = "createDate";
    } else {
      return; // 정렬이 필요 없는 열은 건너뜁니다.
    }

    // 원래의 헤더 텍스트를 저장 (이미 저장되어 있지 않다면)
    if (!th.dataset.originalText) {
      th.dataset.originalText = headerText;
    }

    // 클릭 가능하다는 시각적 힌트
    th.style.cursor = "pointer";

    // URL에서 해당 정렬 파라미터가 있는지 확인하여 초기 정렬 상태 설정
    const sortOrder = url.searchParams.get(`more[${sortKey}]`);
    if (sortOrder) {
      th.setAttribute("data-direction", sortOrder);
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(sortOrder)}`;
    }

    // 클릭 이벤트 리스너 등록
    th.addEventListener("click", function() {
      // 현재 정렬 방향 가져오기, 없으면 기본 asc
      let currentDirection = th.getAttribute("data-direction") || "asc";
      // 토글: asc이면 desc, 아니면 asc
      currentDirection = currentDirection === "asc" ? "desc" : "asc";
      th.setAttribute("data-direction", currentDirection);
      // 헤더 텍스트에 아이콘 업데이트
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(currentDirection)}`;

      // 현재 URL 기반으로 새 URL 생성 및 파라미터 업데이트
      const newUrl = new URL(window.location.href);
      newUrl.searchParams.set(`more[${sortKey}]`, currentDirection);
      // router.navigate(newUrl)로 페이지 이동 (router 객체는 전역에 정의되어 있어야 합니다)
      router.navigate(newUrl.toString());
    });
  });
}


export function renderAdminSoundsSort() {
  const container = document.getElementById('chart-selector-container');
  if (!container) return;

  // 고유 식별자 생성 (timestamp와 랜덤 숫자 조합)
  const uniqueSuffix = Date.now() + Math.floor(Math.random() * 1000);
  const sortKeywordId = `sortKeyword_${uniqueSuffix}`;
  const musicSortMenuId = `musicSortMenu_${uniqueSuffix}`;

  const item = document.createElement('div');
  item.classList.add('music-sort'); // 기존 스타일 재사용

  item.innerHTML = `
    <div class="sort-01">
      <span class="music-sort-left" id="${sortKeywordId}">
        <img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정보 전환">정보 전환
      </span>
      <!-- 드롭다운 메뉴 -->
      <div class="music-sort-menu" id="${musicSortMenuId}">
        <ul>
          <li data-category="album">앨범 정보</li>
          <li data-category="music">음원 정보</li>
          <li data-category="tag">태그 정보</li>
          <li data-category="verify">미등록 앨범 정보</li>
        </ul>
      </div>
    </div>
  `;

  container.appendChild(item);
  setupAdminSoundsDropdownEvents(sortKeywordId, musicSortMenuId);
}

// 드롭다운 메뉴 이벤트 설정 함수 (고유 ID를 파라미터로 받음)
function setupAdminSoundsDropdownEvents(sortKeywordId, musicSortMenuId) {
  const sortKeyword = document.getElementById(sortKeywordId);
  const menu = document.getElementById(musicSortMenuId);

  // 정렬 아이콘 또는 텍스트 클릭 시 드롭다운 토글
  sortKeyword.addEventListener('click', () => {
    menu.classList.toggle('visible');
  });

  // 메뉴 아이템 클릭 시 이벤트 처리
  menu.querySelectorAll('li').forEach(li => {
    li.addEventListener('click', async () => {
      // 드롭다운 닫기
      menu.classList.remove('visible');

      // 모든 항목에서 active 클래스 제거 후, 현재 항목에 추가
      menu.querySelectorAll('li').forEach(item => item.classList.remove('active'));
      li.classList.add('active');

      // data-category 값에 따라 라우터 이동 처리
      const category = li.getAttribute('data-category');
      switch (category) {
        case 'album':
          router.navigate('/admin/albums');
          break;
        case 'music':
          router.navigate('/admin/tracks');
          break;
        case 'tag':
          router.navigate('/admin/tags/spelling');
          break;
        case 'verify':
          router.navigate('/admin/albums/verify');
          break;
        default:
          break;
      }
    });
  });
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
    console.log(response);
    const { errors, processedData } = inputHandler(response,formData);

//    const handle= {
//        onBadRequest: ()=>{
//            alert("입력한 정보에서 오류가 발생했습니다.");
//            router.navigate("/admin/albums");
//        },
//        onSuccess:()=>{
//            alert("입력한 정보로 수정했습니다.")
//            router.navigate("/admin/albums");
//        },
//    }
    // 서버 응답 핸들링 객체
    const handle = {
        success:{
            navigate:"/admin/albums"
        },
    };

    if (!errors) {
        await axiosPatch({endpoint: '/api/admin/albums/' + albumId, body: processedData,handle});
    }

}

// 폼을 서버로 전송하는 함수
window.sendAdminTracksUpdateRequest = async function(musicId, formData) {
    const response = serializeFormToJSON(formData);

    const { errors, processedData } = inputHandler(response,formData);

//    const handle= {
//        onBadRequest: ()=>{
//            alert("입력한 정보에서 오류가 발생했습니다.");
//            router.navigate("/admin/tracks");
//        },
//        onSuccess:()=>{
//            alert("입력한 정보로 수정했습니다.")
//            router.navigate("/admin/tracks");
//        },
//    }
    // 서버 응답 핸들링 객체
    const handle = {
        success:{
            navigate:"/admin/albums"
        },
    };

    if (!errors) {
        await axiosPatch({ endpoint: '/api/admin/tracks/' + musicId, body: processedData,handle });
    }
}

// 1. 화면에서 앨범보고 (전환 가능 메인)
export async function renderArtistsAlbums(data) {
    const container = document.getElementById("content-body");
    container.innerHTML = '';

    if (data.dtoList && data.dtoList.length > 0) {
        const manageHTML = `
            <h3>앨범 정보 수정</h3>
            <div id="render-update" class="render-update"></div>

            <!-- 드롭다운 메뉴 영역 -->
            <div id="chart-selector-container"></div>

            <table class="table-container">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>회원 ID</th>
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
                        <tr data-album-id="${item.albumDTO.albumId}" data-user-id="${item.albumDTO.userId}">
                            <td>${item.albumDTO.albumId}</td>
                            <td>${item.albumDTO.userId}</td>
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
                                <button class="edit-button" onclick="enableAdminEditing(this)">수정하기</button>
                                <button class="apply-button" style="display: none;" onclick="applyAdminAlbumsChanges(this, ${item.albumDTO.albumId})">적용</button>
                                <button class="delete-button" style="display: none;" onclick="applyAdminAlbumsDelete(this, ${item.albumDTO.albumId})">삭제</button>
                                <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;

        container.innerHTML = manageHTML;

        // 드롭다운 메뉴 렌더링 호출
        renderAdminSoundsSort();

        // 테이블 바디 클릭 시 앨범 모달 열기 (버튼/인풋 제외)
        const tbody = container.querySelector("tbody");
        tbody.addEventListener("click", function(event) {
            const targetTag = event.target.tagName.toLowerCase();
            if (targetTag === "button" || targetTag === "input") return;
            const tr = event.target.closest("tr");
            if (tr && tr.dataset.albumId && tr.dataset.userId) {
                openAlbumModal(tr.dataset.userId,tr.dataset.albumId);
            }
        });

        // 테이블 헤더 클릭 이벤트 등록
        registerTableHeaderSort();
    } else {
        container.innerHTML = '<p>앨범이 없습니다.</p>';
    }
}

// 1-1. 레코드 클릭해서 모달 창 열어 보고
async function openAlbumModal(userId,albumId) {
    const response = await axiosGet({ endpoint:`/api/admin/tracks/${userId}/albums/${albumId}` });

    // 모달 제목 설정 (앨범의 음원 정보 수정)
    const modalTitle = document.querySelector('#soundDetailModal h2');
    modalTitle.innerText = `앨범 ${albumId}의 음원 정보 수정`;

    // 모달 본문에 트랙 관리 테이블 렌더링
    const modalBody = document.getElementById('sound-detail-modal-body');
    if (response.dtoList && response.dtoList.length > 0) {
        modalBody.innerHTML = `
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
                    <tbody>
                        ${response.dtoList.map(manage => `
                            <tr>
                                <td>${manage.musicDTO.musicId}</td>
                                <td>${manage.musicDTO.nickname}</td>
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
                                    <button class="edit-button" onclick="enableAdminEditing(this)">수정하기</button>
                                    <button class="apply-button" style="display: none;" onclick="applyAdminTracksChanges(this, ${manage.musicDTO.musicId})">적용</button>
                                    <button class="delete-button" style="display: none;" onclick="applyAdminTrackDelete(this, ${manage.musicDTO.musicId})">삭제</button>
                                    <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
    } else {
        modalBody.innerHTML = '<p>음원이 없습니다.</p>';
    }

    // 모달 창 보이기
    document.getElementById('soundDetailModal').style.display = 'block';
}

// 2. 태그 화면으로 전환하고 (전환 가능 메인)
export async function renderTagsSpelling(data) {
  const container = document.getElementById("content-body");
  container.innerHTML = '';

  // 신규 태그 등록 버튼 영역 추가
  const newTagRegistrationButton = `
    <div class="new-tag-registration" style="margin: 1em 0;">
      <button id="new-tag-btn" class="new-tag-btn">신규 태그 등록</button>
    </div>
  `;

  const dto = data.dtoList[0]; // 배열의 첫 번째 객체 선택
  const categories = [
    { category: 'instrument', tags: dto.instrument },
    { category: 'mood', tags: dto.mood },
    { category: 'genre', tags: dto.genre }
  ];

  if (categories && categories.length > 0) {
    const tagsHTML = `
      ${newTagRegistrationButton}
      <h3>태그 정보 수정</h3>
      <div id="chart-selector-container"></div>
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
            ${categories.map(category => `
              ${category.tags.map(tag => `
                <tr>
                  <td>${tag}</td>
                  <td>${category.category}</td>
                  <td>
                    <span class="current-value" data-field="tagName">${tag}</span>
                    <input type="text" class="editable-field" data-field="tagName" value="${tag}" style="display: none;">
                  </td>
                  <td>
                    <button class="edit-button" onclick="enableAdminEditing(this)">수정하기</button>
                    <button class="apply-button" style="display: none;" onclick="applyAdminTagSpellingChanges(this, '${tag}', '${category.category}')">적용</button>
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

    // 신규 태그 등록 버튼 이벤트 등록: 클릭 시 신규 태그 등록 모달 호출
    document.getElementById('new-tag-btn').addEventListener('click', function() {
      renderTagsNew(); // 모달에 신규 태그 등록 화면 렌더링
    });

  } else {
    container.innerHTML = '<p>태그가 없습니다.</p>';
  }

  // 드롭다운 메뉴 렌더링 호출
  renderAdminSoundsSort();
}

// 2-1. 태그 추가하고프면 추가하고
export async function renderTagsNew(data) {
    // 기존의 모달 창(예: soundDetailModal)의 제목 및 본문 영역 재활용
    const modalTitle = document.querySelector('#soundDetailModal h2');
    modalTitle.innerText = '태그 추가';

    const modalBody = document.getElementById('sound-detail-modal-body');
    modalBody.innerHTML = `
        <form id="tag-form">
            <section class="tag-add-section">
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

    // 모달 창 보이기
    document.getElementById('soundDetailModal').style.display = 'block';

    // 태그 추가 폼의 제출 이벤트 처리
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

//        const handle = {
//            onSuccess: () => {
//                alert("태그를 정상적으로 생성했습니다.");
//            },
//            onBadRequest: () => {
//                alert("태그를 정상적으로 생성하지 못했습니다.");
//            },
//        };

        const handle={
            success:{
                navigate:"/admin/tags"
            },
        }

        const jsonData = serializeFormToJSON(event.target);
        const { errors, processedData } = inputHandler(jsonData, event.target);

        if (!errors) {
            if (tagsDto.hasOwnProperty(tagType)) {
                tagsDto[tagType].push(processedData.tagName);
            } else {
                alert(`잘못된 태그 타입: ${tagType}`);
            }
            await axiosPost({ endpoint: `/api/admin/tags`, body: tagsDto, handle});

            document.getElementById('soundDetailModal').style.display = 'none';
        }
    });

}

// 3. 미등록(대기)앨범 보고프면 보고
export async function renderArtistsVerify(data){
    // API 호출 및 렌더링 처리
    const container = document.getElementById("content-body");
    container.innerHTML = '';

    // 앨범 정보가 있으면 테이블 렌더링
    if (data.dtoList && data.dtoList.length > 0) {
        const manageHTML = `
            <h3>미등록 앨범 정보 수정</h3>
            <div id="render-update" class="render-update"></div>
            <div id="chart-selector-container"></div>
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
                                    <span data-field="albumName">${item.albumDTO.albumName}</span>
                                </td>
                                <td>
                                    <span data-field="description">${item.albumDTO.description}</span>
                                </td>
                                <td>${formatDate(item.albumDTO.createDate)}</td>
                                <td>
                                    <button class="edit-button" onclick="enableAdminEditing(this)">수정하기</button>
                                    <button class="apply-button" style="display: none;" onclick="applyAdminAlbumsVerify(this, ${item.albumDTO.albumId})">앨범통과</button>
                                    <button class="watch-button" style="display: none" onclick="viewAdminVerify(this, ${item.albumDTO.albumId}, '${item.albumDTO.userId}')">자세히 보기</button>
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

    // 드롭다운 메뉴 렌더링 호출
    renderAdminSoundsSort();
}

// 3-1. 미등록 앨범의 자세한 내용 보고프면 보고
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

// 3-1'. 미등록 앨범의 자세한 내용 중 하나인 트랙도 같이보고
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

// 4. 앨범 말고 track만 모아보고 싶다면?
export async function renderArtistsTracks(data){
    try{
        const container = document.getElementById("content-body");
        container.innerHTML = '';

        // 앨범 정보가 있으면 테이블 렌더링
        if (data.dtoList && data.dtoList.length > 0) {
            // 데이터 렌더링
            const manageHTML = `
                <h3>음원 정보 수정</h3>
                <div id="render-update" class="render-update"></div>
                <div id="chart-selector-container"></div>
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
                                        <button class="edit-button" onclick="enableAdminEditing(this)">수정하기</button>
                                        <button class="apply-button" style="display: none;" onclick="applyAdminTracksChanges(this, ${manage.musicDTO.musicId})">적용</button>
                                        <button class="delete-button" style="display: none;" onclick="applyAdminTrackDelete(this, ${manage.musicDTO.musicId})">삭제</button>
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

        // 헤더의 각 전환 버튼에 이벤트 리스너 등록
        registerTableHeaderSortForTrack();

        // 드롭다운 메뉴 렌더링 호출
        renderAdminSoundsSort();
    }catch (error) {
        console.error('Error occurred while rendering:', error);
    }
}

window.viewAdminVerify = function(button,id, uid){
    console.log("id : "+ id);
    console.log("name : "+uid);

    router.navigate(`/admin/albums/one/verify?id=${id}&uid=${uid}`);
}

window.applyAdminTagSpellingChanges = async function (button, originalTag, category) {
    // 버튼의 부모 tr 요소를 찾습니다.
    const row = button.closest('tr');

    // 해당 tr 안에 있는 input 필드를 찾아서 값을 가져옵니다.
    const inputField = row.querySelector('.editable-field');

    const newTag = inputField.value;
    const afterNewTag = newTag.replace(/"/g, '');
//    const handle ={
//        onSuccess:() =>{
//            alert("요청한 태그 철자 변경을 수행했습니다.");
//            router.navigate("/admin/tags/spelling");
//        },
//        onBadRequest:()=>{
//            alert("요청한 태그 변경을 수행하지 못했습니다.");
//            router.navigate("/admin/tags/spelling");
//        }
//    }
    // 서버 응답 핸들링 객체
    const handle = {
        success:{
            navigate:"/admin/tags/spelling"
        },
    };

    const pattern = new RegExp("^[a-z0-9.,()-_\\s]+$");
    if (pattern.test(afterNewTag)) {
        if (category === 'instrument') {
            await axiosPatch({endpoint: `/api/admin/tags/instruments/${originalTag}`,body:{'instrument':[afterNewTag]} ,handle})
        }
        if (category === 'mood') {
            await axiosPatch({endpoint: `/api/admin/tags/moods/${originalTag}`,body:{'mood':[afterNewTag]} ,handle})
        }
        if (category === 'genre') {
            await axiosPatch({endpoint: `/api/admin/tags/genres/${originalTag}`,body:{'genre':[afterNewTag]},handle})
        }
    }else {
        alert('태그 형식이 잘못 되었습니다');
    }
}

window.applyAdminAlbumsVerify = async function(button, albumId){
//    const handle ={
//        onSuccess:()=>{
//          alert("대기중인 앨범을 성공적으로 확인했습니다.");
//          router.navigate('/admin/albums/verify')
//        },
//    }
    // 서버 응답 핸들링 객체
    const handle = {
        success:{
            navigate:"/admin/albums/verify"
        },
    };

    await axiosPatch({ endpoint:`/api/admin/albums/${albumId}/verify`, handle});
}

/**
 * 모달 외부 클릭 시 닫힘 처리
 */
window.addEventListener('click', (event) => {
	const modal = document.getElementById('soundDetailModal');
	if (event.target === modal) {
		modal.style.display = 'none';
	}
});


window.addEventListener('click', (event) => {
    const modal = document.getElementById('soundDetailModal');
    if (event.target === modal || event.target.classList.contains('close')) {
        modal.style.display = 'none';
    }
});