import { router } from '/js/router.js';
import { globalStateManager } from "/js/globalState.js";

export function renderTagsFromSearch(data, initialParams = {}) {
    const container = document.getElementById("render-tags-sort-container");
    container.innerHTML = ''; // Clear existing content

    const html = `
        <div class="music-tag-sort">
            <div class="music-tag-sort-list">
                <img src="/images/label_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="태그">
                <span class="music-tag-sort-toggle" data-type="instrument">악기 접기</span>

                <img src="/images/label_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="태그">
                <span class="music-tag-sort-toggle" data-type="mood">분위기 접기</span>

                <img src="/images/label_48dp_5F6368_FILL0_wght400_GRAD0_opsz48.svg" alt="태그">
                <span class="music-tag-sort-toggle" data-type="genre">장르 접기</span>

            </div>
            <div class="music-tag-display">
                <div class="tag-section " id="instrument-section">${data.dto.instrument.map(tag => createTagItem(tag, 'instrument')).join('')}</div>
                <div class="tag-section " id="mood-section">${data.dto.mood.map(tag => createTagItem(tag, 'mood')).join('')}</div>
                <div class="tag-section " id="genre-section">${data.dto.genre.map(tag => createTagItem(tag, 'genre')).join('')}</div>
            </div>
        </div>
    `;

    container.innerHTML = html;

    initializeActiveTags();

    Object.entries(initialParams).forEach(([type, value]) => {
        const tag = document.querySelector(`.tag-item[data-type="${type}"][data-value="${value}"]`);
        if (tag) activateTag(tag);
    });

    document.querySelectorAll('.music-tag-sort-toggle').forEach(toggle => {
        toggle.addEventListener('click', () => {
            const section = document.getElementById(`${toggle.dataset.type}-section`);
            if (section.classList.contains('hidden')) {
                // 열기 애니메이션 (fadeIn)
                section.classList.remove('hidden');
                section.classList.remove('fade-out');

                if (toggle.dataset.type === "instrument") {
                    toggle.textContent = "악기 접기";
                } else if (toggle.dataset.type === "mood") {
                    toggle.textContent = "분위기 접기";
                } else if (toggle.dataset.type === "genre") {
                    toggle.textContent = "장르 접기";
                }
            } else {
                // 닫기 애니메이션 (fadeOut)
                section.classList.add('fade-out');
                section.addEventListener('animationend', function handleAnimationEnd() {
                    section.classList.add('hidden'); // 애니메이션이 끝난 후 hidden 적용
                    section.classList.remove('fade-out'); // fade-out 클래스 제거
                    section.removeEventListener('animationend', handleAnimationEnd); // 이벤트 리스너 제거
                });

                if (toggle.dataset.type === "instrument") {
                    toggle.textContent = "악기 펼치기";
                } else if (toggle.dataset.type === "mood") {
                    toggle.textContent = "분위기 펼치기";
                } else if (toggle.dataset.type === "genre") {
                    toggle.textContent = "장르 펼치기";
                }
            }
        });
    });

    document.querySelectorAll('.tag-item').forEach(tag => {
        tag.addEventListener('click', async () => {
            const type = tag.dataset.type;
            const value = tag.dataset.value;

            if (tag.classList.contains('active')) {
                deactivateTag(tag);
                await performSearch({[type]: value}, true); // true로 비활성화 작업임을 전달
            } else {
                activateTag(tag);
                await performSearch({[type]: value});
            }
        });
    });

    function createTagItem(value, type) {
        return `<span class="tag-item" data-value="${value}" data-type="${type}">${value}</span>`;
    }

    // function createTagItem(value, type) {
    //     const darkModeClass = localStorage.getItem('darkMode') === 'enabled' ? ' dark-mode' : '';
    //     return `<span class="tag-item${darkModeClass}" data-value="${value}" data-type="${type}">${value}</span>`;
    // }

    function activateTag(tag) {
        tag.classList.add('active');
        tag.style.order = '-1';
    }

    function deactivateTag(tag) {
        tag.classList.remove('active');
        tag.style.order = 'initial';
    }

    // URL 파라미터를 확인하고 초기 활성화 상태 설정
    function initializeActiveTags() {
        const currentParams = new URLSearchParams(window.location.search);
        // console.log(currentParams);
        currentParams.forEach((value, key) => {
            const match = key.match(/^more\[(.+?)\]$/);
            if (match) {
                const type = match[1];
                value.split(',').forEach(tagValue => {
                    const tag = document.querySelector(`.tag-item[data-type="${type}"][data-value="${tagValue}"]`);
                    console.log(tag);
                    if (tag) activateTag(tag);
                });
            }
        });
    }

    async function performSearch(params, isDeactivation = false) {
        // 현재 URL의 쿼리 파라미터를 파싱
        const currentParams = new URLSearchParams(window.location.search);

        // 새로 추가된 파라미터 병합 또는 제거
        Object.entries(params).forEach(([key, value]) => {
            const paramKey = `more[${key}]`;

            if (isDeactivation) {
                // 비활성화 로직: 특정 value만 제거
                const existingValue = currentParams.get(paramKey);
                if (existingValue) {
                    const updatedValue = existingValue
                        .split(',') // 쉼표로 분리
                        .filter(v => v !== value) // 제거할 value 제외
                        .join(',');

                    if (updatedValue) {
                        currentParams.set(paramKey, updatedValue); // 값이 남아 있으면 업데이트
                    } else {
                        currentParams.delete(paramKey); // 값이 비어 있으면 전체 키 삭제
                    }
                }
            } else {
                // 활성화 로직: 새로운 value 추가
                if (value !== null) {
                    const existingValue = currentParams.get(paramKey);
                    if (existingValue) {
                        const updatedValue = existingValue.split(',').includes(value)
                            ? existingValue // 이미 존재하면 그대로 유지
                            : `${existingValue},${value}`; // 새로운 값 추가
                        currentParams.set(paramKey, updatedValue);
                    } else {
                        currentParams.set(paramKey, value);
                    }
                }
            }
        });

        currentParams.delete('page');

        const newQueryString = currentParams.toString();

        const newUrl = `${window.location.pathname}?${newQueryString}`;

        router.navigate(newUrl);
    }
}
