import { axiosGet } from "/js/fetch/standardAxios.js";
import { renderTotalSounds,renderTotalAlbums } from '/js/render/sound.js';
import { renderPagination } from "/js/pagination.js";
import { globalState,dispatch,actions } from '/js/globalState.js';

export function renderSort() {
    const container = document.getElementById("render-result-sort-container");
    container.innerHTML = '';

    const item = document.createElement('div');
    item.classList.add('music-sort');

    item.innerHTML = `
        <div class="music-sort">
            <div class="sort-01">
                <span class="music-sort-left" id="sortKeyword">정렬
                    <img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정렬" id="sortIcon">
                </span>
               
                <!-- 정렬 드롭다운 -->
                <div class="music-sort-menu" id="musicSortMenu">
                    <ul>
                        <li data-sort="newest">Newest</li>
                        <li data-sort="oldest">Oldest</li>
                        <li data-sort="download">Download</li>
                    </ul>
                </div>
            </div>
            <div class="sort-02">
                <button id="viewToggleBtn">앨범으로 보기</button>
            </div>
        </div>
    `;

    container.appendChild(item);

    setupDropdownEvents();
    highlightCurrentSort();

    // '앨범으로 보기' 버튼 클릭 시 이벤트 핸들러
    document.getElementById('viewToggleBtn').addEventListener('click', toggleView);
}

// 드롭다운 열기/닫기 이벤트 핸들러
function setupDropdownEvents() {
    const dropdown = document.getElementById('musicSortMenu');
    document.querySelectorAll('#sortIcon, #sortKeyword').forEach(element => {
        element.addEventListener('click', () => toggleDropdown(dropdown));
    });

    // 외부 클릭 시 드롭다운 닫기
    document.addEventListener('click', event => {
        if (!event.target.closest('#musicSortMenu, #sortIcon, #sortKeyword')) {
            closeDropdown(dropdown);
        }
    });

    // 정렬 옵션 선택 시 처리
    document.querySelectorAll('.music-sort-menu li').forEach(item => {
        item.addEventListener('click', async () => handleSortSelection(item, dropdown));
    });
}

// 드롭다운 토글
function toggleDropdown(dropdown) {
    dropdown.style.display = (dropdown.style.display === 'block') ? 'none' : 'block';
}

// 드롭다운 닫기
function closeDropdown(dropdown) {
    dropdown.style.display = 'none';
}

// 정렬 옵션 선택 처리
async function handleSortSelection(item, dropdown) {
    const sortValue = item.dataset.sort;

    // 모든 항목에서 'active' 클래스 제거 후 현재 항목에 추가
    document.querySelectorAll('.music-sort-menu li').forEach(li => li.classList.remove('active'));
    item.classList.add('active');

    try {
        const response = await updateSortParam(sortValue);
        renderTotalSounds(response.dtoList); // 새로운 데이터로 렌더링
        renderPagination(response);
    } catch (error) {
        console.error("Error fetching sorted data:", error);
    }

    closeDropdown(dropdown); // 드롭다운 닫기
}

// 현재 정렬 상태 강조 표시
function highlightCurrentSort() {
    const currentParams = new URLSearchParams(window.location.search);
    const currentSort = currentParams.get('more[sort]');

    if (currentSort) {
        const activeItem = document.querySelector(`.music-sort-menu li[data-sort="${currentSort}"]`);
        if (activeItem) activeItem.classList.add('active');
    }
}

// URL 업데이트 및 데이터 호출
async function updateSortParam(sortValue) {
    const currentParams = new URLSearchParams(window.location.search);

    currentParams.set('more[sort]', sortValue);
    currentParams.delete('page');

    const newQueryString = currentParams.toString();
    const newUrl = `${window.location.pathname}?${newQueryString}`;

    window.history.pushState({ point: window.location.pathname, params: newQueryString }, '', newUrl);

    // 전역 상태 변수
    const endpoint = globalState.currentView === 'albums' ? '/api/sounds/albums' : '/api/sounds/tracks';

    // 데이터 호출
    return await axiosGet({ endpoint: `${endpoint}?${newQueryString}` });
}

// '앨범' / '트랙' 보기 토글 함수
async function toggleView() {
    const button = document.getElementById('viewToggleBtn');
    const currentView = globalState.currentView;

    if (currentView === 'albums') {
        // '앨범'에서 '트랙' 보기로 변경
        dispatch({type: actions.SET_VIEW, payload: 'tracks'});
        button.textContent = '앨범으로 보기';
    } else {
        // '트랙'에서 '앨범' 보기로 변경
        dispatch({type: actions.SET_VIEW, payload: 'albums'});
        button.textContent = '트랙으로 보기';
    }

    // 상태 변경 후 데이터를 새로 호출
    await updateViewData();
}

// 현재 상태에 맞는 데이터 호출
async function updateViewData() {
    const currentParams = new URLSearchParams(window.location.search);
    currentParams.delete('page');

    const newQueryString = currentParams.toString();
    const newUrl = `${window.location.pathname}?${newQueryString}`;

    window.history.pushState(newUrl, '', newUrl);

    const endpoint = globalState.currentView === 'albums' ? '/api/sounds/albums' : '/api/sounds/tracks';

    try {
        const response = await axiosGet({ endpoint: `${endpoint}?${newQueryString}` });
        if(endpoint === '/api/sounds/albums'){
            renderTotalAlbums(response.dtoList);
        }else {
            renderTotalSounds(response.dtoList);
        }
        renderPagination(response);
    } catch (error) {
        console.error("Error fetching updated data:", error);
    }
}
