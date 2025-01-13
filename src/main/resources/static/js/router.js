import {axiosGet,axiosPost} from '/js/fetch/standardAxios.js';
import { extractTagsFromURL } from "/js/globalState.js";
import {renderTotalSounds,renderTotalAlbums,renderSoundOne,renderAlbumOne} from '/js/render/sound.js';
import {renderPagination} from "/js/pagination.js";
import {renderSort} from "/js/sound/sort.js";
import {renderTagsFromSearch} from "/js/sound/soundTagsModule.js";
import {renderViewType} from "/js/sound/viewType.js";
import {globalStateManager} from "/js/globalState.js";


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

router.addRoute('/sounds/tracks',async () => {
    const queryParams = window.location.search; // 쿼리 파라미터 들고오기
    // alert("addRoute : "+queryParams);

    // if(compareTagsWithUrlParams()){
        const renderTags = await axiosGet({endpoint: `/api/sounds/tags${queryParams}`});
        renderTagsFromSearch(renderTags); // 태그 영역 컴포넌트
        extractTagsFromURL();
    // }

    const response = await axiosGet({endpoint: `/api/sounds/tracks${queryParams}`});

    renderTotalSounds(response.dtoList);
    renderPagination(response);
});

router.addRoute('/sounds/albums', async () => {
    const queryParams = window.location.search; // 쿼리 파라미터 들고오기

    const renderTags = await axiosGet({endpoint: `/api/sounds/tags${queryParams}`});
    renderTagsFromSearch(renderTags); // 태그 영역 컴포넌트
    extractTagsFromURL()

    const response = await axiosGet({endpoint: `/api/sounds/albums${queryParams}`});
    renderTotalAlbums(response.dtoList);
    renderPagination(response);
});

router.addRoute('/sounds/tracks/one',async (context) => {
    const urlParams = new URLSearchParams(window.location.search);
    const nickname = urlParams.get('nickname');
    const title = urlParams.get('title');

    const response = await axiosGet({endpoint: '/api/sounds/tracks/' + nickname + '/title/' + title});
    const tagsBody = {dto: [response.dto]};
    const renderTags = await axiosPost({endpoint: '/api/sounds/tags', body: tagsBody});

    renderSoundOne(response.dto, renderTags);
    //얘는 페이징이 없음 ( 지우거나 해야함)
    const container = document.querySelector('.pagination-container');
    container.innerHTML='';
});

router.addRoute('/sounds/albums/one',async (context) => {
    const urlParams = new URLSearchParams(window.location.search);
    const newQueryString = urlParams.toString();
    const nickname = urlParams.get('nickname');
    const albumName = urlParams.get('albumName');
    // alert("nickname : "+ nickname + " , albumName : "+albumName);

    const response = await axiosGet({endpoint: `/api/sounds/albums/` + nickname + `/title/` + albumName+`?${newQueryString}`});
    renderAlbumOne(response);

    renderTotalSounds(response.dtoList);
    renderPagination(response);

    const tagsBody = {dto: response.dtoList};
    const renderTags = await axiosPost({endpoint: '/api/sounds/tags', body: tagsBody});

    renderTagsFromSearch(renderTags);
});

// router.addRoute('',()=>{
//
// });

router.start();



