import {axiosGet,axiosPost} from '/js/fetch/standardAxios.js';
import {globalStateManager} from "/js/globalState.js";
import {renderMyAlbums,renderMyTracks,renderMyTags,renderMyMain,renderSoundUpload} from "/js/sound/soundManage.js";
import {renderPagination} from "/js/pagination.js";

export class Router {
    constructor() {
        this.routes = {};
        window.addEventListener('popstate', () => this.handleRouteChange());
    }

    // 라우트 추가
    addRoute(path, handler) {
        this.routes[path] = handler;
    }

    // 현재 경로에 맞는 핸들러 호출
    handleRouteChange() {
        const path = window.location.pathname;
        const handler = this.routes[path];
        if (handler) {
            handler();
        } else {
            // 기본적으로 'not found' 처리
            this.routes['/404'] && this.routes['/404']();
        }

        // 여기서 상태 업데이트 추가
        this.updateStateFromURL();
    }

    //상태 업데이트
    updateStateFromURL() {
        // alert("!!! 상태 업데이트 !!!");
        const path = window.location.pathname; // 예: "/sounds/tracks/one"

        // "/sounds/tracks/one"에서 첫 번째 칸("sounds") 제거
        const segments = path.split('/').filter(Boolean); // 빈 문자열 제거
        const updatedPath = '/' + segments.slice(1).join('/'); // 첫 번째 요소 제거 후 다시 경로 생성

        // 결과적으로 "/tracks/one"과 같은 값이 updatedPath에 저장됨

        // 필요하면 상태 업데이트 로직에 활용
        globalStateManager.dispatch({ type: 'SET_VIEW', payload: updatedPath });
    }

    // 라우터 시작
    start() {
        this.handleRouteChange();
    }

    // 경로 변경
    navigate(path) {
        window.history.pushState({}, '', path);
        this.handleRouteChange();
    }
}

export const router = new Router();
// alert("router");


// 기본적으로 me에 관련된 것은 파라미터의 사용은 제한적이다 즉, page, size 이런거만 있을듯
// ==> 페이징 버튼이 필요하다 렌더링 해오자.

router.addRoute('/me/sounds/upload',()=>{
    renderSoundUpload();
});

router.addRoute('/me/sounds/albums', async ()=>{
    const queryParams = window.location.search;
    const response = await axiosGet({endpoint:`/api/me/albums${queryParams}`});
    await renderMyAlbums(response);
    renderPagination(response);
});

router.addRoute('/me/sounds/tracks', async ()=>{
    const queryParams = window.location.search;
    const response = await axiosGet({endpoint : `/api/me/tracks${queryParams}`});
    await renderMyTracks(response);
    renderPagination(response);
});

router.addRoute('/me/sounds/tags', async ()=>{
    const queryParams = window.location.search;
    const response = await axiosGet({ endpoint:`/api/me/tags${queryParams}`});
    await renderMyTags(response);
    renderPagination(response);
});

router.addRoute('/me/sounds', async ()=>{
    renderMyMain();
});

router.start();



