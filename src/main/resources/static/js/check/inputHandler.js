import { validationRules,processingRules } from '/js/check/inputRules.js';

export function inputProcessor(key, value, rules) {
    if (!rules || !rules[key]) return value; // 처리 규칙 없으면 원래 값 반환
    let processedValue = value;

    rules[key].forEach((rule) => {
        switch (rule) {
            case 'trim':
                if (typeof processedValue === 'string') {
                    processedValue = processedValue.trim();
                }
                break;
            case 'toLowerCase':
                if (typeof processedValue === 'string') {
                    processedValue = processedValue.toLowerCase();
                }
                break;
            case 'trimMiddle':
                if (typeof processedValue === 'string') {
                    processedValue = processedValue.replace(/\s+/g, ' ').trim(); // 중간 공백 제거
                }
                break;
            default:
                console.warn(`Unknown processing rule: ${rule}`);
        }
    });

    return processedValue;
}

export function inputValidator(key, value, rules) {
    const errors = [];
    if (!rules) return errors;

    // 필수 항목 확인
    if (rules.required && (value === null || value === undefined || value === '')) {
        errors.push(`${key} is required.`);
    }

    // 최대 길이 확인
    if (rules.maxLength && value?.length > rules.maxLength) {
        errors.push(`${key} must not exceed ${rules.maxLength} characters.`);
    }

    // 패턴(정규식) 확인
    if (rules.pattern && Array.isArray(value)) {
        // 배열 내의 각 항목에 대해 패턴 검사
        value.forEach((item, index) => {
            if (!rules.pattern.test(item)) {
                errors.push(`${key}[${index}] has an invalid format.`);
            }
        });
    } else if (rules.pattern && !Array.isArray(value) && !rules.pattern.test(value)) {
        errors.push(`${key} has an invalid format.`);
    }

    // 숫자 타입 확인
    if (rules.type === 'number' && value != null) {
        const numValue = Number(value);
        if (isNaN(numValue)) {
            errors.push(`${key} must be a valid number.`);
        } else {
            if (rules.min !== undefined && numValue < rules.min) {
                errors.push(`${key} must be at least ${rules.min}.`);
            }
            if (rules.max !== undefined && numValue > rules.max) {
                errors.push(`${key} must not exceed ${rules.max}.`);
            }
        }
    }

    return errors;
}

export function inputHandler(input, form) {
    const errors = {};
    const processedData = {};
    let firstErrorField = null; // 첫 번째 에러가 발생한 필드

    Object.keys(input).forEach((key) => {
        let value = input[key];

        // 처리 규칙 적용
        value = inputProcessor(key, value, processingRules);

        const fieldErrors = inputValidator(key, value, validationRules[key]);

        if (fieldErrors.length) {
            errors[key] = fieldErrors;
            // 첫 번째 에러 필드를 추적
            if (!firstErrorField) {
                firstErrorField = key;
            }
        } else {
            processedData[key] = value; // 처리된 데이터 저장
        }
    });

    // 에러가 있으면 피드백 제공
    if (Object.keys(errors).length > 0) {
        const firstErrorMessage = errors[firstErrorField][0]; // 첫 번째 에러 메시지
        alert(`Validation Error: ${firstErrorMessage}`); // 사용자에게 에러 메시지 알림

        // 폼 내의 첫 번째 에러 필드 찾기
        const fieldWithError = form.querySelector(`[name="${firstErrorField}"]`);
        if (fieldWithError) {
            fieldWithError.focus(); // 포커스 이동
        }

        return { errors, processedData: null };
    }

    // 에러가 없으면 처리된 데이터를 반환
    return { errors: null, processedData };
}
