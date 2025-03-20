import TokenUtil from '/js/tokenUtil.js';

export class GlobalState {
    constructor() {
        // 초기 상태 정의
        this.state = {
            currentView: '/tracks',  // 'tracks' 또는 'albums'
            isLoggedIn: false,      // 로그인 여부
            instrumentTags: [],
            moodTags: [],
            genreTags: [],
            isFirstTagLoad:true,
            isRole: ['visitor'],
        };

        // Pub/Sub 관련
        this.subscribers = {};
    }

    // 상태 변경
    setState(newState) {
        this.state = { ...this.state, ...newState };

        // 변경된 상태에 맞는 구독자에게 알림
        Object.keys(newState).forEach(key => {
            if (this.subscribers[key]) {
                this.subscribers[key].forEach(callback => callback(this.state));
            }
        });
    }

    // 상태 조회
    getState() {
        return this.state;
    }

    // 액션 디스패치
    dispatch(action) {
        switch (action.type) {
            case 'SET_VIEW':
                // alert("dispatch!");
                this.setState({ currentView: action.payload });
                break;
            case 'SET_LOGIN_STATUS':
                this.setState({ isLoggedIn: action.payload });
                break;
            case 'SET_INSTRUMENT_TAGS':
                this.setState({ instrumentTags: action.payload });
                break;
            case 'SET_MOOD_TAGS':
                this.setState({ moodTags: action.payload });
                break;
            case 'SET_GENRE_TAGS':
                this.setState({ genreTags: action.payload });
                break;
            case 'SET_TAG_LOAD_STATUS':
                this.setState(({ isFirstTagLoad: action.payload}));
                break;
            case 'SET_IS_ROLE':
                this.setState({ isRole: action.payload });
            default:
                break;
        }
    }

    // 구독
    subscribe(eventName, callback) {
        if (!this.subscribers[eventName]) {
            this.subscribers[eventName] = [];
        }
        this.subscribers[eventName].push(callback);
    }

    // 구독 취소
    unsubscribe(eventName, callback) {
        if (!this.subscribers[eventName]) return;
        this.subscribers[eventName] = this.subscribers[eventName].filter(sub => sub !== callback);
    }
}

// GlobalState 인스턴스 생성
export const globalStateManager = new GlobalState();

// 액션 정의
// document.addEventListener('DOMContentLoaded', () => {
export const actions = {
    SET_VIEW: 'SET_VIEW',
    SET_LOGIN_STATUS: 'SET_LOGIN_STATUS',
    SET_INSTRUMENT_TAGS: 'SET_INSTRUMENT_TAGS',
    SET_MOOD_TAGS: 'SET_MOOD_TAGS',
    SET_GENRE_TAGS: 'SET_GENRE_TAGS',
    SET_TAG_LOAD_STATUS:'SET_TAG_LOAD_STATUS',
    SET_IS_ROLE: 'SET_IS_ROLE',
};

function displaySearchBar(state) {
    const stateActions = {
        '/': 'block',
        //sounds
        '/sounds/tracks': 'block',
        '/sounds/albums': 'block',
        '/sounds/tracks/one': 'none',
        '/sounds/albums/one': 'none',
        //me
        '/me/info':'none',
        '/me/change-password':'none',
        '/me/subscription':'none',
        '/subscription':'none',
        '/me/statistic':'none',
        '/me/sounds/upload':'none',
        '/me/sounds/albums':'block',
        '/me/sounds/tracks':'block',
        '/me/sounds/tags':'block',
        //admin
        '/admin/tracks':'block',
        '/admin/albums':'block',
        '/admin/albums/verify':'none',
        '/admin/tags/spelling':'none',
        '/admin/tags/new':'none',
        '/admin/albums/one/verify':'none',
        '/admin/sounds':'none',
        '/admin/users':'block',
        '/admin/statistic':'none'
    };

    const searchItem = document.querySelector('.search-sort');
    const displayStyle = stateActions[state.currentView];

    if(searchItem) {
        if (displayStyle !== undefined) {
            searchItem.style.display = displayStyle;
        }
    }
}

