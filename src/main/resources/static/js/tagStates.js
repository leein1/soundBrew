// tagStates.js
const tagStates = {
    piano: false,
    violin: false,
    happy: false,
    sad: false,
    exciting: false,
    classic: false,
    jazz: false,
    rock: false
};

// 태그 상태를 관리하는 함수나 메소드도 추가할 수 있습니다.
function toggleTagState(tag) {
    tagStates[tag] = !tagStates[tag];
}

// ==========
//  upload tag list

const tags = {
    instrument: ['Piano', 'Violin', 'Guitar', 'Drums'],
    mood: ['Happy', 'Sad', 'Energetic', 'Calm'],
    genre: ['Pop', 'Rock', 'Classical', 'Jazz']
};