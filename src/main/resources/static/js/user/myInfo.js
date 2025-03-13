// myInfoForm.js

import { axiosPost, axiosPatch } from '/js/fetch/standardAxios.js';
import { serializeFormToJSON } from '/js/serialize/formToJson.js';
import { serializeInputToJSON } from '/js/serialize/inputToJson.js';
import { inputHandler } from '/js/check/inputHandler.js';

/**
 * 회원정보(dto)를 받아 폼을 렌더링하고 이벤트들을 초기화합니다.
 * @param {Object} dto - 회원 정보 객체 ({ userId, email, name, nickname, phoneNumber, birth })
 */
export async function renderMyInfo(dto) {
  const contentBody = document.getElementById('content-body');
  if (!contentBody) {
    console.error("content-body 요소를 찾을 수 없습니다.");
    return;
  }

  // HTML 렌더링
  contentBody.innerHTML = `
  <div class="myInfo-content-header">
      내 정보
  </div>

    <div class="myInfo-content-body">
    <form id="myInfo-form" class="myInfo-form">
      <div class="input-group">
        <label for="email" style="justify-content: space-between; display: flex">
          <span>이메일</span>
        </label>
        <div class="input-with-button">
          <input type="email" id="email" name="email" readonly required>
          <input type="checkbox" id="emailChecked" hidden>
        </div>
      </div>

      <div class="input-group">
        <label for="name">이름</label>
        <div class="input-with-button">
          <input type="text" id="name" name="name" readonly required>
          <button type="button" class="edit-btn" data-target="name">수정하기</button>
          <button type="button" class="save-btn" data-target="name" hidden>수정완료</button>
        </div>
        <button type="button" class="cancel-btn" data-target="name" hidden>취소하기</button>
      </div>

      <div class="input-group">
        <label for="nickname" style="justify-content: space-between; display: flex">
          <span>닉네임 ( 특수문자 없는 2글자 이상 )</span>
          <span class="duplicate-check-nickname" hidden>중복확인</span>
        </label>
        <div class="input-with-button">
          <input type="text" id="nickname" name="nickname" readonly required>
          <input type="checkbox" id="nicknameChecked" hidden>
          <button type="button" class="edit-btn" data-target="nickname">수정하기</button>
          <button type="button" class="save-btn" data-target="nickname" hidden>수정완료</button>
        </div>
        <button type="button" class="cancel-btn" data-target="nickname" hidden>취소하기</button>
      </div>

      <div class="input-group">
        <label>전화번호</label>
        <div class="phone-number">
          <input type="text" id="phone1" value="010" readonly>
          <input type="text" id="phone2" maxlength="4" pattern="\\d{4}" readonly required>
          <input type="text" id="phone3" maxlength="4" pattern="\\d{4}" readonly required>
          <button type="button" class="edit-btn" data-target="phoneNumber">수정하기</button>
          <button type="button" class="save-btn" data-target="phoneNumber" hidden>수정완료</button>
        </div>
        <button type="button" class="cancel-btn" data-target="phoneNumber" hidden>취소하기</button>
      </div>

      <div class="input-group">
        <label for="birth">생일 </label>
        <input type="date" id="birth" name="birth" readonly required>
      </div>

      <input type="hidden" name="phoneNumber" id="phoneNumber">
      <input type="hidden" name="userId" id="userId" readonly required>
    </form>
    </div>
  `;

  // 회원정보 업데이트
  updateMyInfo(dto);

  // 이벤트 리스너 초기화
  initEventListeners();
}

/**
 * dto 데이터를 폼 요소에 채워넣습니다.
 * @param {Object} dto
 */
function updateMyInfo(dto) {
  document.getElementById("userId").value = dto.userId;
  document.getElementById("email").value = dto.email;
  document.getElementById("name").value = dto.name;
  document.getElementById("nickname").value = dto.nickname;

  if (dto.phoneNumber) {
    const phoneNum = dto.phoneNumber.replace(/\D/g, "");
    if (phoneNum.length === 11) {
      document.getElementById("phone1").value = phoneNum.substring(0, 3);
      document.getElementById("phone2").value = phoneNum.substring(3, 7);
      document.getElementById("phone3").value = phoneNum.substring(7, 11);
    }
  }
  document.getElementById("birth").value = dto.birth;
}

