import { router } from "/js/router.js";
import { serializeFormToJSON } from "/js/serialize/formToJson.js";
import { inputHandler } from "/js/check/inputHandler.js";
import { axiosGet, axiosPatch, axiosPost, axiosDelete } from "/js/fetch/standardAxios.js";
import { formatDate } from "/js/formatDate.js";

// -------------------------------------------------------------------
// 1. 편집/업데이트/삭제 관련 전역 함수 (me 버전)
// -------------------------------------------------------------------

window.enableMeEditing = function(button) {
  const row = button.closest('tr');

  // 기존 값 숨기기
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

  // 삭제 및 보기 버튼 (존재하면) 보이기
  const deleteButton = row.querySelector('.delete-button');
  if (deleteButton) deleteButton.style.display = 'inline-block';

  const watchButton = row.querySelector('.watch-button');
  if (watchButton) watchButton.style.display = 'inline-block';

  row.querySelector('.cancel-button').style.display = 'inline-block';
};

window.updateMeUI = function(row, updatedData, button) {
  row.querySelectorAll('.current-value').forEach(span => {
    const field = span.dataset.field;
    span.textContent = updatedData[field];
    span.style.display = 'inline-block';
  });

  row.querySelectorAll('.editable-field').forEach(input => {
    input.style.display = 'none';
  });

  row.querySelector('.edit-button').style.display = 'inline-block';
  button.style.display = 'none';
  row.querySelector('.cancel-button').style.display = 'none';
};

window.createMeFormData = function(row) {
  const form = document.createElement('form');
  form.id = 'myForm';

  row.querySelectorAll('.editable-field').forEach(input => {
    const name = input.dataset.field;
    const value = input.value;
    const hiddenInput = document.createElement('input');
    hiddenInput.type = 'hidden';
    hiddenInput.name = name;
    hiddenInput.value = value;
    form.appendChild(hiddenInput);
  });

  return form;
};

window.applyMeAlbumsChanges = async function(button, albumId) {
  const row = button.closest('tr');
  const container = document.getElementById("render-update");
  container.innerHTML = ''; // 기존 폼 비우기
  const formData = createMeFormData(row);
  container.appendChild(formData);
  await sendMeAlbumsUpdateRequest(albumId, formData);
};

window.applyMeTracksChanges = async function(button, musicId) {
  const row = button.closest('tr');
  const container = document.getElementById("render-update");
  container.innerHTML = ''; // 기존 폼 비우기
  const formData = createMeFormData(row);
  container.appendChild(formData);
  await sendMeTracksUpdateRequest(musicId, formData);
};

window.cancelChanges = function(button) {
  const row = button.closest('tr');
  row.querySelectorAll('.editable-field').forEach(input => {
    const field = input.dataset.field;
    const currentValue = row.querySelector(`.current-value[data-field="${field}"]`).textContent;
    input.value = currentValue;
    input.style.display = 'none';
  });
  row.querySelectorAll('.current-value').forEach(span => {
    span.style.display = 'inline-block';
  });
  row.querySelector('.edit-button').style.display = 'inline-block';
  button.style.display = 'none';
  row.querySelector('.apply-button').style.display = 'none';
  const deleteButton = row.querySelector('.delete-button');
  if (deleteButton) deleteButton.style.display = 'none';
  const watchButton = row.querySelector('.watch-button');
  if (watchButton) watchButton.style.display = 'none';
};

// -------------------------------------------------------------------
// 2. API 요청 함수 (me 버전)
// -------------------------------------------------------------------

window.sendMeAlbumsUpdateRequest = async function(albumId, formData) {
  const response = serializeFormToJSON(formData);
  console.log(response);
  const { errors, processedData } = inputHandler(response, formData);
//  const handle = {
//    onBadRequest: () => {
//      alert("입력한 정보에 오류가 있습니다.");
//      router.navigate("/me/sounds/albums");
//    },
//    onSuccess: () => {
//      alert("앨범 정보를 수정했습니다.");
//      router.navigate("/me/sounds/albums");
//    }
//  };
  const handle = {
      success:{
         navigate:"/me/sounds/albums"
      },
  };

  if (!errors) {
    await axiosPatch({ endpoint: '/api/me/albums/' + albumId, body: processedData, handle });
  }
};

