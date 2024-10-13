// ** 태그를 사용한 정렬 기능 **
document.addEventListener('DOMContentLoaded', function() {
    function createTagSection(target) {
        
        let sectionHTML = '';
        // 'target' 값에 따라 HTML 문자열을 설정합니다.
        if (target === 'instrument') {
            // const tags = { instrument: ['piano', 'violin'] };
            // const tagStates = { piano: true, violin: false };
            const instrumentTags = tags.instrument.map(tag => `
                <span data-tag="${tag}" class="tag ${tagStates[tag] ? 'active' : ''}">${tag}</span>
            `).join('');
        
            sectionHTML = `
                <div class="tag-section" id="instrument-section">
                    ${instrumentTags}
                </div>
            `;
        } else if (target === 'mood') {
            const moodTags = tags.mood.map(tag => `
                <span data-tag="${tag}" class="tag ${tagStates[tag] ? 'active' : ''}">${tag}</span>
            `).join('');
        
            sectionHTML = `
                <div class="tag-section" id="mood-section">
                    ${moodTags}
                </div>
            `;
        } else if (target === 'genre') {
            const genreTags = tags.genre.map(tag => `
                <span data-tag="${tag}" class="tag ${tagStates[tag] ? 'active' : ''}">${tag}</span>
            `).join('');
        
            sectionHTML = `
                <div class="tag-section" id="genre-section">
                    ${genreTags}
                </div>
            `;
        } 
        return sectionHTML;
    }

    document.querySelectorAll('.music-tag-sort-toggle').forEach(function(toggle) {
        toggle.addEventListener('click', function() {
            const target = this.getAttribute('data-target');
            const sectionId = `${target}-section`;
            const displayArea = document.querySelector('.music-tag-display');
            const existingSection = document.getElementById(sectionId);

            if (existingSection) {
            // fade-out 애니메이션 추가
            existingSection.classList.add('fade-out');

            // 애니메이션이 끝난 뒤에 요소 삭제
            existingSection.addEventListener('animationend', function() {
                existingSection.remove();
            });
            } else {
                const newSection = document.createElement('div');
                newSection.innerHTML = createTagSection(target);
                displayArea.appendChild(newSection);
            }
        });
    });

    document.querySelector('.music-tag-display').addEventListener('click', function(event) {
        if (event.target.classList.contains('tag')) {
            event.target.classList.toggle('active');
            const tagValue = event.target.getAttribute('data-tag');
            toggleTagState(tagValue);
            console.log(`클릭된 태그: ${tagValue}, 활성화 상태: ${tagStates[tagValue]}`);
        }
    });
});
