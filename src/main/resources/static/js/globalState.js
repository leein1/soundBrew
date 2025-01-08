// 전역 상태 객체 (상태는 직접 수정하지 않음, 초기 상태만 정의)
const initialState = {
    currentView: 'tracks',  // 'tracks' 또는 'albums'
    isLoggedIn: false,      // 로그인 여부
};

export let globalState = { ...initialState };

// [Action]
// 액션은 상태 변경을 요청하는 객체
export const actions = {
    SET_VIEW: 'SET_VIEW',           // 뷰 전환 액션
    SET_LOGIN_STATUS: 'SET_LOGIN_STATUS', // 로그인 상태 변경 액션
};

// [Reducer]
// 리듀서는 상태를 변경하는 순수 함수
function reducer(state = initialState, action) {
    switch (action.type) {
        case actions.SET_VIEW:
            return { ...state, currentView: action.payload }; // 새로운 상태 반환
        case actions.SET_LOGIN_STATUS:
            return { ...state, isLoggedIn: action.payload }; // 로그인 상태 변경
        default:
            return state; // 변경되지 않은 상태 반환
    }
}

// [Dispatch]
// 액션을 받아 리듀서를 통해 상태를 변경
export function dispatch(action) {
    const newState = reducer(globalState, action);

    // 새로운 상태를 글로벌 상태에 반영
    globalState = newState; // globalState를 직접 수정하지 않고 새 상태로 교체

    // 상태 변경 후 렌더링 호출
}

// [Render]
// 상태에 따라 화면을 업데이트
// function render() {
//     const view = globalState.currentView;
//     const isLoggedIn = globalState.isLoggedIn;
//
//     const app = document.getElementById('app');
//     if (!app) return;
//
//     app.innerHTML = ''; // 기존 화면 초기화
//
//     if (isLoggedIn) {
//         // 로그인 되어 있을 때
//         if (view === 'tracks') {
//             app.innerHTML = `<h1>Tracks View</h1><p>Welcome! You are logged in.</p>`;
//         } else if (view === 'albums') {
//             app.innerHTML = `<h1>Albums View</h1><p>Welcome! You are logged in.</p>`;
//         }
//     } else {
//         // 로그인되지 않았을 때
//         app.innerHTML = `<h1>Please Log In</h1>`;
//     }
// }

// 예제: 뷰 앨범/트랙 버튼
// document.getElementById('toTracks').addEventListener('click', () => {
//     dispatch({ type: actions.SET_VIEW, payload: 'tracks' });
// });

// 예제 : 로그인/로그아웃 버튼
// document.getElementById('login').addEventListener('click', () => {
//     dispatch({ type: actions.SET_LOGIN_STATUS, payload: true });
// });
