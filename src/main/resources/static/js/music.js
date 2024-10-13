// showTags 함수 정의
window.showTags = function() {
    const selectedTags = Object.keys(tagStates).filter(tag => tagStates[tag]);
    alert(`선택된 태그: ${selectedTags.join(', ')}`);
};