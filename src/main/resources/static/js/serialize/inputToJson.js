export function serializeInputToJSON(inputElement) {
  if (!(inputElement instanceof HTMLInputElement)) {
    throw new Error("제공된 엘리먼트는 input 요소가 아닙니다.");
  }

  const key = inputElement.name;
  let value = inputElement.value;

  // 필요에 따라 타입별 값 변환 (예: checkbox, number, date 등)
  switch (inputElement.type) {
    case 'number':
      value = Number(value);
      break;
    case 'date':
      value = new Date(value);
      break;
    case 'checkbox':
      value = inputElement.checked;
      break;
    // 필요에 따라 다른 타입도 추가할 수 있음
    default:
      // 별도의 변환 없이 문자열 그대로 사용
      break;
  }

  return { [key]: value };
}