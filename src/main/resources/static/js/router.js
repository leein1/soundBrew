import {axiosGet,axiosPost, callRefresh} from '/js/fetch/standardAxios.js';
import { extractTagsFromURL,compareTagsWithUrlParams } from "/js/tagStateUtil.js";
import {renderTotalSounds,renderTotalAlbums,renderSoundOne,renderAlbumOne} from '/js/sound/sound.js';
import { renderMyInfo } from '/js/user/myInfo.js';
import { renderChangePassword } from '/js/user/changepw.js';
import { renderMySubscription } from '/js/user/mySubscription.js';
import { renderSubscriptionPlans } from '/js/user/subscriptionPlan.js';
import {renderPagination} from "/js/pagination.js";
import {renderSort} from "/js/sound/sort.js";
import {renderTagsFromSearch} from "/js/sound/soundTagsModule.js";
import {renderViewType} from "/js/sound/viewType.js";
import {globalStateManager} from "/js/globalState.js";
import {renderMeAlbums, renderMeTracks, renderMeTags,renderSoundUpload } from "/js/sound/soundManage.js";
import {renderArtistsTracks,renderArtistsAlbums,renderTagsSpelling,renderTagsNew,renderArtistsVerify, renderArtistsVerifyOne,renderTotalSoundsVerify} from "/js/sound/soundAdmin.js";
import { loadSoundTypeCSS, loadSoundManageTypeCSS, updateDynamicCSS , SoundTypeCSSFiles, SoundManageTypeCSSFiles, UserAdminTypeCSSFiles, loadUserAdminTypeCSS, removeAllDynamicCSS,AdminStatisticTypeCSSFiles,loadAdminStatisticTypeCSS,loadSoundManageMainTypeCSS,SoundManageMainTypeCSSFiles} from '/js/CSSLoader.js';
import {renderUserInfoWithRole} from '/js/user/userAdmin.js';
import {renderSubscriptionInfo } from '/js/user/subscriptionAdmin.js';
import {initDashboard} from '/js/user/dashboard.js';
import {initMeDashboard} from '/js/user/meDashboard.js';
import {renderIndex} from '/js/renderIndex.js';
import TokenUtil from '/js/tokenUtil.js';

export class Router {
    constructor() {
        // routes 객체는 각 경로에 대한 핸들러와 권한 정보를 저장합니다.
        this.routes = {};
        window.addEventListener('popstate', () => this.handleRouteChange());
    }

    addRoute(path, handler, allowedRoles = []) {
        this.routes[path] = {
            handler,
            allowedRoles,
        };
    }

    // 현재 경로에 맞는 핸들러 호출 및 권한 검사
    handleRouteChange() {
        const path = window.location.pathname;
        const routeInfo = this.routes[path];

        if (routeInfo) {
            // 권한 검사: 만약 사용자의 역할이 라우트의 allowedRoles에 포함되어 있지 않다면
            if (!this.checkPermission(routeInfo)) {
                this.callRefreshWithState(); // 토큰 갱신 시도
                if (!this.checkPermission(routeInfo)) { // 갱신 후에도 권한이 없으면 차단
                    alert("요청한 '"+ path +"'에 접근 권한이 없습니다.");
                    window.location.href='/sounds/tracks'; // 403 페이지로 이동 (필요에 따라 변경 가능)
                    return;
                }
            }
            // 권한이 있으면 해당 핸들러 호출
            routeInfo.handler();
        } else {
            // 등록되지 않은 경로인 경우 기본 404 처리
            this.routes['/404'] && this.routes['/404']();
        }

        // 뷰 업데이트 순서: 태그 로딩 상태 업데이트 → 뷰 상태 업데이트
        this.updateTagLoadingStateFromURL();
        this.updateStateFromURL();
    }

    async callRefreshWithState(){
        const token = await callRefresh();
        if (token) {
            const userInfo = TokenUtil.getUserInfo(token);
            if (userInfo && userInfo.roles && userInfo.roles.length > 0) {
                globalStateManager.dispatch({ type: 'SET_LOGIN_STATUS', payload: true });
                globalStateManager.dispatch({ type: 'SET_IS_ROLE', payload: userInfo.roles });
            } else {
                globalStateManager.dispatch({ type: 'SET_LOGIN_STATUS', payload: false });
                globalStateManager.dispatch({ type: 'SET_IS_ROLE', payload: ['visitor'] });
            }
        } else {
            alert("권한에서 문제가 발생해도 이 얼럿이 뜨면 안됨(이전에 잡아졌어야함)");
        }
    }

    // 현재 사용자가 해당 라우트에 접근할 수 있는지 체크
    checkPermission(routeInfo) {
        if (!routeInfo.allowedRoles || routeInfo.allowedRoles.length === 0) {
            return true;
        }
        const userRoles = globalStateManager.getState().isRole; // 유저 역할 (배열)

        console.log("User Roles:", userRoles);
        console.log("Allowed Roles:", routeInfo.allowedRoles);

        return userRoles.some(role => routeInfo.allowedRoles.includes(role));
    }

