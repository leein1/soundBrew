import { globalStateManager } from '/js/globalState.js';
import {router} from "/js/router.js";

export function renderViewType(){
    const container = document.getElementById("render-view-type-container");
    container.innerHTML='';

    const item = document.createElement('div');
    item.classList.add('view-type');

    item.innerHTML=`
        <span id="viewToggleBtn" class="viewToggleBtn">
            <img src="/images/list_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg" alt="정렬">
            <span id="toggleText">보기전환</span>
        </span>
    `;

    container.appendChild(item);

    document.getElementById('viewToggleBtn').addEventListener('click', toggleView);
}

// '앨범' / '트랙' 보기 토글 함수
async function toggleView() {
    // alert("리스트 뷰 타입 토글");
    const button = document.getElementById('viewToggleBtn');
    const currentView = globalStateManager.getState().currentView;
    // alert(currentView);
    console.log("현재 뷰 스테이트" + currentView);

    const textNode = document.getElementById('toggleText');

    if (currentView === '/sounds/albums') {
        // alert("앨범으로 보기 로 변경되야함");
        // '앨범'에서 '트랙' 보기로 변경
        console.log("if문은 작동함")
        textNode.textContent = '음원 목록으로 보기';
    } else {
        // alert("트랙으로 보기 로 변경되야함");
        // '트랙'에서 '앨범' 보기로 변경
        console.log("else문은 작동함")
        textNode.textContent = '앨범 목록으로 보기';
    }

    // 상태 변경 후 데이터를 새로 호출
    await updateViewData();
}

// 현재 상태에 맞는 데이터 호출
async function updateViewData() {
    const currentParams = new URLSearchParams(window.location.search);
    currentParams.delete('page');

    const currentView = globalStateManager.getState().currentView;
    // '/'도 트랙 뷰로 간주
    const isTrackView = (currentView === '/sounds/tracks' || currentView === '/');
    // 트랙 뷰일 경우 앨범 뷰로 전환, 그 외(앨범 뷰)라면 기본 페이지인 '/'로 전환
    const nextURL = isTrackView ? '/sounds/albums' : '/sounds/tracks';

    const newQueryString = currentParams.toString();
    const newUrl = `${nextURL}?${newQueryString}`;

    router.navigate(newUrl);
}