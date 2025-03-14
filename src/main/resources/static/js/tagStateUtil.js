import {globalStateManager, actions} from "/js/globalState.js";

export function extractTagsFromURL() {
    const urlParams = new URLSearchParams(window.location.search);

    const instrumentTags = urlParams.has('more[instrument]') ? urlParams.get('more[instrument]').split(',') : [];
    const moodTags = urlParams.has('more[mood]') ? urlParams.get('more[mood]').split(',') : [];
    const genreTags = urlParams.has('more[genre]') ? urlParams.get('more[genre]').split(',') : [];

    // 추출한 태그들을 상태에 반영
    globalStateManager.dispatch({type: actions.SET_INSTRUMENT_TAGS, payload: instrumentTags});
    globalStateManager.dispatch({type: actions.SET_MOOD_TAGS, payload: moodTags});
    globalStateManager.dispatch({type: actions.SET_GENRE_TAGS, payload: genreTags});
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

    console.log("현재 상태에 올라온 태그 : " + currentInstrumentTags + currentMoodTags + currentGenreTags);
    console.log("지금 파라미터에 있는 태그 : " + instrumentTags + moodTags + genreTags);
    // 각각의 태그 비교
    const isInstrumentChanged = !arraysAreEqual(currentInstrumentTags, instrumentTags);
    const isMoodChanged = !arraysAreEqual(currentMoodTags, moodTags);
    const isGenreChanged = !arraysAreEqual(currentGenreTags, genreTags);

    // 하나라도 달라지면 true 반환
    return isInstrumentChanged || isMoodChanged || isGenreChanged;
}

// 배열 비교 함수
export function arraysAreEqual(arr1, arr2) {
    if (arr1.length !== arr2.length) return false;
    for (let i = 0; i < arr1.length; i++) {
        if (arr1[i] !== arr2[i]) return false;
    }
    return true;
}