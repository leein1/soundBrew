import {renderViewType} from "/js/sound/viewType.js";
import {renderTagsFromSearch} from "/js/sound/soundTagsModule.js";
import {renderSort} from "/js/sound/sort.js";

export class GlobalState {
    constructor() {
        // 초기 상태 정의
        this.state = {
            currentView: '/tracks',  // 'tracks' 또는 'albums'
            isLoggedIn: false,      // 로그인 여부
            instrumentTags: [],
            moodTags: [],
            genreTags: [],
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
const actions = {
    SET_VIEW: 'SET_VIEW',
    SET_LOGIN_STATUS: 'SET_LOGIN_STATUS',
    SET_INSTRUMENT_TAGS: 'SET_INSTRUMENT_TAGS',
    SET_MOOD_TAGS: 'SET_MOOD_TAGS',
    SET_GENRE_TAGS: 'SET_GENRE_TAGS',
};

// 뷰 전환에 따른 서치바 처리 함수
function displaySearchBar(state) {
    // alert("displaySearchBar");
    let searchItem = document.querySelector('.search-sort');

    if (!searchItem) {
        renderSearch();
    }else if (state.currentView === '/tracks') {
        searchItem.style.display = 'block'; //보임
    }else if (state.currentView === '/albums') {
        searchItem.style.display = 'block'; //보임
    }else if (state.currentView === '/tracks/one'){
        searchItem.style.display = 'none'; // 한개 트랙에는 안보임
    }else if(state.currentView === '/albums/one'){
        searchItem.style.display = 'none'; // 한개 앨범에는 안보임
    }
}

function displaySortBar(state){
    // alert("displaySortBar");
    let sortItem = document.querySelector('.music-sort');

    if (!sortItem) {
        renderSort();
    }else if (state.currentView === '/tracks') { // 트랙 리스트일때
        sortItem.style.display = 'block'; //보임
    }else if (state.currentView === '/albums') { // 앨범 리스트일때
        sortItem.style.display = 'block'; //보임
    }else if (state.currentView === '/tracks/one'){ // 트랙 1개일때
        sortItem.style.display = 'none'; // 안보임
    }else if(state.currentView === '/albums/one'){ // 앨범 1개일때
        sortItem.style.display = 'block'; //보임
    }
}

function displayViewTypeBar(state){
    // alert("viewTypeBar : "+state.currentView);
    let viewTypeItem = document.querySelector('.view-type');

    if (!viewTypeItem) {
        renderViewType();
    } else if (state.currentView === '/tracks') { // 트랙 리스트일때
        // alert("viewTypeBar!!!");
        viewTypeItem.style.display = 'block'; // 보임
    }else if (state.currentView === '/albums') { // 앨범 리스트일때
        viewTypeItem.style.display = 'block'; // 보임
    }else if (state.currentView === '/tracks/one'){ // 트랙 1개일때
        viewTypeItem.style.display = 'none'; // 한개 트랙에는 안보임
    }else if(state.currentView === '/albums/one'){ // 앨범 1개일때
        viewTypeItem.style.display = 'none'; // 안보임
    }
}

function displayTagsBar(state){
    // alert("displayTagsBar");
    let tagsBarItem = document.querySelector('.music-tag-sort');
    // alert("tagsBarItem : "+tagsBarItem);
    if(tagsBarItem){
        if (state.currentView === '/tracks') { // 트랙 리스트일때
            tagsBarItem.style.display = 'block'; // 보임
        }else if (state.currentView === '/albums') { // 앨범 리스트일때
            tagsBarItem.style.display = 'block'; // 보임
        }else if (state.currentView === '/tracks/one'){ // 트랙 1개일때
            tagsBarItem.style.display = 'none'; // 한개 트랙에는 안보임
        }else if(state.currentView === '/albums/one'){ // 앨범 1개일때
            tagsBarItem.style.display = 'block'; // 보임
        }
    }
}

function displayAlbumInfoBar(state){
    let albumInfoBar = document.querySelector('.render-album-info-container');
    // alert("tagsBarItem : "+tagsBarItem);
    if(albumInfoBar){
        if (state.currentView === '/tracks') { // 트랙 리스트일때
            albumInfoBar.innerHTML = ''; // 보임
        }else if (state.currentView === '/albums') { // 앨범 리스트일때
            albumInfoBar.innerHTML = ''; // 보임
        }else if (state.currentView === '/tracks/one'){ // 트랙 1개일때
            albumInfoBar.innerHTML = ''; // 보임
        }else if(state.currentView === '/albums/one'){ // 앨범 1개일때
            console.log("album-info");
        }
    }
}

// 구독 설정
globalStateManager.subscribe('currentView', displaySearchBar);
globalStateManager.subscribe('currentView', displaySortBar);
globalStateManager.subscribe('currentView', displayViewTypeBar);
globalStateManager.subscribe('currentView', displayTagsBar);
globalStateManager.subscribe('currentView', displayAlbumInfoBar);

// 최초 로드 시, dispatch로 상태 설정
globalStateManager.dispatch({
    type: 'SET_VIEW',
    payload: 'tracks'  // 기본 뷰 설정
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


// [URL 파라미터에서 태그 추출]
export function extractTagsFromURL() {
    const urlParams = new URLSearchParams(window.location.search);

    const instrumentTags = urlParams.has('more[instrument]') ? urlParams.get('more[instrument]').split(',') : [];
    const moodTags = urlParams.has('more[mood]') ? urlParams.get('more[mood]').split(',') : [];
    const genreTags = urlParams.has('more[genre]') ? urlParams.get('more[genre]').split(',') : [];

    // 추출한 태그들을 상태에 반영
    globalStateManager.dispatch({ type: actions.SET_INSTRUMENT_TAGS, payload: instrumentTags });
    globalStateManager.dispatch({ type: actions.SET_MOOD_TAGS, payload: moodTags });
    globalStateManager.dispatch({ type: actions.SET_GENRE_TAGS, payload: genreTags });
}

// 상태와 URL 파라미터 비교 함수
export function compareTagsWithUrlParams() {
    const urlParams = new URLSearchParams(window.location.search);
    const instrumentTags = urlParams.has('more[instrument]') ? urlParams.get('more[instrument]').split(',') : [];
    const moodTags = urlParams.has('more[mood]') ? urlParams.get('more[mood]').split(',') : [];
    const genreTags = urlParams.has('more[genre]') ? urlParams.get('more[genre]').split(',') : [];

    const currentInstrumentTags = globalStateManager.getState().instrumentTags;
    const currentMoodTags = globalStateManager.getState().moodTags;
    const currentGenreTags = globalStateManager.getState().genreTags;

    console.log("현재 상태에 올라온 태그 : "+currentInstrumentTags+currentMoodTags+currentGenreTags);
    console.log("지금 파라미터에 있는 태그 : "+instrumentTags+moodTags+genreTags);
    // 각각의 태그 비교
    const isInstrumentChanged = !arraysAreEqual(currentInstrumentTags, instrumentTags);
    const isMoodChanged = !arraysAreEqual(currentMoodTags, moodTags);
    const isGenreChanged = !arraysAreEqual(currentGenreTags, genreTags);

    // 하나라도 달라지면 true 반환
    return isInstrumentChanged || isMoodChanged || isGenreChanged;
}

// 배열 비교 함수
function arraysAreEqual(arr1, arr2) {
    if (arr1.length !== arr2.length) return false;
    for (let i = 0; i < arr1.length; i++) {
        if (arr1[i] !== arr2[i]) return false;
    }
    return true;
}
