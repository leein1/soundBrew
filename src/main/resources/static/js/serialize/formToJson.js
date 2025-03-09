//formToJson.js

// Form 데이터를 JSON으로 변환하는 함수
export function serializeFormToJSON(form) {
    const excludeFields = ['pageMetadata', 'csrfToken'];
    const formData = new FormData(form);
    const jsonObject = {};

    formData.forEach((value, key) => {
        if (excludeFields.includes(key)) {
            return; // 제외 필드 무시
        }

        // 입력 필드의 type 가져오기
        const field = form.querySelector(`[name="${key}"]`);
        const type = field?.type || 'text'; // 타입이 없으면 기본값은 'text'

        // 값 변환
        let convertedValue = value;
        if (type === 'number') {
            convertedValue = Number(value);
        } else if (type === 'date') {
            convertedValue = new Date(value);
        } else if (type === 'checkbox') {
            convertedValue = field.checked; // 체크박스는 true/false로 변환
        } else if (type === 'radio') {
            if (!field.checked) return; // 체크된 라디오 버튼만 처리
        }

        // 중범주 키 분리
        const keys = key.split('.'); // 예: 'albumDto.nickname' → ['albumDto', 'nickname']
        let current = jsonObject;

        keys.forEach((k, index) => {
            if (index === keys.length - 1) {//반복 중인 k(키)가 배열 keys의 마지막 요소인지 여부
                if (k === 'instrument' || k === 'genre'  || k === 'mood' ) { // 현재 키가 'instrument'인지 확인
                    // instrument가 배열이면 기존 배열에 추가, 아니면 새 배열로 생성
                    if (Array.isArray(current[k])) {
                        current[k].push(convertedValue);
                    } else {
                        current[k] = [convertedValue];
                    }
                // 마지막 키는 값을 할당
                }else if (current[k]) {
                    if (!Array.isArray(current[k])) {
                        current[k] = [current[k]];
                    }
                    current[k].push(convertedValue);
                } else {
                    current[k] = convertedValue;
                }
            } else {
                // 중간 키는 객체 생성
                if (!current[k]) {
                    current[k] = {};
                }
                current = current[k];
            }
        });
    });

    return jsonObject;
}