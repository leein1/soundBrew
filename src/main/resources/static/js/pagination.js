import { axiosGet } from "/js/fetch/standardAxios.js";
import { renderTotalSounds,renderTotalAlbums } from '/js/render/sound.js';
// import { globalState } from '/js/globalState.js';
import { router } from '/js/router.js';

export function renderPagination(responseDTO) {
    const container = document.getElementById("pagination-container");
    if (!responseDTO || responseDTO.total === 0) {
        container.innerHTML = ''; // 공란으로 설정
        return;
    }

    const { page, size, total, start, end, prev, next } = responseDTO;

    const totalPages = Math.ceil(total / size); // 전체 페이지 수 계산

    let pageHTML = `
        <div class="pagination">
            ${prev ? `<a class="page-link" data-page="${page - 1}">&laquo; </a>` : ''}
    `;

    for (let i = start; i <= end; i++) {
        if (i === page) {
            // 현재 페이지는 비활성화된 상태로 렌더링
            pageHTML += `
                <span class="page-link active" aria-disabled="true">
                    ${i}
                </span>
            `;
        } else {
            pageHTML += `
                <a class="page-link" data-page="${i}">
                    ${i}
                </a>
            `;
        }
    }

    pageHTML += `
            ${next ? `<a class="page-link" data-page="${page + 1}"> &raquo;</a>` : ''}
        </div>
    `;

    container.innerHTML = pageHTML;

    // 기존 이벤트 리스너를 중복으로 추가하지 않도록 먼저 제거
    container.removeEventListener("click", handlePaginationClick);

    // 새로 이벤트 리스너 추가
    container.addEventListener("click", handlePaginationClick);
}

async function handlePaginationClick(event) {
    const target = event.target;

    if (target.classList.contains("page-link") && !target.classList.contains("active")) {
        const selectedPage = target.getAttribute("data-page");

        const currentParams = new URLSearchParams(window.location.search);
        currentParams.set('page', selectedPage);

        const newQueryString = currentParams.toString();

        const newUrl = `${window.location.pathname}?${newQueryString}`;

        router.navigate(newUrl);
    }
}
//
// async function fetchNewPageData(selectedPage) {
//     // 현재 URL에서 파라미터 가져오기
//     const currentParams = new URLSearchParams(window.location.search);
//
//     // 'page' 파라미터 갱신 (이미 있으면 갱신, 없으면 추가)
//     currentParams.set('page', selectedPage);
//
//     // 새로 갱신된 쿼리 문자열 생성
//     const newQueryString = currentParams.toString();
//
//     // 현재 URL의 pathname에 새로운 쿼리 문자열을 붙여서 새로운 URL 생성
//     const newUrl = `${window.location.pathname}?${newQueryString}`;
//     // 새로운 URL로 페이지의 상태를 갱신
//     window.history.pushState({ point: window.location.pathname, params: newQueryString }, '', newUrl);
//
//     //전역 상태 변수
//     const endpoint = globalState.currentView === 'albums' ? '/api/sounds/albums' : '/api/sounds/tracks';
//
//     // 서버에서 새로운 데이터를 가져옴
//     return await axiosGet({ endpoint: `${endpoint}?${newQueryString}` });
// }
//
