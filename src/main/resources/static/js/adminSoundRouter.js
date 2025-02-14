//import {axiosGet,axiosPost} from '/js/fetch/standardAxios.js';
//import {renderPagination} from "/js/pagination.js";
//import {renderTagsFromSearch} from "/js/sound/soundTagsModule.js";
//import {globalStateManager} from "/js/globalState.js";
//import {renderArtistsTracks,renderArtistsAlbums,renderTagsSpelling,renderTagsNew,renderArtistsVerify, renderArtistsVerifyOne,renderTotalSoundsVerify,renderSoundsAdminMain} from "/js/sound/soundAdmin.js";
//import {compareTagsWithUrlParams,extractTagsFromURL} from "/js/tagStateUtil.js";
//import { removeBTypeCSS, loadATypeCSS, loadBTypeCSS, removeATypeCSS } from '/js/CSSLoader.js';
//
//
//export class Router {
//    constructor() {
//        this.routes = {};
//        window.addEventListener('popstate', () => this.handleRouteChange());
//    }
//
//    // 라우트 추가
//    addRoute(path, handler) {
//        this.routes[path] = handler;
//    }
//
//    // 현재 경로에 맞는 핸들러 호출
//    handleRouteChange() {
//        const path = window.location.pathname;
//        const handler = this.routes[path];
//        if (handler) {
//            handler();
//        } else {
//            // 기본적으로 'not found' 처리
//            this.routes['/404'] && this.routes['/404']();
//        }
//
//        // 여기서 상태 업데이트 추가
//        this.updateTagLoadingStateFromURL() // 뷰 업데이트 보다 먼저 해야함.
//        this.updateStateFromURL();
//    }
//
//    //태그 첫 로딩 초기화
//    updateTagLoadingStateFromURL(){
//        const viewState = globalStateManager.getState().currentView;
//        const path = window.location.pathname;
//
//        if(viewState !== path){
//            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: true});
//            // alert("globalStateManager.getState().isFirstTagLoad : "+globalStateManager.getState().isFirstTagLoad);
//        }
//    }
//
//    //상태 업데이트
//    updateStateFromURL() {
//        // alert("!!! 상태 업데이트 !!!");
//        const path = window.location.pathname; // 예: "/sounds/tracks/one"
//
//        // const segments = path.split('/').filter(Boolean); // 빈 문자열 제거
//        // const firstSegment = segments[0]; // 첫 번째 요소 가져오기
//
//        // alert("segments : "+segments);
//        // alert("firstSegment : "+firstSegment);
//        // 필요하면 상태 업데이트 로직에 활용
//        globalStateManager.dispatch({ type: 'SET_VIEW', payload: path });
//    }
//
//    // 라우터 시작
//    start() {
//        this.handleRouteChange();
//    }
//
//    // 경로 변경
//    navigate(path) {
//        // alert("경로 핸들링");
//        window.history.pushState({}, '', path);
//        this.handleRouteChange();
//    }
//}
//
//export const router = new Router();
//
//document.addEventListener('DOMContentLoaded', () => {
//
//
//    router.addRoute('/admin/tracks',async () => {
//        removeATypeCSS();
//        loadBTypeCSS();
//        const queryParams = window.location.search;
//        const response = await axiosGet({endpoint : `/api/admin/tracks${queryParams}`});
//        await renderArtistsTracks(response);
//        await renderPagination(response);
//        renderSearch();
//    });
//
//    router.addRoute('/admin/albums',async () => {
//        removeATypeCSS();
//        loadBTypeCSS();
//        const queryParams = window.location.search;
//        const response = await axiosGet({endpoint : `/api/admin/albums${queryParams}`});
//        renderArtistsAlbums(response);
//        renderPagination(response);
//        renderSearch();
//    });
//
//    router.addRoute('/admin/albums/verify',async () => {
//        // alert("혹시 이게 호출되는건가?");
//        removeATypeCSS();
//        loadBTypeCSS();
//        const queryParams = window.location.search;
//        const response = await axiosGet({endpoint : `/api/admin/albums/verify${queryParams}`});
//        await renderArtistsVerify(response);
//        renderPagination(response);
//        // renderSearch();
//    });
//
//    router.addRoute('/admin/tags/spelling',async () => {
//        removeATypeCSS();
//        loadBTypeCSS();
//        const queryParams = window.location.search;
//        const renderTags = await axiosGet({endpoint: `/api/sounds/tags${queryParams}`});
//        renderTagsSpelling(renderTags);
//        renderPagination();
//    });
//
//    router.addRoute('/admin/tags/new',async()=>{
//        removeATypeCSS();
//        loadBTypeCSS();
//        renderTagsNew();
//    });
//
//    router.addRoute(`/admin/albums/one/verify`, async()=>{
//        // alert("원하는 라우터 호출 !");
//        await removeBTypeCSS();
//        await loadATypeCSS();
//
//        const queryParams = new URLSearchParams(window.location.search);
//        const albumId = queryParams.get('id');
//        const userId = queryParams.get('uid');
//
//        const response = await axiosGet({endpoint: `/api/admin/albums/${userId}/title/${albumId}/verify`});
//        await renderArtistsVerifyOne(response);
//        await renderTotalSoundsVerify(response);
//        await renderPagination(response);
//
//        if (globalStateManager.getState().isFirstTagLoad || compareTagsWithUrlParams()) {
//            const tagsBody = {dto: response.dtoList};
//            const renderTags = await axiosPost({endpoint: '/api/sounds/tags', body: tagsBody});
//
//            renderTagsFromSearch(renderTags);
//            extractTagsFromURL(); // 태그 상태 업데이트
//
//            // 최초 호출 이후에는 상태를 false로 변경
//            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: false});
//        }
//    });
//
//    router.addRoute(`/admin/sounds`, async ()=>{
//        removeATypeCSS();
//        loadBTypeCSS();
//        await renderSoundsAdminMain();
//    });
//
//    router.start();
//});
//
//