/**
 * 전체 버튼 및 입력 필드의 이벤트를 초기화합니다.
 */
function initEventListeners() {
  initNicknameCheck();
  initCancelButtons();
  initEditButtons();
  initSaveButtons();
}

/**
 * 닉네임 입력 필드의 중복 체크 관련 이벤트 초기화
 */
function initNicknameCheck() {
  const nicknameInput = document.getElementById("nickname");
  const nicknameChecked = document.getElementById("nicknameChecked");
  let nicknameTimer;

  nicknameInput.addEventListener("input", () => {
    clearTimeout(nicknameTimer);
    nicknameChecked.checked = false;
    nicknameInput.style.borderColor = "gray";
    const duplicateEl = document.querySelector('.duplicate-check-nickname');
    if (duplicateEl) duplicateEl.style.setProperty('--status-color', "gray");

    nicknameTimer = setTimeout(() => {
      checkDuplicate(nicknameInput, "/api/verification/nickname", nicknameChecked);
    }, 500);
  });
}

/**
 * 취소 버튼 클릭 시 원래 상태로 복원
 */
function initCancelButtons() {
  document.querySelectorAll(".cancel-btn").forEach(button => {
    button.addEventListener("click", event => {
      const targetId = event.target.getAttribute("data-target");
      const inputField = document.getElementById(targetId);
      const editButton = document.querySelector(`.edit-btn[data-target="${targetId}"]`);
      const saveButton = document.querySelector(`.save-btn[data-target="${targetId}"]`);

      if (targetId === "nickname") {
        const duplicateEl = document.querySelector(".duplicate-check-nickname");
        if (duplicateEl) duplicateEl.hidden = true;
      }

      if (targetId === "phoneNumber") {
        const phone2 = document.getElementById("phone2");
        const phone3 = document.getElementById("phone3");
        phone2.setAttribute("readonly", "true");
        phone3.setAttribute("readonly", "true");
        phone2.style.borderColor = "";
        phone3.style.borderColor = "";
      }

      inputField.setAttribute("readonly", "true");
      inputField.style.borderColor = "";
      saveButton.hidden = true;
      event.target.hidden = true;
      editButton.hidden = false;
    });
  });
}

/**
 * 수정 버튼 클릭 시 해당 필드 활성화 및 버튼 상태 전환
 */
function initEditButtons() {
  document.querySelectorAll(".edit-btn").forEach(button => {
    button.addEventListener("click", event => {
      if (document.querySelectorAll(".save-btn:not([hidden])").length > 0) {
        alert("다른 항목을 수정 중입니다. 먼저 현재 항목의 수정을 완료하거나 취소해주세요.");
        return;
      }

      const targetId = event.target.getAttribute("data-target");
      const saveButton = document.querySelector(`.save-btn[data-target="${targetId}"]`);
      const cancelButton = document.querySelector(`.cancel-btn[data-target="${targetId}"]`);

      if (targetId === "nickname") {
        const duplicateEl = document.querySelector(".duplicate-check-nickname");
        if (duplicateEl) duplicateEl.hidden = false;
      }

      if (targetId === "phoneNumber") {
        const phone2 = document.getElementById("phone2");
        const phone3 = document.getElementById("phone3");
        phone2.removeAttribute("readonly");
        phone3.removeAttribute("readonly");
        phone2.style.borderColor = "orange";
        phone3.style.borderColor = "orange";
      } else {
        const inputField = document.getElementById(targetId);
        if (inputField) {
          inputField.removeAttribute("readonly");
          inputField.style.borderColor = "orange";
        }
      }

      saveButton.style.backgroundColor = "orange";
      saveButton.style.color = "white";
      event.target.hidden = true;
      saveButton.hidden = false;
      cancelButton.hidden = false;
    });
  });
}

/**
 * 수정완료 버튼 클릭 시 입력값 검증 및 API PATCH 호출
 */
