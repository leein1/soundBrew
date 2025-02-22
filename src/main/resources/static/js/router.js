import {axiosGet,axiosPost} from '/js/fetch/standardAxios.js';
import { extractTagsFromURL,compareTagsWithUrlParams } from "/js/tagStateUtil.js";
import {renderTotalSounds,renderTotalAlbums,renderSoundOne,renderAlbumOne} from '/js/sound/sound.js';
import {renderPagination} from "/js/pagination.js";
import {renderSort} from "/js/sound/sort.js";
import {renderTagsFromSearch} from "/js/sound/soundTagsModule.js";
import {renderViewType} from "/js/sound/viewType.js";
import {globalStateManager} from "/js/globalState.js";
import {renderMyAlbums,renderMyTracks,renderMyTags,renderMyMain,renderSoundUpload} from "/js/sound/soundManage.js";
import {renderArtistsTracks,renderArtistsAlbums,renderTagsSpelling,renderTagsNew,renderArtistsVerify, renderArtistsVerifyOne,renderTotalSoundsVerify} from "/js/sound/soundAdmin.js";
import { loadSoundTypeCSS, loadSoundManageTypeCSS, updateDynamicCSS , SoundTypeCSSFiles, SoundManageTypeCSSFiles, UserAdminTypeCSSFiles, loadUserAdminTypeCSS, removeAllDynamicCSS,AdminStatisticTypeCSSFiles,loadAdminStatisticTypeCSS,loadSoundManageMainTypeCSS,SoundManageMainTypeCSSFiles} from '/js/CSSLoader.js';
import {renderUserInfoWithRole} from '/js/user/userAdmin.js';
import {renderSubscriptionInfo } from '/js/user/subscriptionAdmin.js';
import {initDashboard} from '/js/user/dashboard.js';

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
        this.updateTagLoadingStateFromURL() // 뷰 업데이트 보다 먼저 해야함.
        this.updateStateFromURL(); // 뷰 업데이트
    }

    //상태 업데이트
    updateStateFromURL() {
        // alert("!!! 상태 업데이트 !!!");
        const path = window.location.pathname; // 예: "/sounds/tracks/one"

        // const segments = path.split('/').filter(Boolean); // 빈 문자열 제거
        // const firstSegment = segments[0]; // 첫 번째 요소 가져오기

        // alert("segments : "+segments);
        // alert("firstSegment : "+firstSegment);
        // 필요하면 상태 업데이트 로직에 활용
        globalStateManager.dispatch({ type: 'SET_VIEW', payload: path });
    }

    //태그 첫 로딩 초기화
    updateTagLoadingStateFromURL(){
        const viewState = globalStateManager.getState().currentView;
        const path = window.location.pathname;

        if(viewState !== path){
            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: true});
            // alert("globalStateManager.getState().isFirstTagLoad : "+globalStateManager.getState().isFirstTagLoad);
        }
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

