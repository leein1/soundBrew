document.addEventListener('DOMContentLoaded', function() {
    // 현재 URL 쿼리 스트링 가져오기
    const currentUrl = new URL(window.location.href);
    const searchParams = currentUrl.searchParams;  // 쿼리 파라미터 객체

    // 각 태그의 활성화 상태를 저장할 객체
    const tagStates = {
        instrument: {},
        mood: {},
        genre: {}
    };

    // URL에서 'instrument', 'mood', 'genre' 태그 파라미터를 확인하고 미리 설정
    ['instrument', 'mood', 'genre'].forEach(type => {
        if (searchParams.has(type)) {
            const tags = searchParams.get(type).split(',');  // URL 파라미터에서 태그들을 분리
            tags.forEach(tag => {
                tagStates[type][tag] = true;  // 각 태그를 활성화 상태로 설정
            });
        }
    });

    // 'music-tag-sort-toggle' 클래스가 붙은 요소들에 클릭 이벤트를 추가
    document.querySelectorAll('.music-tag-sort-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            const target = this.getAttribute('data-target');
            const sectionId = `${target}-section`;
            const displayArea = document.querySelector('.music-tag-display');
            const existingSection = document.getElementById(sectionId);

            // 기존 섹션이 존재하는 경우
            if (existingSection) {
                existingSection.classList.add('fade-out');
                existingSection.addEventListener('animationend', function() {
                    existingSection.remove();
                });
            } else {
                // 새로운 섹션을 열 때 기존의 섹션을 제거할 필요 없이 바로 생성
                const newSection = document.createElement('div');
                newSection.innerHTML = createTagSection(target);
                displayArea.appendChild(newSection);
            }
        });
    });

    // 태그 섹션을 생성하는 함수
    function createTagSection(target) {
        let sectionHTML = '';

        if (target === 'instrument') {
            const instrumentTags = musicTags.instTags.map(tag => `
                <span data-tag="${tag}" class="tag ${tagStates.instrument[tag] ? 'active' : ''}">${tag}</span>
            `).join('');

            sectionHTML = `
                <div class="tag-section" id="instrument-section">
                    ${instrumentTags}
                </div>
            `;
        } else if (target === 'mood') {
            const moodTags = musicTags.moodTags.map(tag => `
                <span data-tag="${tag}" class="tag ${tagStates.mood[tag] ? 'active' : ''}">${tag}</span>
            `).join('');

            sectionHTML = `
                <div class="tag-section" id="mood-section">
                    ${moodTags}
                </div>
            `;
        } else if (target === 'genre') {
            const genreTags = musicTags.genreTags.map(tag => `
                <span data-tag="${tag}" class="tag ${tagStates.genre[tag] ? 'active' : ''}">${tag}</span>
            `).join('');

            sectionHTML = `
                <div class="tag-section" id="genre-section">
                    ${genreTags}
                </div>
            `;
        }

        return sectionHTML;
    }

    document.querySelector('.music-tag-display').addEventListener('click', function(event) {
        if (event.target.classList.contains('tag')) {
            event.target.classList.toggle('active');
            const tagValue = event.target.getAttribute('data-tag');
            const sectionType = event.target.closest('.tag-section').id.split('-')[0];

            tagStates[sectionType][tagValue] = !tagStates[sectionType][tagValue];

            ['instrument', 'mood', 'genre'].forEach(type => {
                const activeTags = Object.keys(tagStates[type]).filter(tag => tagStates[type][tag]);

                if (activeTags.length > 0) {
                    searchParams.set(type, activeTags.join(','));
                } else {
                    searchParams.delete(type);
                }
            });

            const newUrl = currentUrl.origin + currentUrl.pathname + '?' + searchParams.toString();
            window.location.href = newUrl;
        }
    });

    // 페이지 로드 시 URL에 태그 파라미터가 있으면 해당 섹션을 미리 열어서 표시
    ['instrument', 'mood', 'genre'].forEach(type => {
        const toggle = document.querySelector(`[data-target="${type}"]`);
        if (toggle) {
            toggle.click();  // 해당 태그 섹션을 자동으로 열도록 클릭 이벤트 트리거
        }
    });
});
