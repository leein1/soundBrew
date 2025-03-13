import TokenUtil from '/js/tokenUtil.js';
import {axiosPost, axiosPatch, callRefresh} from '/js/fetch/standardAxios.js';
// 요소 선택
const profileModal = document.getElementById('profileModal');
const profileForm = document.getElementById('profileForm');
const profileInput = document.getElementById('profileInput');
const profilePreview = document.getElementById('profilePreview');
const cancelProfileImageBtn = document.getElementById('cancelProfileImage');
const profileModalBtn = document.getElementById('profileModalBtn'); // 모달 열기 버튼 (존재 시)
const userProfileImage = document.getElementById('userProfileImage'); // 페이지 내 사용자 프로필 이미지

// 모달 열기 함수
function showModal() {
    profileModal.classList.remove('hidden');
}

// 모달 닫기 및 입력값 초기화 함수
function hideModal() {
    profileModal.classList.add('hidden');
    profileInput.value = '';
    profilePreview.src = '';
    profilePreview.style.display = 'none';
}

// 이미지 선택 시 미리보기 업데이트
profileInput.addEventListener('change', (event) => {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
        profilePreview.src = e.target.result;
        profilePreview.style.display = 'block';
    };
        reader.readAsDataURL(file);
    }
});

// 폼 제출 시 업로드 진행
profileForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const selectedFiles = profileInput.files;
    if (selectedFiles.length === 0) {
        alert("파일을 업로드해주세요.");
        return;
    }
    if (selectedFiles.length > 1) {
        alert("파일은 한 번에 하나만 업로드할 수 있습니다.");
        return;
    }

    const selectedFile = selectedFiles[0];

    // 파일 크기 제한 (2MB)
    const maxFileSize = 2 * 1024 * 1024;
    if (selectedFile.size > maxFileSize) {
        alert("이미지 크기는 2MB를 초과할 수 없습니다.");
        return;
    }

    // 파일 확장자 제한 (JPG, JPEG, PNG)
    const allowedExtensions = ["jpg", "jpeg", "png"];
    const fileExtension = selectedFile.name.split(".").pop().toLowerCase();
    if (!allowedExtensions.includes(fileExtension)) {
        alert(`허용된 파일 형식은 ${allowedExtensions.join(", ")}입니다.`);
        return;
    }

    // 미리보기 이미지가 있다면 페이지의 사용자 프로필 이미지 업데이트
    if (profilePreview.src && userProfileImage) {
        userProfileImage.src = profilePreview.src;
    }

    // FormData 객체 생성 (폼 내 파일 및 기타 데이터를 함께 전송)
    const formData = new FormData(profileForm);

    const token = TokenUtil.getToken(); // accessToken을 가져옴
    const userInfo = TokenUtil.getUserInfo(token);

    formData.append("userId", userInfo.userId);

    // 서버 응답 핸들링 객체
    const handle = {
        success:{
            location:"/sounds/tracks",
            callback: async ()=>{
                await callRefresh();
            }
        },
        failure:{

        },
    };

    // axiosPost 함수를 통해 서버에 업로드 요청
    await axiosPost({endpoint: "/api/files/profiles",body: formData, handle});

    // 업로드 후 모달 닫기
    hideModal();
});
// 취소 버튼 클릭 시 모달 닫기
cancelProfileImageBtn.addEventListener('click', hideModal);

// 모달 열기 버튼이 있다면 이벤트 등록
if (profileModalBtn) {
    profileModalBtn.addEventListener('click', showModal);
}