window.sendMeTracksUpdateRequest = async function(musicId, formData) {
  const response = serializeFormToJSON(formData);
  const { errors, processedData } = inputHandler(response, formData);
  const handle = {
        success:{
           navigate:"/me/sounds/tracks"
        },
    };
  if (!errors) {
    await axiosPatch({ endpoint: '/api/me/tracks/' + musicId, body: processedData, handle });
  }
};

// -------------------------------------------------------------------
// 3. 정렬 및 드롭다운 메뉴 관련 헬퍼 함수 (me 버전)
// -------------------------------------------------------------------

function getSortIcon(sortOrder) {
  return sortOrder === 'asc' ? '\u25B2' : sortOrder === 'desc' ? '\u25BC' : '';
}

function registerTableHeaderSort() {
  const headerCells = document.querySelectorAll(".table-container thead th");
  const url = new URL(window.location.href);
  headerCells.forEach(th => {
    let sortKey = "";
    const headerText = th.textContent.trim();
    if (headerText === "ID") {
      sortKey = "albumId";
    } else if (headerText === "아티스트") {
      sortKey = "userId";
    } else if (headerText === "업로드일") {
      sortKey = "createDate";
    } else {
      return;
    }
    if (!th.dataset.originalText) {
      th.dataset.originalText = headerText;
    }
    th.style.cursor = "pointer";
    const sortOrder = url.searchParams.get(`more[${sortKey}]`);
    if (sortOrder) {
      th.setAttribute("data-direction", sortOrder);
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(sortOrder)}`;
    }
    th.addEventListener("click", function() {
      let currentDirection = th.getAttribute("data-direction") || "asc";
      currentDirection = currentDirection === "asc" ? "desc" : "asc";
      th.setAttribute("data-direction", currentDirection);
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(currentDirection)}`;
      const newUrl = new URL(window.location.href);
      newUrl.searchParams.set(`more[${sortKey}]`, currentDirection);
      router.navigate(newUrl.toString());
    });
  });
}

