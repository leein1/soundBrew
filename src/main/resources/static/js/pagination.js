import { router as defaultRouter } from '/js/router.js';
import { router as meSoundRouter } from '/js/meSoundRouter.js';
import { router as adminSoundRouter} from '/js/adminSoundRouter.js'
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
    //상태관리에서 때옴
    const myState = globalStateManager.getState().currentView;
    // alert("현재 내 VIEW_STATE : " + myState);
    const stateActions = {
        '/sounds/tracks': defaultRouter,
        '/sounds/albums': defaultRouter,
        '/sounds/tracks/one': defaultRouter,
        '/sounds/albums/one': defaultRouter,
        '/me/sounds' : meSoundRouter,
        '/me/sounds/albums' : meSoundRouter,
        '/me/sounds/tracks' : meSoundRouter,
        '/me/sounds/tags' : meSoundRouter,
        '/admin/tracks' :adminSoundRouter,
        '/admin/albums':adminSoundRouter,
        '/admin/albums/verify':adminSoundRouter,
        '/admin/albums/one/verify':adminSoundRouter,
        '/admin/tags/new' :adminSoundRouter,
        '/admin/tags/spelling' :adminSoundRouter,
    };

    const target = event.target;

    if (target.classList.contains("page-link") && !target.classList.contains("active")) {
        const selectedPage = target.getAttribute("data-page");

        const currentParams = new URLSearchParams(window.location.search);
        currentParams.set('page', selectedPage);

        const newQueryString = currentParams.toString();

        const newUrl = `${window.location.pathname}?${newQueryString}`;

        const selectedRouter = stateActions[myState];
        selectedRouter.navigate(newUrl);
    }
}