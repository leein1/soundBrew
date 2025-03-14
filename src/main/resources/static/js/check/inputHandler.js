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
                    alert("!!");
                    alert("before : "+processedValue);
                    processedValue = processedValue.toLowerCase();
                    alert("after : "+processedValue);
                }
                break;
            case 'trimMiddle':
                if (typeof processedValue === 'string') {
                    processedValue = processedValue.replace(/\s+/g, ' ').trim(); // 중간 공백 제거
                }
                break;
            case 'toUpperCase':
                if (typeof processedValue === 'string'){
                    processedValue = processedValue.toUpperCase();
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

    // 최소 길이 확인
    if (rules.minLength && value?.length < rules.minLength) {
        errors.push(`${key} must not exceed ${rules.minLength} characters.`);
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
                errors.push(`${key}[${index}] 가 양식에 맞지 않습니다.`);
            }
        });
    } else if (rules.pattern && !Array.isArray(value) && !rules.pattern.test(value)) {
        errors.push(`${key} 가 양식에 맞지 않습니다.`);
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

function flattenObject(obj, parentKey = '', result = {}) {
    for (let key in obj) {
        const newKey = parentKey ? `${parentKey}.${key}` : key;
        if (typeof obj[key] === 'object' && !Array.isArray(obj[key])) {
            flattenObject(obj[key], newKey, result);
        } else {
            result[newKey] = obj[key];
        }
    }
    return result;
}

function unflattenObject(flatObj) {
    const result = {};
    for (let flatKey in flatObj) {
        const keys = flatKey.split('.');
        let current = result;
        keys.forEach((key, index) => {
            if (index === keys.length - 1) {
                current[key] = flatObj[flatKey];
            } else {
                current[key] = current[key] || {};
                current = current[key];
            }
        });
    }
    return result;
}

export function inputHandler(input, form) {
    const flatInput = flattenObject(input); // 중첩 객체를 평탄화
    const errors = {};
    const processedData = {};
    let firstErrorField = null;

    Object.keys(flatInput).forEach((flatKey) => {
        let value = flatInput[flatKey];     

        // 처리 규칙 적용
        value = inputProcessor(flatKey, value, processingRules);

        // 검증 수행
        const fieldErrors = inputValidator(flatKey, value, validationRules[flatKey]);

        if (fieldErrors.length) {
            errors[flatKey] = fieldErrors;
            if (!firstErrorField) {
                firstErrorField = flatKey;
            }
        } else {
            processedData[flatKey] = value;
        }
    });

    if (Object.keys(errors).length > 0) {
        const firstErrorMessage = errors[firstErrorField][0];
        alert(`Validation Error: ${firstErrorMessage}`); // 사용자에게 에러 메시지 알림
        // 수정된 부분: firstErrorField 그대로 사용
        const firstErrorFieldOriginal = firstErrorField;

        const fieldWithError = form.querySelector(`[name="${firstErrorFieldOriginal}"]`);
        if (fieldWithError) {
            fieldWithError.focus();
        }

        return { errors, processedData: null };
    }

    // 평탄화된 데이터 복구
    const nestedProcessedData = unflattenObject(processedData);
    return { errors: null, processedData: nestedProcessedData };
}
