function enableEditing(button) {
    const row = button.closest('tr');

    // 기존 값 감추기
    row.querySelectorAll('.current-value').forEach(span => {
        span.style.display = 'none';
    });

    // 수정 필드 보이기
    row.querySelectorAll('.editable-field').forEach(input => {
        input.style.display = 'inline-block';
    });

    // 버튼 상태 변경
    button.style.display = 'none';
    row.querySelector('.apply-button').style.display = 'inline-block';
    row.querySelector('.cancel-button').style.display = 'inline-block';
    row.querySelector('.open-modal').style.display = 'inline-block';
}

// 폼을 생성하는 함수
function createFormData(row) {
    const form = document.createElement('form');
    form.id = 'myForm';

    // 수정된 데이터 수집 후, 폼에 hidden input으로 추가
    row.querySelectorAll('.editable-field').forEach(input => {
        const name = input.dataset.field;
        const value = input.value;

        // hidden input을 만들어서 폼에 추가
        const hiddenInput = document.createElement('input');
        hiddenInput.type = 'hidden';  // hidden 타입으로 설정
        hiddenInput.name = name;  // 필드명
        hiddenInput.value = value;  // 입력된 값

        form.appendChild(hiddenInput);
    });

    return form;
}
// applyChanges 함수
async function applyChanges(button, albumId) {
    const row = button.closest('tr');
    const container = document.getElementById("render-update");
    container.innerHTML = '';  // 기존 폼 비우기

    const formData = createFormData(row);  // 폼 데이터 생성

    // 폼을 body에 추가
    container.appendChild(formData);

    // 서버에 데이터 전송
    await sendUpdateRequest(albumId, formData);
}

function cancelChanges(button) {
    const row = button.closest('tr');

    // 입력 필드 초기화
    row.querySelectorAll('.editable-field').forEach(input => {
        const field = input.dataset.field;
        const currentValue = row.querySelector(`.current-value[data-field="${field}"]`).textContent;
        input.value = currentValue;
        input.style.display = 'none';
    });

    // 기존 값 보이기
    row.querySelectorAll('.current-value').forEach(span => {
        span.style.display = 'inline-block';
    });

    // 버튼 상태 복원
    row.querySelector('.edit-button').style.display = 'inline-block';
    button.style.display = 'none';
    row.querySelector('.apply-button').style.display = 'none';
    row.querySelector('.open-modal').style.display = 'none';
}