    // 상태 업데이트: URL에 따른 현재 뷰 업데이트
    updateStateFromURL() {
        const path = window.location.pathname;
        globalStateManager.dispatch({ type: 'SET_VIEW', payload: path });
    }

    // 태그 첫 로딩 상태 업데이트: URL이 바뀌었을 때만 로딩 상태를 변경
    updateTagLoadingStateFromURL(){
        const viewState = globalStateManager.getState().currentView;
        const path = window.location.pathname;
        if (viewState !== path){
            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: true });
        }
    }

    // 라우터 시작 (최초 경로에 맞게 핸들러 실행)
    start() {
        this.handleRouteChange();
    }

    // 경로 변경 시 history에 push 및 라우트 처리
    navigate(path) {
        window.history.pushState({}, '', path);
        this.handleRouteChange();
    }
}

export const router = new Router();

document.addEventListener('DOMContentLoaded', async () => {
    router.addRoute('/', async()=>{
        updateDynamicCSS(SoundTypeCSSFiles);
        await loadSoundTypeCSS();

        const queryParams = window.location.search; // 현재 URL 쿼리 파라미터 가져오기

        // 초기 로딩이거나, 태그가 변경된 경우 API 호출
        if (globalStateManager.getState().isFirstTagLoad || compareTagsWithUrlParams()) {
            renderSearch();
            renderSort();
            renderViewType();

            // 태그 API 호출
            const renderTags = await axiosGet({ endpoint: `/api/sounds/tags/mapped${queryParams}`});

            renderTagsFromSearch(renderTags); // 태그 UI 렌더링
            extractTagsFromURL(); // 태그 상태 업데이트

            // 최초 호출 이후에는 상태를 false로 변경
            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: false});
        }
        // 트랙 데이터를 항상 가져옴 (검색 결과는 항상 갱신해야 하므로)
        const response = await axiosGet({ endpoint: `/api/sounds/tracks${queryParams}`});

        console.log(response);
        renderTotalSounds(response.dtoList); // 트랙 리스트 렌더링
        renderPagination(response); // 페이지네이션 렌더링
    });

    router.addRoute('/subscription', async ()=>{
        //css 적용
        updateDynamicCSS(UserAdminTypeCSSFiles);
        await loadUserAdminTypeCSS();

        await renderSubscriptionPlans();
    });

    router.addRoute('/sounds/tracks', async () => {
        updateDynamicCSS(SoundTypeCSSFiles);
        await loadSoundTypeCSS();

        const queryParams = window.location.search; // 현재 URL 쿼리 파라미터 가져오기

        // 초기 로딩이거나, 태그가 변경된 경우 API 호출
        if (globalStateManager.getState().isFirstTagLoad || compareTagsWithUrlParams()) {
            renderSearch();
            renderSort();
            renderViewType();

            // 태그 API 호출
            const renderTags = await axiosGet({ endpoint: `/api/sounds/tags/mapped${queryParams}`});

            renderTagsFromSearch(renderTags); // 태그 UI 렌더링
            extractTagsFromURL(); // 태그 상태 업데이트

            // 최초 호출 이후에는 상태를 false로 변경
            globalStateManager.dispatch({ type : 'SET_TAG_LOAD_STATUS', payload: false});
        }
        // 트랙 데이터를 항상 가져옴 (검색 결과는 항상 갱신해야 하므로)
        const response = await axiosGet({ endpoint: `/api/sounds/tracks${queryParams}`});

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

    router.addRoute('/me/info' , async ()=>{
        //css 관련 로딩
        updateDynamicCSS(UserAdminTypeCSSFiles);
        await loadUserAdminTypeCSS();

        const response = await axiosGet({endpoint:"/api/me", useToken:true});
        await renderMyInfo(response.dto);
    }, ['ROLE_USER']);

    router.addRoute('/me/change-password', async ()=>{
        //css 관련 로딩
        updateDynamicCSS(UserAdminTypeCSSFiles);
        await loadUserAdminTypeCSS();

        await renderChangePassword();
    }, ['ROLE_USER']);

    router.addRoute('/me/subscription' , async ()=>{
        //css 관련 로딩
        updateDynamicCSS(UserAdminTypeCSSFiles);
        await loadUserAdminTypeCSS();

        await renderMySubscription();
    }, ['ROLE_USER']);

    router.addRoute('/me/sounds/upload',async () => {
        updateDynamicCSS(SoundManageMainTypeCSSFiles);
        await loadSoundManageMainTypeCSS();

        renderSoundUpload();
    }, ['ROLE_USER']);

    router.addRoute('/me/sounds/albums', async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint: `/api/me/albums${queryParams}`});
        console.log(response);
        renderSearch();
        await renderMeAlbums(response);
        await renderPagination(response);
    }, ['ROLE_USER']);

    router.addRoute('/me/sounds/tracks', async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint: `/api/me/tracks${queryParams}`});
        renderSearch();
        await renderMeTracks(response);
        renderPagination(response);
    }, ['ROLE_USER']);

    router.addRoute('/me/sounds/tags', async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint: `/api/me/tracks${queryParams}`});
        renderSearch();
        await renderMeTags(response);
        renderPagination(response);
    }, ['ROLE_USER']);

    router.addRoute('/me/statistic' , async () =>{
        updateDynamicCSS(AdminStatisticTypeCSSFiles);
        await loadAdminStatisticTypeCSS();

        const soundsStats = await axiosGet({ endpoint: '/api/statistic/sounds/stats/me' });
        const tagsStats = await axiosGet({endpoint:'/api/statistic/tags/stats/me'});
//        const subscriptionStats = await axiosGet({endpoint : '/api/statistic/subscription/stats/me'});

        await initMeDashboard(soundsStats,tagsStats);
    }, ['ROLE_USER']);

    router.addRoute('/admin/statistic' , async () =>{
        updateDynamicCSS(AdminStatisticTypeCSSFiles);
        await loadAdminStatisticTypeCSS();

        await initDashboard();
    }, ['ROLE_ADMIN']);

    router.addRoute('/admin/tracks',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint : `/api/admin/tracks${queryParams}`});
        await renderArtistsTracks(response);
        await renderPagination(response);
        renderSearch();
    }, ['ROLE_ADMIN']);

    router.addRoute('/admin/albums',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint : `/api/admin/albums${queryParams}`});
        renderArtistsAlbums(response);
        renderPagination(response);
        renderSearch();
    }, ['ROLE_ADMIN']);

    router.addRoute('/admin/albums/verify',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const response = await axiosGet({endpoint : `/api/admin/albums/verify${queryParams}`});
        await renderArtistsVerify(response);
        renderPagination(response);
        // renderSearch();
    }, ['ROLE_ADMIN']);

    router.addRoute('/admin/tags/spelling',async () => {
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        const queryParams = window.location.search;

        const renderTags = await axiosGet({endpoint: `/api/sounds/tags${queryParams}`});
        renderTagsSpelling(renderTags);
        renderPagination();
    }, ['ROLE_ADMIN']);

    router.addRoute('/admin/tags/new',async()=>{
        updateDynamicCSS(SoundManageTypeCSSFiles);
        await loadSoundManageTypeCSS();

        renderTagsNew();
    }, ['ROLE_ADMIN']);

    router.addRoute('/admin/albums/one/verify', async()=>{
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
    }, ['ROLE_ADMIN']);

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

    }, ['ROLE_ADMIN']);

    router.addRoute('/admin/subscription', async () =>{
        //css 로딩/삭제
        updateDynamicCSS(UserAdminTypeCSSFiles);
        await loadUserAdminTypeCSS();

        // 1. 엑시오스를 통한 특정 데이터 들고오기
        const response = await axiosGet({ endpoint:`/api/admin/subscription` });

        // 2. 이후 데이터(response)로 렌더링
        renderSubscriptionInfo(response);
    }, ['ROLE_ADMIN']);

    document.querySelector('#soundTracksRoute')?.addEventListener('click', () => {
        router.navigate('/sounds/tracks');
    });

    document.querySelector('#subscriptionListRoute')?.addEventListener('click', () => {
        router.navigate('/subscription');
    });

    document.querySelector('#mySoundRoute')?.addEventListener('click', () => {
        router.navigate('/me/sounds/albums');
    });

    document.querySelector('#mySoundStatisticRoute')?.addEventListener('click', () => {
        router.navigate('/me/statistic');
    });

    document.querySelector('#mySoundUploadRoute')?.addEventListener('click', () => {
        router.navigate('/me/sounds/upload');
    });

    document.querySelector('#myInfoRoute')?.addEventListener('click', () => {
        router.navigate('/me/info');
    });

    document.querySelector('#changePasswordRoute')?.addEventListener('click', () => {
        router.navigate('/me/change-password');
    });

    document.querySelector('#mySubscriptionRoute')?.addEventListener('click', () => {
        router.navigate('/me/subscription');
    });

    document.getElementById("adminStatisticRoute")?.addEventListener("click", () => {
        router.navigate("/admin/statistic");
    });

    document.getElementById("adminInfoRoute")?.addEventListener("click", () => {
        router.navigate("/admin/users");
    });

    document.getElementById("adminSoundRoute")?.addEventListener("click", () => {
        router.navigate("/admin/albums");
    });

    // 네비게이션 버튼들: location.href로 페이지를 새로고침 하듯 이동
    document.getElementById("sitename")?.addEventListener("click", () => {
        window.location.href = "/sounds/tracks";
    });

    document.getElementById("registerPage")?.addEventListener("click", () => {
        window.location.href = "/register";
    });

    document.querySelectorAll('.logoutPage')?.forEach(element => {
        element.addEventListener('click', function() {
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            window.location.href = "/logout";
        });
    });

    document.querySelectorAll('.loginPage')?.forEach(element => {
        element.addEventListener('click', function() {
            window.location.href = "/login";
        });
    });


router.start();

});