function displaySortBar(state){
    // alert("displaySortBar");
    const stateActions={
        '/': 'block',
        '/sounds/tracks': 'block',
        '/sounds/albums': 'block',
        '/sounds/tracks/one': 'none',
        '/sounds/albums/one': 'block',
        //me
        '/me/info':'none',
        '/me/change-password':'none',
        '/me/subscription':'none',
        '/subscription':'none',
        '/me/statistic':'none',
        '/me/sounds/upload':'none',
        '/me/sounds/albums':'none',
        '/me/sounds/tracks':'none',
        '/me/sounds/tags':'none',
        //admin
        '/admin':'none',
        '/admin/tracks':'none',
        '/admin/albums':'none',
        '/admin/albums/verify':'none',
        '/admin/tags/spelling':'none',
        '/admin/tags/new':'none',
        '/admin/albums/one/verify':'none',
        '/admin/users':'none',
        '/admin/statistic':'none'
    };

    let sortItem = document.querySelector('.music-sort');
    const displayStyle = stateActions[state.currentView];

    if(sortItem) {
        if (displayStyle !== undefined) {
            sortItem.style.display = displayStyle;
        }
    }
}

function displayViewTypeBar(state){
    // alert("viewTypeBar : "+state.currentView);
    const stateActions={
        '/': 'block',
        '/sounds/tracks': 'block',
        '/sounds/albums': 'block',
        '/sounds/tracks/one': 'none',
        '/sounds/albums/one': 'none',
        //me
        '/me/info':'none',
                '/me/change-password':'none',
                '/me/subscription':'none',
                '/subscription':'none',
        '/me/statistic':'none',
        '/me/sounds/upload':'none',
        '/me/sounds/albums':'none',
        '/me/sounds/tracks':'none',
        '/me/sounds/tags':'none',
        //admin
        '/admin':'none',
        '/admin/tracks':'none',
        '/admin/albums':'none',
        '/admin/albums/verify':'none',
        '/admin/tags/spelling':'none',
        '/admin/tags/new':'none',
        '/admin/albums/one/verify':'none',
        '/admin/users':'none',
        '/admin/statistic':'none'
    };

    let viewTypeItem = document.querySelector('.view-type');
    const displayStyle = stateActions[state.currentView];

    if(viewTypeItem) {
        if (displayStyle !== undefined) {
            viewTypeItem.style.display = displayStyle;
        }
    }
}

function displayTagsBar(state){
    // alert("displayTagsBar");
    const stateActions={
        '/': 'block',
        '/sounds/tracks': 'block',
        '/sounds/albums': 'block',
        '/sounds/tracks/one': 'none',
        '/sounds/albums/one': 'block',
        //me
        '/me/info':'none',
                '/me/change-password':'none',
                '/me/subscription':'none',
                '/subscription':'none',
        '/me/statistic':'none',
        '/me/sounds/upload':'none',
        '/me/sounds/albums':'none',
        '/me/sounds/tracks':'none',
        '/me/sounds/tags':'none',
        //admin
        '/admin':'none',
        '/admin/tracks':'none',
        '/admin/albums':'none',
        '/admin/albums/verify':'none',
        '/admin/tags/spelling':'none',
        '/admin/tags/new':'none',
        '/admin/albums/one/verify':'block',
        '/admin/users':'none',
        '/admin/statistic':'none'
    };

    let tagsBarItem = document.querySelector('.music-tag-sort');
    const displayStyle = stateActions[state.currentView];

    if(tagsBarItem) {
        if (displayStyle !== undefined) {
            tagsBarItem.style.display = displayStyle;
        }
    }
}

function displayPaginationBar(state){
    // 페이지 바에 대해서, 가리거나 등등.
    const stateActions = {
        '/': 'block',
        //sounds
        '/sounds/tracks': 'block',
        '/sounds/albums': 'block',
        '/sounds/tracks/one': 'none',
        '/sounds/albums/one': 'block',
        //me
        '/me/info':'none',
                '/me/change-password':'none',
                '/me/subscription':'none',
                '/subscription':'none',
        '/me/statistic':'none',
        '/me/sounds/upload':'none',
        '/me/sounds/albums':'block',
        '/me/sounds/tracks':'block',
        '/me/sounds/tags':'block',
        //admin
        '/admin/tracks':'block',
        '/admin/albums':'block',
        '/admin/albums/verify':'block',
        '/admin/tags/spelling':'none',
        '/admin/tags/new':'none',
        '/admin/albums/one/verify':'block',
        '/admin/sounds':'none',
        '/admin/users' : 'block',
        '/admin/statistic':'none'
    };

    const pageItem = document.querySelector('.pagination-container');
    const displayStyle = stateActions[state.currentView];

    if(pageItem) {
        if (displayStyle !== undefined) {
            pageItem.style.display = displayStyle;
        }
    }
}