document.addEventListener('DOMContentLoaded', () => {
    router.addRoute('/sounds/tracks', async () => {
        updateDynamicCSS(SoundTypeCSSFiles);
        await loadSoundTypeCSS();

        const queryParams = window.location.search; // 현재 URL 쿼리 파라미터 가져오기

        // 초기 로딩이거나, 태그가 변경된 경우 API 호출
        if (globalStateManager.getState().isFirstTagLoad || compareTagsWithUrlParams()) {
            renderSearch();
            renderSort();
            renderViewType();

            // const handle = {
            //     onSuccess: (data) => {
            //         console.log("태그 데이터를 성공적으로 불러왔음:", data);
            //     },
            //     onBadRequest: (data) => {
            //         console.error("태그 데이터 요청 실패:", data);
            //     },
            // };

            // 태그 API 호출
            const renderTags = await axiosGet({ endpoint: `/api/sounds/tags/mapped${queryParams}` });

            renderTagsFromSearch(renderTags); // 태그 UI 렌더링
            extractTagsFromURL(); // 태그 상태 업데이트

            // 최초 호출 이후에는 상태를 false로 변경
            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: false});
        }
        // 트랙 데이터를 항상 가져옴 (검색 결과는 항상 갱신해야 하므로)
        const response = await axiosGet({ endpoint: `/api/sounds/tracks${queryParams}` });

        console.log(response);
        renderTotalSounds(response.dtoList); // 트랙 리스트 렌더링
        renderPagination(response); // 페이지네이션 렌더링
    });

    router.addRoute('/sounds/albums', async () => {
        updateDynamicCSS(SoundTypeCSSFiles);
        await loadSoundTypeCSS();

        const queryParams = window.location.search; // 쿼리 파라미터 들고오기

        if (globalStateManager.getState().isFirstTagLoad || compareTagsWithUrlParams()) {
            renderSearch();
            renderSort();
            renderViewType();

            const renderTags = await axiosGet({endpoint: `/api/sounds/tags/mapped${queryParams}`});
            renderTagsFromSearch(renderTags); // 태그 영역 컴포넌트
            extractTagsFromURL();
            // 최초 호출 이후에는 플래그를 false로 변경
            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: false});
        }

        const response = await axiosGet({endpoint: `/api/sounds/albums${queryParams}`});
        renderTotalAlbums(response.dtoList);
        renderPagination(response);
    });

    router.addRoute('/sounds/tracks/one',async (context) => {
        updateDynamicCSS(SoundTypeCSSFiles);
        await loadSoundTypeCSS();

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
        updateDynamicCSS(SoundTypeCSSFiles);
        await loadSoundTypeCSS();

        const urlParams = new URLSearchParams(window.location.search);
        const newQueryString = urlParams.toString();
        const nickname = urlParams.get('nickname');
        const albumName = urlParams.get('albumName');

        const response = await axiosGet({endpoint: `/api/sounds/albums/` + nickname + `/title/` + albumName+`?${newQueryString}`});
        renderAlbumOne(response);

        renderTotalSounds(response.dtoList);
        renderPagination(response);

        // 초기 로딩이거나, 태그가 변경된 경우 API 호출
        if (globalStateManager.getState().isFirstTagLoad || compareTagsWithUrlParams()) {
            renderSort();

            const tagsBody = {dto: response.dtoList};
            const renderTags = await axiosPost({endpoint: '/api/sounds/tags', body: tagsBody});

            renderTagsFromSearch(renderTags);

            globalStateManager.dispatch({type: 'SET_TAG_LOAD_STATUS', payload: false});
        }
    });

    router.addRoute('/me/sounds/upload',async () => {
        updateDynamicCSS(SoundManageMainTypeCSSFiles);
        await loadSoundManageMainTypeCSS();

        renderSoundUpload();
    });

    router.addRoute('/me/sounds', async () => {
        updateDynamicCSS(SoundManageMainTypeCSSFiles);
        await loadSoundManageMainTypeCSS();

        renderMyMain();

        const response = await axiosGet({endpoint: `/api/sounds/tracks`});
    });

    router.addRoute('/me/sounds/albums', async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint: `/api/me/albums${queryParams}`});
        await renderMyAlbums(response);
        renderPagination(response);
    });

    router.addRoute('/me/sounds/tracks', async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint: `/api/me/tracks${queryParams}`});
        await renderMyTracks(response);
        renderPagination(response);
    });

    router.addRoute('/me/sounds/tags', async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint: `/api/me/tracks${queryParams}`});
        await renderMyTags(response);
        renderPagination(response);
    });

    router.addRoute('/admin' , async () =>{
        updateDynamicCSS(AdminStatisticTypeCSSFiles);
        await loadAdminStatisticTypeCSS();

        await initDashboard();
    });

    router.addRoute('/admin/tracks',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint : `/api/admin/tracks${queryParams}`});
        await renderArtistsTracks(response);
        await renderPagination(response);
        renderSearch();
    });

    router.addRoute('/admin/albums',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint : `/api/admin/albums${queryParams}`});
        renderArtistsAlbums(response);
        renderPagination(response);
        renderSearch();
    });

    router.addRoute('/admin/albums/verify',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint : `/api/admin/albums/verify${queryParams}`});
        await renderArtistsVerify(response);
        renderPagination(response);
        // renderSearch();
    });

    router.addRoute('/admin/tags/spelling',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const renderTags = await axiosGet({endpoint: `/api/sounds/tags${queryParams}`});
        renderTagsSpelling(renderTags);
        renderPagination();
    });

    router.addRoute('/admin/tags/new',async()=>{
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        renderTagsNew();
    });

    router.addRoute(`/admin/albums/one/verify`, async()=>{
        updateDynamicCSS(SoundTypeCSSFiles);
        await loadSoundTypeCSS();

        const queryParams = new URLSearchParams(window.location.search);
        const albumId = queryParams.get('id');
        const userId = queryParams.get('uid');

        const response = await axiosGet({endpoint: `/api/admin/albums/${userId}/title/${albumId}/verify`});
        await renderArtistsVerifyOne(response);
        await renderTotalSoundsVerify(response);
        await renderPagination(response);

        if (globalStateManager.getState().isFirstTagLoad || compareTagsWithUrlParams()) {
            const tagsBody = {dto: response.dtoList};
            const renderTags = await axiosPost({endpoint: '/api/sounds/tags', body: tagsBody});

            await renderTagsFromSearch(renderTags);
            extractTagsFromURL(); // 태그 상태 업데이트

            // 최초 호출 이후에는 상태를 false로 변경
            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: false});
        }
    });

    router.addRoute('/admin/users', async () =>{
        //css 로딩/삭제
        updateDynamicCSS(UserAdminTypeCSSFiles);
        await loadUserAdminTypeCSS();

        // 0. 쿼리 파라미터 확인
        const queryParams = window.location.search;
        // 1. 엑시오스를 통한 특정 데이터 들고오기
        const response = await axiosGet({ endpoint:`/api/admin/users${queryParams}` });

        // 2. 이후 데이터(response)로 렌더링
        renderUserInfoWithRole(response);

        // 3. 부가적인 렌더링 검색바, 페이징
        renderPagination(response);
        renderSearch();

    });

    router.addRoute('/admin/subscription', async () =>{
        //css 로딩/삭제
        updateDynamicCSS(UserAdminTypeCSSFiles);
        await loadUserAdminTypeCSS();

        // 1. 엑시오스를 통한 특정 데이터 들고오기
        const response = await axiosGet({ endpoint:`/api/admin/subscription` });

        // 2. 이후 데이터(response)로 렌더링
        renderSubscriptionInfo(response);
    });

    document.querySelector('#soundTracksRoute').addEventListener('click', () => {
        router.navigate('/sounds/tracks');
    });

    document.querySelector('#mySoundRoute').addEventListener('click', () => {
        router.navigate('/me/sounds');
    });

    document.querySelector('#myInfoRoute').addEventListener('click', () => {
        router.navigate('/me/info');
    });

    document.getElementById("adminStatisticRoute").addEventListener("click",()=>{
        router.navigate("/admin");
    });

    document.getElementById("adminInfoRoute").addEventListener("click",()=>{
        router.navigate("/admin/users");
    });

    document.getElementById("adminSoundRoute").addEventListener("click",()=>{
        router.navigate("/admin/albums");
    });

    // 네비게이션들 버튼, 얘네들은 location.href을 사용해서 페이지를 마치 초기화 하듯 유도.
    document.getElementById("sitename").addEventListener("click", () => {
        window.location.href = "/sounds/tracks";
    });

    document.getElementById("registerPage").addEventListener("click", ()=>{
        window.location.href = "/register";
    });

    document.querySelectorAll('.logoutPage').forEach(element =>{
        element.addEventListener('click', function(){
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            window.location.href = "/logout";
        });
    });

    document.querySelectorAll('.loginPage').forEach(element =>{
        element.addEventListener('click', function(){
            window.location.href = "/login";
        });
    });

router.start();

});




