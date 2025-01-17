import { router } from '/js/router.js';

export function renderSort() {
    const container = document.getElementById("render-result-sort-container");
    container.innerHTML = '';

    const item = document.createElement('div');
    item.classList.add('music-sort');

    item.innerHTML = `
        <div class="sort-01">
            <span class="music-sort-left" id="sortKeyword">
                <img src="/images/swap_vert_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="정렬" id="sortIcon">정렬
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

    `;
    container.appendChild(item);

    setupDropdownEvents();
    highlightCurrentSort();
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

    const currentParams = new URLSearchParams(window.location.search);
    currentParams.set('more[sort]', sortValue);
    currentParams.delete('page');

    const newQueryString = currentParams.toString();
    const newUrl = `${window.location.pathname}?${newQueryString}`;

    closeDropdown(dropdown); // 드롭다운 닫기

    router.navigate(newUrl);
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