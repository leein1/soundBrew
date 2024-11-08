    // 수정 모드 활성화 함수
    function enableEditing(button) {
        // 클릭된 버튼을 포함하는 행(tr)을 찾음
        const row = button.closest('tr');

        // 현재 값이 표시되는 요소들 숨기기
        row.querySelectorAll('.current-value').forEach(element => element.style.display = 'none');

        // 수정 가능한 입력 필드들을 표시
        row.querySelectorAll('.editable-field').forEach(element => element.style.display = 'inline');

        // 수정 버튼 숨기기
        button.style.display = 'none';

        // 적용 버튼과 취소 버튼을 표시
        row.querySelector('.apply-button').style.display = 'inline';
        row.querySelector('.cancel-button').style.display = 'inline';
    }

    // 변경 사항 적용 함수 (비동기 함수)
    async function applyChanges(button, id) {
        // 클릭된 버튼을 포함하는 행(tr)을 찾음
        const row = button.closest('tr');

        // 각 입력 필드(수정된 값)와 현재 값 텍스트를 각각 가져옴
        const albumTitleInput = row.querySelector('.editable-field[data-field="albumTitle"]');
        const albumDescriptionInput = row.querySelector('.editable-field[data-field="albumDescription"]');
        const albumTitleDisplay = row.querySelector('.current-value[data-field="albumTitle"]');
        const albumDescriptionDisplay = row.querySelector('.current-value[data-field="albumDescription"]');

        // 입력 필드에서 수정된 값을 화면에 표시
        albumTitleDisplay.textContent = albumTitleInput.value;
        albumDescriptionDisplay.textContent = albumDescriptionInput.value;

        // 입력 필드는 숨기고, 변경된 값이 표시된 부분은 다시 보여줌
        albumTitleInput.style.display = 'none';
        albumTitleDisplay.style.display = 'inline';
        albumDescriptionInput.style.display = 'none';
        albumDescriptionDisplay.style.display = 'inline';

        // 서버로 데이터 전송 (비동기)
        try {
            // 서버에 수정된 값을 POST 방식으로 전송
            await fetch(`/sounds/manage/${id}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    albumTitle: albumTitleInput.value,
                    albumDescription: albumDescriptionInput.value
                })
            });
            // 수정 완료 후 사용자에게 알림
            alert("수정이 완료되었습니다.");
        } catch (error) {
            // 요청 실패 시 오류 메시지 출력
            alert("수정 중 오류가 발생했습니다.");
            console.error(error); // 콘솔에 오류 상세 정보 출력
        }

        // 버튼 상태 초기화: 취소 버튼과 적용 버튼 숨기기, 수정 버튼 다시 표시
        button.style.display = 'none';
        row.querySelector('.cancel-button').style.display = 'none';
        row.querySelector('.edit-button').style.display = 'inline';
    }

    // 수정 취소 함수
    function cancelChanges(button) {
        // 클릭된 취소 버튼을 포함하는 행(tr)을 찾음
        const row = button.closest('tr');

        // 수정 가능한 입력 필드와 현재 값을 각각 가져옴
        const inputFields = row.querySelectorAll('.editable-field');
        const currentValues = row.querySelectorAll('.current-value');

        // 원래 값으로 되돌리기
        inputFields.forEach((input, index) => {
            // 원래 텍스트 값을 입력 필드에 복원
            input.value = currentValues[index].textContent;
            // 입력 필드는 숨기고, 원래 값을 다시 표시
            input.style.display = 'none';
            currentValues[index].style.display = 'inline';
        });

        // 버튼 상태 초기화: 취소 버튼 숨기기, 수정 버튼 다시 표시, 적용 버튼 숨기기
        button.style.display = 'none';
        row.querySelector('.apply-button').style.display = 'none';
        row.querySelector('.edit-button').style.display = 'inline';
    }
