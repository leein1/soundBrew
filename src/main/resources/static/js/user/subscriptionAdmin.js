import { axiosGet, axiosPatch } from '/js/fetch/standardAxios.js';
import { router } from '/js/router.js';
import { formatDate } from '/js/formatDate.js';

/**
 * 편집 모드 활성화
 */
function enableEditing(button) {
  const row = button.closest('tr');

  // 현재 표시 중인 값(span) 숨기고, input 필드 보이기
  row.querySelectorAll('.current-value').forEach(span => {
    span.style.display = 'none';
  });
  row.querySelectorAll('.editable-field').forEach(input => {
    input.style.display = 'inline-block';
  });

  // "수정하기" 버튼 숨기고 "적용"과 "취소" 버튼 보이기
  button.style.display = 'none';
  const applyButton = row.querySelector('.apply-button');
  const cancelButton = row.querySelector('.cancel-button');
  if (applyButton) applyButton.style.display = 'inline-block';
  if (cancelButton) cancelButton.style.display = 'inline-block';
}

/**
 * 변경 내용을 적용하고 PATCH 요청 보내기
 */
async function applyChanges(button) {
  const row = button.closest('tr');
  // row에 data-속성으로 subscriptionId를 저장해두었음
  const subscriptionId = row.getAttribute('data-subscription-id');

  // 입력 필드에서 수정된 값 읽어오기 (필요시 유효성 검사 추가 가능)
  const subscriptionPriceInput = row.querySelector('input.editable-field[data-field="subscriptionPrice"]');
  const creditPerMonthInput = row.querySelector('input.editable-field[data-field="creditPerMonth"]');

  const updatedSubscriptionPrice = parseFloat(subscriptionPriceInput.value);
  const updatedCreditPerMonth = parseInt(creditPerMonthInput.value, 10);

  // 백엔드로 전송할 JSON 객체 생성 (SubscriptionDTO와 매핑)
  const updatedSubscriptionDTO = {
    subscriptionPrice: updatedSubscriptionPrice,
    creditPerMonth: updatedCreditPerMonth,
  };

  console.log(updatedSubscriptionDTO);

  const handle = {
    success:{
      navigate:"/admin/subscription"
    },
  };

  // PATCH 요청 전송 (백엔드의 SubscriptionDTO와 매칭되도록 body 전달)
  await axiosPatch({endpoint: `/api/admin/subscription/${subscriptionId}`,body: updatedSubscriptionDTO,handle,});

  // 변경 후 취소하여 초기 상태로 복귀
  cancelChanges(button);
}

/**
 * 편집 취소 (원래 상태로 복구)
 */
function cancelChanges(button) {
  const row = button.closest('tr');

  // input 필드 숨기고, span 요소 보이기
  row.querySelectorAll('.editable-field').forEach(input => {
    input.style.display = 'none';
  });
  row.querySelectorAll('.current-value').forEach(span => {
    span.style.display = 'inline-block';
  });

  // 버튼 상태 복원: "수정하기" 버튼 보이고, "적용"/"취소" 버튼 숨기기
  const editButton = row.querySelector('.edit-button');
  const applyButton = row.querySelector('.apply-button');
  const cancelButton = row.querySelector('.cancel-button');
  if (editButton) editButton.style.display = 'inline-block';
  if (applyButton) applyButton.style.display = 'none';
  if (cancelButton) cancelButton.style.display = 'none';
}

/**
 * 렌더링 후 각 버튼에 이벤트 리스너 등록
 */
function attachEventListeners() {
  const container = document.getElementById("content-body");

  container.querySelectorAll('.edit-button').forEach(button => {
    button.addEventListener('click', () => enableEditing(button));
  });
  container.querySelectorAll('.apply-button').forEach(button => {
    button.addEventListener('click', () => applyChanges(button));
  });
  container.querySelectorAll('.cancel-button').forEach(button => {
    button.addEventListener('click', () => cancelChanges(button));
  });
}

/**
 * 구독 정보를 렌더링하는 함수 (모듈로 export)
 */
export function renderSubscriptionInfo(response) {
  console.log(response);
  if (response.dtoList && response.dtoList.length > 0) {
    const container = document.getElementById("content-body");
    container.innerHTML = ''; // 기존 내용 초기화

    // 각 행(tr)에 data-subscription-id 속성을 추가하여 나중에 ID를 쉽게 참조할 수 있도록 함
    const renderHtml = `
      <!-- 데이터 테이블 -->
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>구독명</th>
              <th>금액</th>
              <th>제공 크레딧</th>
              <th>생성일</th>
              <th>수정일</th>
              <th>작업</th>
            </tr>
          </thead>
          <tbody>
            ${response.dtoList.map(manage => `
              <tr data-subscription-id="${manage.subscriptionId}">
                <td>${manage.subscriptionId}</td>
                <td>${manage.subscriptionName}</td>
                <td>
                  <span class="current-value" data-field="subscriptionPrice">${manage.subscriptionPrice}</span>
                  <input class="editable-field" type="number" data-field="subscriptionPrice" value="${manage.subscriptionPrice}" style="display:none;">
                </td>
                <td>
                  <span class="current-value" data-field="creditPerMonth">${manage.creditPerMonth}</span>
                  <input class="editable-field" type="number" data-field="creditPerMonth" value="${manage.creditPerMonth}" style="display:none;">
                </td>
                <td>${formatDate(manage.createDate)}</td>
                <td>${formatDate(manage.modifyDate)}</td>
                <td>
                  <button class="edit-button">수정하기</button>
                  <button class="apply-button" style="display:none;">적용</button>
                  <button class="cancel-button" style="display:none;">취소</button>
                </td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      </div>
    `;
    container.innerHTML = renderHtml;

    // 렌더링 후 버튼 이벤트 등록
    attachEventListeners();
  }
}
