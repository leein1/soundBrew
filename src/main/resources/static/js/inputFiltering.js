// 각 필드의 입력을 검증하는 함수
function handleInputChange(event, maxLength, validators) {
  const input = event.target.value; // 입력된 값 가져오기

  // validateInput 함수를 호출해 주어진 validators 배열의 규칙으로 입력값을 검증
  if (!InputValidator.validateInput(input, maxLength, validators)) {
    // 검증 실패 시, 사용자에게 유효하지 않은 입력에 대한 메시지를 보여줌
    event.target.setCustomValidity("유효하지 않은 입력입니다. 글자 수, 특수문자, 공백을 확인하세요.");
  } else {
    // 검증 성공 시, 오류 메시지를 초기화
    event.target.setCustomValidity("");
  }
}

// 입력값 검증 모듈
const InputValidator = {

  //모듈 #1
  // 입력 길이가 최대 길이 이하인지 확인
  validateLength(input, maxLength) {
    return input.length <= maxLength;
  },

  //모듈 #2
  // 특수문자가 없는지 확인 (한글, 영어, 숫자, 공백만 허용)
  validateNoSpecialChars(input) {
    const specialCharPattern = /[^a-zA-Z0-9가-힣\s]/;
    return !specialCharPattern.test(input);
  },

  //모듈 #3
  // 공백을 다듬고, 연속된 공백이 있는지 확인
  trimAndValidateWhitespace(input) {
    const trimmedInput = input.trim();
    const multipleSpacePattern = /\s{2,}/;
    return !multipleSpacePattern.test(trimmedInput);
  },

  // 전달된 검증 함수 목록을 이용해 각 조건을 종합적으로 확인
  validateInput(input, maxLength, validators) {
    let isValid = true;

    // 전달된 검증 함수 목록에 있는 각 함수 이름을 반복
    for (const validator of validators) {
      // validator는 현재 검증할 함수 이름 (문자열 형태)
      // InputValidator[validator]를 통해 해당 함수 호출, input과 maxLength 전달
      if (!InputValidator[validator](input, maxLength)) {
        isValid = false; // 하나라도 검증을 통과하지 못하면 isValid를 false로 설정
        break; // 검증 실패 시 반복문을 중단하고 더 이상 검증하지 않음
      }
    }

    return isValid; // 모든 검증을 통과했을 경우 true 반환
  }
};
