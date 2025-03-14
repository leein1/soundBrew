import { serializeFormToJSON } from '/js/serialize/formToJson.js';
import { inputHandler } from '/js/check/inputHandler.js';
import { axiosPatch } from '/js/fetch/standardAxios.js';


export async function renderChangePassword() {
  const contentBody = document.getElementById('content-body');
  if (!contentBody) {
    console.error("content-body 요소를 찾을 수 없습니다.");
    return;
  }

  contentBody.innerHTML = `
  <div class="changepw-content-header">
          비밀번호 변경

  </div>

  <div class="changepw-content-body">
    <form class="changepw-form" id="changepw-form">
      <div class="input-group">
        <label for="password">비밀번호 8글자 이상 숫자,특수문자,대문자 1개 이상</label>
        <input type="password" id="password" name="password" required>
      </div>
      <div class="input-group">
        <label for="password-check">비밀번호 확인</label>
        <input type="password" id="password-check" name="password-check" required>
      </div>
      <button type="submit">확인</button>
    </form>
    </div>
  `;

  initChangePasswordFormEvents();
}

export function initChangePasswordFormEvents() {
  // 비밀번호 및 확인 입력 요소 선택
  const passwordInput = document.getElementById("password");
  const passwordCheckInput = document.getElementById("password-check");

  if (!passwordInput || !passwordCheckInput) {
    console.error("비밀번호 입력 필드를 찾을 수 없습니다.");
    return;
  }

  // 비밀번호 일치 여부를 체크하는 함수
  function validatePasswordMatch() {
    const password = passwordInput.value;
    const passwordCheck = passwordCheckInput.value;

    if (passwordCheck === "") {
      // 비밀번호 확인란이 비어있으면 기본 상태로
      passwordCheckInput.style.borderColor = "";
    } else if (password === passwordCheck) {
      // 일치하면 초록색 테두리
      passwordCheckInput.style.borderColor = "limegreen";
    } else {
      // 불일치하면 빨간색 테두리
      passwordCheckInput.style.borderColor = "red";
    }
  }

  // 입력 이벤트에 검증 함수 등록
  passwordInput.addEventListener("input", validatePasswordMatch);
  passwordCheckInput.addEventListener("input", validatePasswordMatch);

  // 폼 요소 선택
  const changePwForm = document.getElementById("changepw-form");
  if (!changePwForm) {
    console.error("changepw-form 요소를 찾을 수 없습니다.");
    return;
  }

  // 폼 제출 이벤트 등록
  changePwForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    // submit 버튼만 선택하여 비활성화
    const submitButton = changePwForm.querySelector('button[type="submit"]');
    submitButton.disabled = true;

    try {
      const password = passwordInput.value;
      const passwordCheck = passwordCheckInput.value;

      if (password !== passwordCheck) {
        alert("비밀번호를 다시 확인해주세요");
        passwordCheckInput.focus();
        submitButton.disabled = false;
        return;
      }

      // 폼 데이터를 직렬화 (serializeFormToJSON과 inputHandler는 미리 정의되어 있다고 가정)
      const jsonData = serializeFormToJSON(changePwForm);
      alert(jsonData.toString());

      const { errors, processedData } = inputHandler(jsonData, changePwForm);

      // const handle = {
      //   onSuccess: (data) => {
      //     alert(data.message);
      //     alert("다시 로그인 해주세요.");
      //     window.location.href = "/login";
      //   },
      //   onBadRequest: (data) => {
      //     alert(data.message);
      //     window.location.href = "/sounds/tracks";
      //     submitButton.disabled = false;
      //   }
      // };

      const handle = {
        success: {
          location: "/login"
        },
        failure: {
          message: "오류가 발생하였습니다. 문의부탁 드립니다.",
          location: "/login"
        }
      }

      if (!errors) {
        await axiosPatch({ endpoint: "/api/me/password", body: processedData, useToken: true, handle });
        alert("요청 보냄");

      }
    } catch (err) {
      console.error(err);
      submitButton.disabled = false;
    }
  });
}