function initSaveButtons() {
  document.querySelectorAll(".save-btn").forEach(button => {
    button.addEventListener("click", async event => {
      event.preventDefault();
      const targetId = event.target.getAttribute("data-target");
      const userIdInput = document.getElementById("userId");

      if (targetId === "nickname") {
        if (!document.getElementById("nicknameChecked").checked) {
          alert("닉네임 중복입니다. 다른 닉네임을 입력해주세요.");
          document.getElementById("nickname").focus();
          return;
        }
        await submitFieldChange(targetId, "/api/me", () => {
          alert("닉네임 수정을 완료하였습니다.");
          window.location.href = "/myInfo";
        });
      } else if (targetId === "phoneNumber") {
        const phone2 = document.getElementById("phone2");
        const phone3 = document.getElementById("phone3");
        if (phone2.value.length !== 4 || phone3.value.length !== 4) {
          alert("전화번호를 올바르게 입력해주세요.");
          return;
        }
        // 전화번호는 3-4-4 형식으로 결합
        const fullPhoneNumber = document.getElementById("phone1").value +
                                phone2.value + phone3.value;
        document.getElementById("phoneNumber").value = fullPhoneNumber;
        await submitFieldChange(targetId, "/api/me", () => {
          alert("전화번호 수정을 완료하였습니다.");
          window.location.href = "/myInfo";
        });
      } else if (targetId === "name") {
        await submitFieldChange(targetId, "/api/me", () => {
          alert("이름 수정을 완료하였습니다.");
          window.location.href = "/myInfo";
        });
      }
    });
  });
}

/**
 * 지정한 필드의 변경사항을 폼 데이터로 직렬화하여 PATCH 요청을 보냅니다.
 * @param {string} targetId - 수정 대상 필드 id 또는 phoneNumber(전화번호의 경우)
 * @param {string} endpoint - API 엔드포인트
 * @param {Function} onSuccess - 성공 시 실행할 콜백
 */
async function submitFieldChange(targetId, endpoint, onSuccess) {
  const userIdInput = document.getElementById("userId");
  const form = document.createElement("form");

  if (targetId === "phoneNumber") {
    // 이미 phoneNumber hidden 필드에 값이 채워짐
    const phoneField = document.getElementById("phoneNumber");
    const phoneClone = phoneField.cloneNode();
    phoneClone.value = phoneField.value;
    form.appendChild(phoneClone);
  } else {
    const inputField = document.getElementById(targetId);
    const inputClone = inputField.cloneNode();
    inputClone.value = inputField.value;
    form.appendChild(inputClone);
  }

  const userIdClone = userIdInput.cloneNode();
  userIdClone.value = userIdInput.value;
  form.appendChild(userIdClone);

  const jsonData = serializeFormToJSON(form);
  const { errors, processedData } = inputHandler(jsonData, form);

    const handle = {
        success:{
            navigate:"/me/info"
        },
    };

  if (!errors) {
    await axiosPatch({endpoint,body: processedData,handle,useToken: true});
  }
}

/**
 * 중복 체크 요청 및 입력 필드의 상태 업데이트
 * @param {HTMLElement} inputElement
 * @param {string} endpoint
 * @param {HTMLElement} checkedElement
 */
async function checkDuplicate(inputElement, endpoint, checkedElement) {
  if (inputElement.value.trim() === "") return;

  let checkEl = null;
  if (inputElement.id === "email") {
    checkEl = document.querySelector('.duplicate-check-email');
  } else if (inputElement.id === "nickname") {
    checkEl = document.querySelector('.duplicate-check-nickname');
  }

  inputElement.style.borderColor = "orange";
  if (checkEl) {
    checkEl.style.setProperty('--status-color', "orange");
  }

  console.log(`${endpoint} 중복 체크 요청:`, inputElement.value);
  const inputJson = serializeInputToJSON(inputElement);
  try {
    const response = await axiosPost({ endpoint, body: inputJson });
    console.log(`${endpoint} 중복 체크 결과:`, response);
    checkedElement.checked = response.available;
    updateInputAndCheckColor(inputElement, response.available, checkEl);
  } catch (err) {
    console.error(`${endpoint} 중복 체크 오류:`, err);
    updateInputAndCheckColor(inputElement, false, checkEl);
  }
}

/**
 * 입력 필드와 중복 확인 엘리먼트의 색상을 업데이트합니다.
 * @param {HTMLElement} inputElement
 * @param {boolean} isAvailable
 * @param {HTMLElement} checkEl
 */
function updateInputAndCheckColor(inputElement, isAvailable, checkEl) {
  const color = inputElement.value.trim() === "" ? "gray" : (isAvailable ? "limegreen" : "red");
  inputElement.style.borderColor = color;
  if (checkEl) {
    checkEl.style.setProperty('--status-color', color);
  }
}
