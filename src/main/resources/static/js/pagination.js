import { router } from '/js/router.js';
import { globalStateManager } from "/js/globalState.js";

export async function renderPagination(responseDTO) {
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