function displayAudioBar(state){
    const stateActions = {
        '/': 'block',
        //sounds
        '/sounds/tracks': 'block',
        '/sounds/albums': 'block',
        '/sounds/tracks/one': 'none',
        '/sounds/albums/one': 'block',
        //me
        '/me/info':'none',
                '/me/change-password':'none',
                '/me/subscription':'none',
                '/subscription':'none',
        '/me/statistic':'none',
        '/me/sounds/upload':'none',
        '/me/sounds/albums':'none',
        '/me/sounds/tracks':'none',
        '/me/sounds/tags':'none',
        //admin
        '/admin/sounds':'none',
        '/admin/tracks':'none',
        '/admin/albums':'none',
        '/admin/albums/verify':'none',
        '/admin/tags/spelling':'none',
        '/admin/tags/new':'none',
        '/admin/albums/one/verify':'block',
        '/admin/statistic':'none'
    };

    let albumInfoBar = document.querySelector('.audio-player-bar');
    const displayAudio = stateActions[state.currentView];

    if(albumInfoBar) {
        if (displayAudio !== undefined) {
            albumInfoBar.style.display = displayAudio;
        }
    }
}

function displayAlbumInfoBar(state){
    const stateActions={
        '/': 'none',
        '/sounds/tracks': 'none',
        '/sounds/albums': 'none',
        '/sounds/tracks/one': 'none',
         '/sounds/albums/one': 'block',
        //me
        '/me/info':'none',
        '/me/change-password':'none',
        '/me/subscription':'none',
        '/subscription':'none',
        '/me/statistic':'none',
        '/me/sounds/upload':'none',
        '/me/sounds/albums':'none',
        '/me/sounds/tracks':'none',
        '/me/sounds/tags':'none',
        //admin
        '/admin/tracks':'none',
        '/admin/albums':'none',
        '/admin/albums/verify':'none',
        '/admin/tags/spelling':'none',
        '/admin/tags/new':'none',
        '/admin/albums/one/verify':'block',
        '/admin/statistic':'none'
    };

    let albumInfoBar = document.querySelector('.render-album-info-container');
    const displayStyle = stateActions[state.currentView];

    if(albumInfoBar){
        if(displayStyle !== undefined) {
            albumInfoBar.style.display= displayStyle;
        }
    }
}

// 구독 설정
globalStateManager.subscribe('currentView', displayAudioBar);
globalStateManager.subscribe('currentView', displaySearchBar);
globalStateManager.subscribe('currentView', displaySortBar);
globalStateManager.subscribe('currentView', displayViewTypeBar);
globalStateManager.subscribe('currentView', displayTagsBar);
globalStateManager.subscribe('currentView', displayAlbumInfoBar);
globalStateManager.subscribe('currentView', displayPaginationBar);

// 최초 로드 시, dispatch로 상태 설정
globalStateManager.dispatch({
    type: 'SET_VIEW',
    payload: 'manage'  // 기본 뷰 설정
});

globalStateManager.dispatch({
    type: 'SET_LOGIN_STATUS',
    payload: false  // 로그인 상태 설정
});

globalStateManager.dispatch({
    type: 'SET_INSTRUMENT_TAGS',
    payload: []  // 초기 instrumentTags 상태 설정
});

globalStateManager.dispatch({
    type: 'SET_MOOD_TAGS',
    payload: []  // 초기 moodTags 상태 설정
});

globalStateManager.dispatch({
    type: 'SET_GENRE_TAGS',
    payload: []  // 초기 genreTags 상태 설정
});

const token = TokenUtil.getToken();
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
    globalStateManager.dispatch({ type: 'SET_LOGIN_STATUS', payload: false });
    globalStateManager.dispatch({ type: 'SET_IS_ROLE', payload: ['visitor'] });
}
