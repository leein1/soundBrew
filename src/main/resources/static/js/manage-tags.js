    // 미리 받아온 태그 데이터 저장
    const allTags = {
        instrument: ["Guitar", "Piano", "Drums", "Bass"],
        mood: ["Happy", "Chill", "Energetic", "Sad"],
        genre: ["Rock", "Jazz", "Pop", "Classical"]
    };

    function enableEditing(button) {
        const row = button.closest('tr');
        row.querySelectorAll('.current-value').forEach(element => element.style.display = 'none');
        row.querySelectorAll('.editable-field').forEach(element => element.style.display = 'inline');
        row.querySelectorAll('.tag-search-button').forEach(element => element.style.display = 'inline');
        button.style.display = 'none';
        row.querySelector('.apply-button').style.display = 'inline';
        row.querySelector('.cancel-button').style.display = 'inline';
    }

    // 선택된 태그 유형별로 모달을 열고 태그 목록을 필터링하여 표시
    function openTagModal(tagType) {
        document.getElementById('tagModal').style.display = 'block';
        const tagList = document.getElementById('tagList');
        tagList.innerHTML = allTags[tagType].map(tag => `
            <label><input type="checkbox" value="${tag}"> ${tag}</label><br>
        `).join('');
    }

    function closeTagModal() {
        document.getElementById('tagModal').style.display = 'none';
    }

    async function applyChanges(button, id) {
        const row = button.closest('tr');
        const selectedTags = Array.from(row.querySelectorAll('input[type="checkbox"]:checked'))
                            .map(checkbox => checkbox.value);

        await fetch(`/sounds/tag/update`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id,
                tags: selectedTags
            })
        });

        alert("수정이 완료되었습니다.");
    }

    function cancelChanges(button) {
        const row = button.closest('tr');
        row.querySelectorAll('.editable-field').forEach(input => input.style.display = 'none');
        row.querySelectorAll('.current-value').forEach(span => span.style.display = 'inline');
        row.querySelector('.edit-button').style.display = 'inline';
        row.querySelector('.apply-button').style.display = 'none';
        row.querySelector('.cancel-button').style.display = 'none';
        row.querySelectorAll('.tag-search-button').forEach(button => button.style.display = 'none');
    }

    function cancelTagSelection() {
        // 선택 초기화 후 모달 닫기
        Array.from(document.querySelectorAll('#tagList input[type="checkbox"]')).forEach(checkbox => {
            checkbox.checked = false;
        });
        closeTagModal();
    }