function registerTableHeaderSortForTrack() {
  const headerCells = document.querySelectorAll(".table-container thead th");
  const url = new URL(window.location.href);
  headerCells.forEach(th => {
    let sortKey = "";
    const headerText = th.textContent.trim();
    if (headerText === "ID") {
      sortKey = "musicId";
    } else if (headerText === "업로드일") {
      sortKey = "createDate";
    } else {
      return;
    }
    if (!th.dataset.originalText) {
      th.dataset.originalText = headerText;
    }
    th.style.cursor = "pointer";
    const sortOrder = url.searchParams.get(`more[${sortKey}]`);
    if (sortOrder) {
      th.setAttribute("data-direction", sortOrder);
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(sortOrder)}`;
    }
    th.addEventListener("click", function() {
      let currentDirection = th.getAttribute("data-direction") || "asc";
      currentDirection = currentDirection === "asc" ? "desc" : "asc";
      th.setAttribute("data-direction", currentDirection);
      th.innerHTML = `${th.dataset.originalText} ${getSortIcon(currentDirection)}`;
      const newUrl = new URL(window.location.href);
      newUrl.searchParams.set(`more[${sortKey}]`, currentDirection);
      router.navigate(newUrl.toString());
    });
  });
}

export function renderMeSoundsSort() {
  const container = document.getElementById('chart-selector-container');
  if (!container) return;
  const uniqueSuffix = Date.now() + Math.floor(Math.random() * 1000);
  const sortKeywordId = `sortKeyword_${uniqueSuffix}`;
  const musicSortMenuId = `musicSortMenu_${uniqueSuffix}`;
  const item = document.createElement('div');
  item.classList.add('music-sort');
  item.innerHTML = `
    <div class="sort-01">
      <span class="music-sort-left" id="${sortKeywordId}">
        <img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정보 전환">정보 전환
      </span>
      <div class="music-sort-menu" id="${musicSortMenuId}">
        <ul>
          <li data-category="album">앨범 정보</li>
          <li data-category="music">음원 정보</li>
          <li data-category="tags">태그 정보</li>
        </ul>
      </div>
    </div>
  `;
  container.appendChild(item);
  setupMeSoundsDropdownEvents(sortKeywordId, musicSortMenuId);
}

function setupMeSoundsDropdownEvents(sortKeywordId, musicSortMenuId) {
  const sortKeyword = document.getElementById(sortKeywordId);
  const menu = document.getElementById(musicSortMenuId);
  sortKeyword.addEventListener('click', () => {
    menu.classList.toggle('visible');
  });
  menu.querySelectorAll('li').forEach(li => {
    li.addEventListener('click', () => {
      menu.classList.remove('visible');
      menu.querySelectorAll('li').forEach(item => item.classList.remove('active'));
      li.classList.add('active');
      const category = li.getAttribute('data-category');
      switch (category) {
        case 'album':
          router.navigate('/me/sounds/albums');
          break;
        case 'music':
          router.navigate('/me/sounds/tracks');
          break;
        case 'tags':
          router.navigate('/me/sounds/tags');
          break;
        default:
          break;
      }
    });
  });
}

// -------------------------------------------------------------------
// 4. 화면 렌더링 함수 (me 버전)
// -------------------------------------------------------------------

// [앨범 뷰] - 앨범 목록 렌더링 (레코드 클릭 시 모달로 음원 목록 보기)
export async function renderMeAlbums(data) {
  const container = document.getElementById("content-body");
  container.innerHTML = '';
  if (data.dtoList && data.dtoList.length > 0) {
    const manageHTML = `
      <h3>앨범 정보 수정</h3>
      <div id="render-update" class="render-update"></div>
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
                <button class="edit-button" onclick="enableMeEditing(this)">수정하기</button>
                <button class="apply-button" style="display: none;" onclick="applyMeAlbumsChanges(this, ${item.albumDTO.albumId})">적용</button>
                <button class="cancel-button" style="display: none;" onclick="cancelChanges(this)">취소</button>
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>
    `;
    container.innerHTML = manageHTML;
    renderMeSoundsSort();
    const tbody = container.querySelector("tbody");
    tbody.addEventListener("click", function(event) {
      const targetTag = event.target.tagName.toLowerCase();
      if (targetTag === "button" || targetTag === "input") return;
      const tr = event.target.closest("tr");
      if (tr && tr.dataset.albumId && tr.dataset.userId) {
        openAlbumModal(tr.dataset.userId, tr.dataset.albumId);
      }
    });
    registerTableHeaderSort();
  } else {
    container.innerHTML = '<p>앨범이 없습니다.</p>';
  }
}

async function openAlbumModal(userId, albumId) {
  const response = await axiosGet({ endpoint: `/api/me/albums/${albumId}/tracks` });
  const modalTitle = document.querySelector('#soundDetailModal h2');
  modalTitle.innerText = `앨범 ${albumId}의 음원 정보 수정`;
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
              <tr data-track-id="${manage.musicDTO.musicId}">
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
                  <button class="edit-button" onclick="enableMeEditing(this)">수정하기</button>
                  <button class="apply-button" style="display: none;" onclick="applyMeTracksChanges(this, ${manage.musicDTO.musicId})">적용</button>
                  <button class="delete-button" style="display: none;" onclick="applyMeTrackDelete(this, ${manage.musicDTO.musicId})">삭제</button>
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
  document.getElementById('soundDetailModal').style.display = 'block';
}

// [트랙 뷰] - 음원(트랙) 목록 렌더링 (태그 수정 대상)
export async function renderMeTags(data) {
  const container = document.getElementById("content-body");
  container.innerHTML = '';
  if (data.dtoList && data.dtoList.length > 0) {
    const manageHTML = `
      <h3>음원 태그 수정</h3>
      <div id="render-update" class="render-update"></div>
      <div id="chart-selector-container"></div>
      <div class="table-wrapper">
        <table class="table-container">
          <thead>
            <tr>
              <th>ID</th>
              <th>아티스트</th>
              <th>음원 제목</th>
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
              <tr data-track-id="${manage.musicDTO.musicId}">
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
                  <button type="button" class="open-tag-modal">태그 찾기</button>
                </td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>
    `;
    container.innerHTML = manageHTML;
    registerTableHeaderSortForTrack();
    renderMeSoundsSort();
    // "태그 찾기" 버튼 이벤트 바인딩
    document.querySelectorAll(".open-tag-modal").forEach(button => {
      button.addEventListener("click", () => {
        const row = button.closest("tr");
        const currentTrackId = row.dataset.trackId;
        openTagModal(currentTrackId);
      });
    });
  } else {
    container.innerHTML = '<p>트랙이 없습니다.</p>';
  }
}

// 트랙 목록 렌더링 (앨범 없이 전체 트랙 보기)
export async function renderMeTracks(data) {
  try {
    const container = document.getElementById("content-body");
    container.innerHTML = '';

    if (data.dtoList && data.dtoList.length > 0) {
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
            <tbody>
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
                    <button class="edit-button" onclick="enableMeEditing(this)">수정하기</button>
                    <button class="apply-button" style="display: none;" onclick="applyMeTracksChanges(this, ${manage.musicDTO.musicId})">적용</button>
                    <button class="delete-button" style="display: none;" onclick="applyMeTrackDelete(this, ${manage.musicDTO.musicId})">삭제</button>
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
      container.innerHTML = '<p>트랙이 없습니다.</p>';
    }

    registerTableHeaderSortForTrack();
    renderMeSoundsSort();
  } catch (error) {
    console.error('Error occurred while rendering:', error);
  }
}

// -------------------------------------------------------------------
// 5. 태그 편집 모달 (재사용 가능한 단일 모달 구현)
// -------------------------------------------------------------------
function openTagModal(trackId) {
  // 단일 태그 편집 모달 생성 (없으면 생성)
  let tagModal = document.getElementById("tag-modal");
  if (!tagModal) {
    tagModal = document.createElement("div");
    tagModal.id = "tag-modal";
    tagModal.classList.add("modal", "hidden");
    tagModal.innerHTML = `
      <div class="modal-content">
        <h2>태그 선택</h2>
        <!-- 탭 영역 -->
        <div class="tag-tabs">
          <button type="button" class="tab-button active" data-type="instrument">악기</button>
          <button type="button" class="tab-button" data-type="mood">무드</button>
          <button type="button" class="tab-button" data-type="genre">장르</button>
        </div>
        <!-- 검색 및 목록 영역 -->
        <input type="text" id="tag-search" placeholder="태그 검색" />
        <ul id="tag-list"></ul>
        <button type="button" class="close-modal">닫기</button>
        <button type="button" class="reset-tags">태그 초기화</button>
        <button type="submit" class="apply-tags">수정 확정</button>
      </div>
      <section class="upload-section">
        <form id="tagForm">
          <ul id="selected-tags"></ul>
        </form>
      </section>
    `;
    document.body.appendChild(tagModal);
    // 모달 닫기 버튼 이벤트 (최초 등록)
    tagModal.querySelector(".close-modal").addEventListener("click", () => {
      tagModal.classList.add("hidden");
    });
    // 초기화 완료 플래그 설정
    tagModal.dataset.initialized = "true";
  }

  // 모달 내 요소들 선택 (이미 등록된 이벤트 리스너는 그대로 유지)
  const tagSearchInput = tagModal.querySelector("#tag-search");
  const tagListElement = tagModal.querySelector("#tag-list");
  const selectedTagsElement = tagModal.querySelector("#selected-tags");
  const resetTagsButton = tagModal.querySelector(".reset-tags");
  const tagForm = tagModal.querySelector("#tagForm");
  const tabButtons = tagModal.querySelectorAll(".tab-button");
  const applyTagsButton = tagModal.querySelector(".apply-tags");

  // 초기화: 값, 목록, 선택된 항목, hidden inputs 초기화
  tagSearchInput.value = "";
  tagListElement.innerHTML = "";
  selectedTagsElement.innerHTML = "";
  tagForm.querySelectorAll("input[type='hidden']").forEach(input => input.remove());

  let allTags = [];
  let currentTab = "instrument"; // 초기 탭 설정

  // (이미 이벤트가 등록되어 있다면 중복 등록하지 않음)
  if (!tagModal.dataset.eventsRegistered) {
    // 탭 버튼 클릭 이벤트 등록
    tabButtons.forEach(button => {
      button.addEventListener("click", () => {
        tabButtons.forEach(btn => btn.classList.remove("active"));
        button.classList.add("active");
        currentTab = button.getAttribute("data-type");
        renderTagList(filterTagsByType(allTags, currentTab));
      });
    });

    // 태그 검색 이벤트 등록
    tagSearchInput.addEventListener("input", (e) => {
      const searchTerm = e.target.value.toLowerCase();
      const filtered = filterTagsByType(allTags, currentTab).filter(({ tag }) =>
        tag.toLowerCase().includes(searchTerm)
      );
      renderTagList(filtered);
    });

    // 리셋 버튼 이벤트 등록
    resetTagsButton.addEventListener("click", () => {
      selectedTagsElement.innerHTML = "";
      tagForm.querySelectorAll("input[type='hidden']").forEach(input => input.remove());
    });

    // 폼 제출 이벤트 등록
    tagForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      // 검증: 각 카테고리에서 최소한 하나 이상의 태그가 선택되어야 함 (배열 형태로 수집)
      const selectedInputs = Array.from(tagForm.elements).filter(el => el.type === "hidden");
      const selectedByType = { instrument: [], mood: [], genre: [] };
      selectedInputs.forEach(input => {
        if (input.name === "instrument") {
          selectedByType.instrument.push(input.value);
        } else if (input.name === "mood") {
          selectedByType.mood.push(input.value);
        } else if (input.name === "genre") {
          selectedByType.genre.push(input.value);
        }
      });
      if (selectedByType.instrument.length === 0 || selectedByType.mood.length === 0 || selectedByType.genre.length === 0) {
        alert("모든 태그 종류(악기, 무드, 장르)에서 하나 이상 선택해주세요.");
        return;
      }
      const jsonData = serializeFormToJSON(tagForm);
      console.log("Tag form data:", jsonData); // 디버깅 출력
      const { errors, processedData } = inputHandler(jsonData, tagForm);
      const handle = {
            success:{
               navigate:"/me/sounds/tags"
            },
        };
      if (!errors) {
        await axiosPost({ endpoint: `/api/me/tracks/${trackId}/tags`, body: processedData, handle });
        tagModal.classList.add("hidden");
      }
    });

    // "수정 확정" 버튼 클릭 시 폼 submit 이벤트 강제 발생
    applyTagsButton.addEventListener("click", () => {
      tagForm.dispatchEvent(new Event("submit", { bubbles: true, cancelable: true }));
    });

    // 마감: 이벤트 등록 완료 플래그
    tagModal.dataset.eventsRegistered = "true";
  }

  // 내부 헬퍼 함수: 현재 탭에 해당하는 태그만 반환
  function filterTagsByType(tags, type) {
    return tags.filter(tagObj => tagObj.type === type);
  }

  // 내부 헬퍼 함수: 태그 목록 렌더링
  function renderTagList(tags) {
    tagListElement.innerHTML = "";
    tags.forEach(({ tag, type }) => {
      const li = document.createElement("li");
      li.textContent = tag;
      li.addEventListener("click", () => {
        // 여러 개 선택 허용: 중복 선택은 막기
        const alreadySelected = Array.from(selectedTagsElement.children)
          .some(item => item.textContent === tag && item.getAttribute("data-type") === type);
        if (!alreadySelected) {
          const selectedLi = document.createElement("li");
          selectedLi.textContent = tag;
          selectedLi.setAttribute("data-type", type);
          selectedTagsElement.appendChild(selectedLi);
          // hidden input 추가 (이름 단순하게 지정)
          const input = document.createElement("input");
          input.type = "hidden";
          input.name = type;
          input.value = tag;
          tagForm.appendChild(input);
        }
      });
      tagListElement.appendChild(li);
    });
  }

  // 태그 불러오기 (axiosGet가 응답 데이터를 바로 반환한다고 가정)
  async function fetchTags() {
    try {
      const response = await axiosGet({ endpoint: '/api/sounds/tags' });
      if (!response || !response.dtoList) {
        throw new Error("dtoList가 응답 데이터에 없습니다.");
      }
      const { dtoList } = response;
      allTags = [
        ...dtoList[0].instrument.map(tag => ({ tag, type: 'instrument' })),
        ...dtoList[0].mood.map(tag => ({ tag, type: 'mood' })),
        ...dtoList[0].genre.map(tag => ({ tag, type: 'genre' }))
      ].filter(item => item.tag);
      // 초기 탭에 맞게 렌더링
      renderTagList(filterTagsByType(allTags, currentTab));
    } catch (error) {
      console.error("Error fetching tags:", error);
    }
  }
  fetchTags();
  tagModal.classList.remove("hidden");
}

// -------------------------------------------------------------------
// 6. 모달 외부 클릭 시 닫힘 처리
// -------------------------------------------------------------------
window.addEventListener('click', (event) => {
  const modal = document.getElementById('soundDetailModal');
  if (event.target === modal || event.target.classList.contains('close')) {
    modal.style.display = 'none';
  }
});


//------
// 7. 업로드
//----
export async function renderSoundUpload(){
  const container = document.getElementById('content-body');
  container.innerHTML = '';

  const renderHTML = `
    <h1>음악 업로드 페이지</h1>
    <!-- 앨범 이미지 업로드 -->
    <form id="myImage" class="myImage">
      <section class="upload-section">
        <h2>앨범 이미지를 선택해주세요.</h2>
        <input type="file" id="file-upload-image" name="file" accept=".jpg,.jpeg,.png">
        <input type="text" id="title-image" name="title" placeholder="제목 (자동 입력됨)" readonly hidden>
        <button type="submit" class="upload">업로드</button>
      </section>
    </form>
    <!-- 음원 파일 업로드 -->
    <form id="myTrack" class="myTrack">
      <section class="upload-section">
        <h2>음원 파일을 선택해주세요.</h2>
        <input type="file" id="file-upload-track" name="file" accept=".mp3,.wav">
        <input type="text" id="title-track" name="title" placeholder="제목 (자동 입력됨)" readonly hidden>
        <button type="submit" class="upload">업로드</button>
      </section>
    </form>
    <!-- 메타 정보 입력 폼 -->
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
        <input type="text" id="group-image" name="albumDTO.albumArtPath" readonly hidden>
        <h2>앨범 설명을 작성해주세요.</h2>
        <textarea id="group-description" name="albumDTO.description" placeholder="앨범 설명"></textarea>
      </section>

      <!-- 곡 정보 입력 -->
      <section class="upload-section">
        <h2>곡 제목을 입력해주세요.</h2>
        <input type="text" id="song-title" name="musicDTO.title" placeholder="곡 제목">
        <input type="text" id="file-upload-track-path" name="musicDTO.filePath" readonly hidden>
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
        <button type="button" class="reset-tags-main">태그 초기화</button>
      </section>

      <!-- 최종 확인 및 제출 -->
      <section class="upload-section">
        <h2>최종적으로 작성한 정보를 확인해주세요.</h2>
        <div id="song-info-list"></div>
        <button type="submit" class="upload">업로드</button>
      </section>
    </form>

    <!-- 태그 선택 모달 (탭 기반) -->
    <div id="tag-modal" class="modal hidden">
      <div class="modal-content">
        <h2>태그 선택</h2>
        <!-- 탭 영역 -->
        <div class="tag-tabs">
          <button type="button" class="tab-button active" data-type="instrument">악기</button>
          <button type="button" class="tab-button" data-type="mood">무드</button>
          <button type="button" class="tab-button" data-type="genre">장르</button>
        </div>
        <!-- 검색 및 목록 영역 -->
        <input type="text" id="tag-search" placeholder="태그 검색" />
        <ul id="tag-list"></ul>
        <button type="button" class="close-modal">닫기</button>
        <button type="button" class="reset-tags-modal">태그 초기화</button>
        <button type="button" class="apply-tags">수정 확정</button>
      </div>
    </div>
  `;
  container.innerHTML = renderHTML;

  // 폼 및 엘리먼트 선택
  const form = document.getElementById("myForm");
  const imageForm = document.getElementById("myImage");
  const trackForm = document.getElementById("myTrack");

  // 태그 모달 관련 요소들
  const tagModal = document.getElementById("tag-modal");
  const openModalBtn = document.querySelector(".open-modal");
  const closeModalBtn = tagModal.querySelector(".close-modal");
  const tagList = tagModal.querySelector("#tag-list");
  const tagSearch = tagModal.querySelector("#tag-search");
  const resetTagsModalBtn = tagModal.querySelector(".reset-tags-modal");
  const applyTagsBtn = tagModal.querySelector(".apply-tags");
  const tabButtons = tagModal.querySelectorAll(".tab-button");

  // 메인 폼에 선택된 태그 목록 (모달 외부)
  const selectedTags = document.getElementById("selected-tags");
  // 메인 폼의 태그 초기화 버튼 (별도 처리)
  const resetTagsMainBtn = document.querySelector(".reset-tags-main");

  let uploadImage = '';
  let uploadTrack = '';
  let allTags = [];
  let activeCategory = "instrument"; // 기본 탭

  // 모달 관련 이벤트
  openModalBtn.addEventListener("click", openTagModal);
  closeModalBtn.addEventListener("click", closeTagModal);
  resetTagsModalBtn.addEventListener("click", resetTags);
  applyTagsBtn.addEventListener("click", closeTagModal);
  resetTagsMainBtn.addEventListener("click", resetTags);

  // 탭 버튼 클릭 이벤트 - active 상태 전환 및 해당 카테고리 태그 렌더링
  tabButtons.forEach(button => {
    button.addEventListener("click", () => {
      tabButtons.forEach(btn => btn.classList.remove("active"));
      button.classList.add("active");
      activeCategory = button.getAttribute("data-type");
      renderTagList(filterTags());
    });
  });

  // 검색 입력 이벤트 - 현재 active 카테고리 내에서 필터링
  tagSearch.addEventListener("input", () => {
    renderTagList(filterTags());
  });

  // 모달 열기 시 태그 데이터 로드
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
      // 기본 active 카테고리(악기)에 해당하는 태그 렌더링
      renderTagList(filterTags());
    } catch (error) {
      console.error("태그 데이터를 가져오는 중 오류 발생:", error);
    }
  }

  // 현재 activeCategory와 검색어에 따라 태그 필터링
  function filterTags() {
    const searchQuery = tagSearch.value.toLowerCase();
    return allTags.filter(({ tag, type }) => {
      return type === activeCategory && tag.toLowerCase().includes(searchQuery);
    });
  }

  // 태그 목록 렌더링
  function renderTagList(tags) {
    tagList.innerHTML = "";
    tags.forEach(({ tag, type }) => {
      const li = document.createElement("li");
      li.textContent = tag;
      li.addEventListener("click", () => selectTag(tag, type));
      tagList.appendChild(li);
    });
  }

  // 태그 선택 시 중복체크 후 메인 폼에 태그 추가 (화면에도 표시)
  function selectTag(tag, type) {
    if (!Array.from(selectedTags.children).some(li => li.textContent === tag)) {
      const selectedLi = document.createElement("li");
      selectedLi.textContent = tag;
      selectedTags.appendChild(selectedLi);

      const input = document.createElement("input");
      input.type = "hidden";
      input.name = `tagsDTO.${type}`;
      input.value = tag;
      form.appendChild(input);
    }
  }

  // 모달 닫기
  function closeTagModal() {
    tagModal.classList.add("hidden");
  }

  // 태그 초기화 (모달 및 메인 폼)
  function resetTags() {
    selectedTags.innerHTML = "";
    const inputs = form.querySelectorAll("input[name^='tagsDTO.']");
    inputs.forEach(input => input.remove());
  }

  // === 이미지 업로드 처리 ===
  imageForm.addEventListener("submit", async function (e) {
    e.preventDefault();
    const uploadButton = imageForm.querySelector('button.upload');
    uploadButton.disabled = true;
    const fileInput = imageForm.querySelector('input[type="file"]');
    const selectedFiles = fileInput.files;
    if (selectedFiles.length === 0) {
      alert("이미지를 업로드해주세요.");
      uploadButton.disabled = false;
      return;
    }
    if (selectedFiles.length > 1) {
      alert("이미지는 한 번에 하나만 업로드할 수 있습니다.");
      uploadButton.disabled = false;
      return;
    }
    const selectedFile = selectedFiles[0];
    const maxFileSize = 2 * 1024 * 1024; // 2MB
    if (selectedFile.size > maxFileSize) {
      alert("이미지 크기는 2MB를 초과할 수 없습니다.");
      uploadButton.disabled = false;
      return;
    }
    const allowedExtensions = ["jpg", "jpeg", "png"];
    const fileExtension = selectedFile.name.split(".").pop().toLowerCase();
    if (!allowedExtensions.includes(fileExtension)) {
      alert(`허용된 파일 형식은 ${allowedExtensions.join(", ")}입니다.`);
      uploadButton.disabled = false;
      return;
    }
    // 파일 이름을 제목에 자동 입력
    const titleInput = imageForm.querySelector('input[name="title"]');
    titleInput.value = selectedFile.name;
    const formData = new FormData(imageForm);
//    const handle = {
//      onSuccess: (data) => {
//        alert('이미지가 성공적으로 업로드되었습니다!');
//      },
//      onBadRequest: () => {
//        alert("업로드가 실패했습니다.");
//        imageForm.querySelectorAll('input, button').forEach(el => el.disabled = false);
//      }
//    };

    uploadImage = await axiosPost({ endpoint: "/api/files/albums", body: formData });
  });

  // === 음원 파일 업로드 처리 ===
  trackForm.addEventListener("submit", async function (e) {
    e.preventDefault();
    const uploadButton = trackForm.querySelector('button.upload');
    uploadButton.disabled = true;
    const fileInput = trackForm.querySelector('input[type="file"]');
    const selectedFiles = fileInput.files;
    if (selectedFiles.length === 0) {
      alert("파일을 업로드해주세요.");
      uploadButton.disabled = false;
      return;
    }
    if (selectedFiles.length > 1) {
      alert("파일은 한 번에 하나만 업로드할 수 있습니다.");
      uploadButton.disabled = false;
      return;
    }
    const selectedFile = selectedFiles[0];
    const maxFileSize = 20 * 1024 * 1024; // 20MB
    if (selectedFile.size > maxFileSize) {
      alert("파일 크기는 20MB를 초과할 수 없습니다.");
      uploadButton.disabled = false;
      return;
    }
    const allowedExtensions = ["mp3", "wav"];
    const fileExtension = selectedFile.name.split(".").pop().toLowerCase();
    if (!allowedExtensions.includes(fileExtension)) {
      alert(`허용된 파일 형식은 ${allowedExtensions.join(", ")}입니다.`);
      uploadButton.disabled = false;
      return;
    }
    const titleInput = trackForm.querySelector('input[name="title"]');
    titleInput.value = selectedFile.name;
    const formData = new FormData(trackForm);
//    const handle = {
//      onSuccess: (data) => {
//        alert('음원이 성공적으로 업로드되었습니다!');
//      },
//      onBadRequest: () => {
//        alert("업로드가 실패했습니다.");
//        trackForm.querySelectorAll('input, button').forEach(el => el.disabled = false);
//      }
//    };
    uploadTrack = await axiosPost({ endpoint: "/api/files/tracks", body: formData });
  });

  // === 최종 메타 데이터 제출 처리 ===
  form.addEventListener("submit", async function (event) {
    event.preventDefault();

    // 각 태그 카테고리별 하나 이상의 태그가 선택되었는지 검증
    const hasInstrumentTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDTO.instrument"));
    const hasMoodTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDTO.mood"));
    const hasGenreTag = Array.from(form.elements).some(input => input.name.startsWith("tagsDTO.genre"));
    if (!hasInstrumentTag) {
      alert("태그를 하나 이상 선택해야 합니다: 악기를 선택해주세요.");
      return;
    }
    if (!hasMoodTag) {
      alert("태그를 하나 이상 선택해야 합니다: 무드를 선택해주세요.");
      return;
    }
    if (!hasGenreTag) {
      alert("태그를 하나 이상 선택해야 합니다: 장르를 선택해주세요.");
      return;
    }

    // 업로드된 이미지와 음원 파일 경로를 폼에 세팅
    form.querySelector('input[name="albumDTO.albumArtPath"]').value = uploadImage;
    form.querySelector('input[name="musicDTO.filePath"]').value = uploadTrack;

    const jsonData = serializeFormToJSON(form);
    const { errors, processedData } = inputHandler(jsonData, form);
//    const handle = {
//      onSuccess: (data) => {
//        alert('음원이 성공적으로 업로드되었습니다!');
//        router.navigate("/sounds/tracks");
//      },
//      onBadRequest: () => {
//        alert("업로드가 실패했습니다.");
//      }
//    };

    // 서버 응답 핸들링 객체
    const handle = {
        success:{
            navigate:"/sounds/tracks"
        },
    };

    if (!errors) {
      await axiosPost({ endpoint: '/api/me/sounds', body: processedData, handle });
    }
  });
}
