import { axiosGet, axiosPatch, axiosPost, axiosDelete } from '/js/fetch/standardAxios.js';
import { inputHandler } from '/js/check/inputHandler.js';
import { serializeFormToJSON } from '/js/serialize/formToJson.js';
import { router } from '/js/router.js';
import { formatDate } from '/js/formatDate.js';

/**
 * 정렬 아이콘 추가 함수
 */
function addSortIcons() {
	const urlParams = new URLSearchParams(window.location.search);
	document.querySelectorAll("th.sortable").forEach(th => {
		const sortKey = th.dataset.sortKey;
		const paramName = `more[${sortKey}]`;
		const sortOrder = urlParams.get(paramName);
		let icon = '';

		if (sortOrder === 'asc') {
			icon = '\u25B2'; // ▲
		} else if (sortOrder === 'desc') {
			icon = '\u25BC'; // ▼
		}

		// 기존 아이콘 제거
		const existingIcon = th.querySelector('.sort-icon');
		if (existingIcon) {
			existingIcon.remove();
		}

		// 아이콘 추가
		if (icon !== '') {
			const iconSpan = document.createElement('span');
			iconSpan.classList.add('sort-icon');
			iconSpan.style.marginLeft = '5px';
			iconSpan.textContent = icon;
			th.appendChild(iconSpan);
		}

		// 클릭 시 정렬 파라미터 업데이트
		th.addEventListener("click", function () {
			updateSortParams(sortKey);
		});
	});
}

/**
 * JSON 파싱을 안전하게 하는 함수 (모듈 내에서만 사용)
 */
function safeJSONParse(data, defaultValue = {}) {
	try {
		return data ? JSON.parse(data) : defaultValue;
	} catch (error) {
		console.error('JSON parse error:', error);
		return defaultValue;
	}
}

/**
 * 편집 관련 함수들 – 모두 모듈 지역 함수로 선언
 */
function enableEditing(button) {
	const row = button.closest('tr');
	button.style.display = 'none';
	row.querySelector('.choose-creditBalance').style.display = 'inline-block';
	row.querySelector('.choose-subscriptionId').style.display = 'inline-block';
	row.querySelector('.choose-paymentStatus').style.display = 'inline-block';
}

function enableCreditBalanceEditing(button) {
	const row = button.closest('tr');
	// 다른 선택/적용 버튼 숨김
	row.querySelectorAll('.choose-creditBalance, .choose-subscriptionId, .choose-paymentStatus').forEach(btn => btn.style.display = 'none');
	row.querySelectorAll('.apply-creditBalance, .apply-subscriptionId, .apply-paymentStatus').forEach(btn => btn.style.display = 'none');

	const span = row.querySelector('span.current-value[data-field="creditBalance"]');
	if (span) span.style.display = 'none';
	const input = row.querySelector('.editable-field[data-field="creditBalance"]');
	if (input) input.style.display = 'inline-block';

	const applyBtn = row.querySelector('.apply-creditBalance');
	if (applyBtn) applyBtn.style.display = 'inline-block';
	const cancelBtn = row.querySelector('.cancel-button');
	if (cancelBtn) cancelBtn.style.display = 'inline-block';
}

function enableSubscriptionIdEditing(button) {
	const row = button.closest('tr');
	row.querySelectorAll('.choose-creditBalance, .choose-subscriptionId, .choose-paymentStatus').forEach(btn => btn.style.display = 'none');
	row.querySelectorAll('.apply-creditBalance, .apply-subscriptionId, .apply-paymentStatus').forEach(btn => btn.style.display = 'none');

	const span = row.querySelector('span.current-value[data-field="subscriptionId"]');
	if (span) span.style.display = 'none';
	const select = row.querySelector('.editable-field[data-field="subscriptionId"]');
	if (select) select.style.display = 'inline-block';

	const applyBtn = row.querySelector('.apply-subscriptionId');
	if (applyBtn) applyBtn.style.display = 'inline-block';
	const cancelBtn = row.querySelector('.cancel-button');
	if (cancelBtn) cancelBtn.style.display = 'inline-block';
}

function enablePaymentStatusEditing(button) {
	const row = button.closest('tr');
	row.querySelectorAll('.choose-creditBalance, .choose-subscriptionId, .choose-paymentStatus').forEach(btn => btn.style.display = 'none');
	row.querySelectorAll('.apply-creditBalance, .apply-subscriptionId, .apply-paymentStatus').forEach(btn => btn.style.display = 'none');

	const span = row.querySelector('span.current-value[data-field="paymentStatus"]');
	if (span) span.style.display = 'none';
	const select = row.querySelector('.editable-field[data-field="paymentStatus"]');
	if (select) select.style.display = 'inline-block';

	const applyBtn = row.querySelector('.apply-paymentStatus');
	if (applyBtn) applyBtn.style.display = 'inline-block';
	const cancelBtn = row.querySelector('.cancel-button');
	if (cancelBtn) cancelBtn.style.display = 'inline-block';
}

async function applyCreditBalanceChange(button) {
	const row = button.closest('tr');
	const userInfo = safeJSONParse(row.dataset.moreinformation);
	const userId = userInfo.userId;
	const inputField = row.querySelector('input.editable-field[data-field="creditBalance"]');
	const newCreditBalance = inputField ? inputField.value.trim() : null;

	console.log(`User ${userId} 크레딧 변경: ${newCreditBalance}`);

	if (newCreditBalance === null || newCreditBalance === "" || isNaN(newCreditBalance)) {
		alert("크레딧은 비어있을 수 없으며 숫자여야 합니다.");
		return;
	}

//	const handle = {
//		onSuccess: (data) => {
//			alert('정보가 관리자 권한으로 성공적으로 수정되었습니다!');
//			const spanField = row.querySelector('span.current-value[data-field="creditBalance"]');
//			if (spanField) {
//				spanField.textContent = newCreditBalance;
//				spanField.style.display = 'inline-block';
//			}
//			router.navigate("/admin/users");
//		},
//		onBadRequest: () => {
//			alert("업로드가 실패했습니다.");
//		},
//	};

    const handle = {
        success:{
            navigate:"/admin/users"
        },
    };

	await axiosPatch({ endpoint: `/api/admin/users/${userId}/credit`, body: newCreditBalance, handle });
	cancelChanges(button);
}

async function applySubscriptionId(button) {
	const row = button.closest('tr');
	const userInfo = safeJSONParse(row.dataset.moreinformation);
	const userId = userInfo.userId;
	const inputField = row.querySelector('.editable-field[data-field="subscriptionId"]');
	const newSubscriptionId = inputField ? inputField.value : null;

	console.log(`User ${userId} 구독ID 변경: ${newSubscriptionId}`);

//	const handle = {
//		onSuccess: (data) => {
//			alert('정보가 관리자 권한으로 성공적으로 수정되었습니다!');
//			const spanField = row.querySelector('span.current-value[data-field="subscriptionId"]');
//			if (spanField) {
//				spanField.textContent = newSubscriptionId;
//				spanField.style.display = 'inline-block';
//			}
//			router.navigate("/admin/users");
//		},
//		onBadRequest: () => {
//			alert("업로드가 실패했습니다.");
//		},
//	};
    const handle = {
        success:{
            navigate:"/admin/users"
        },
    };

	await axiosPatch({ endpoint: `/api/admin/users/${userId}/subscription`, body: newSubscriptionId, handle });
	cancelChanges(button);
}

async function applyPaymentStatus(button) {
	const row = button.closest('tr');
	const userInfo = safeJSONParse(row.dataset.moreinformation);
	const userId = userInfo.userId;
	const inputField = row.querySelector('.editable-field[data-field="paymentStatus"]');
	const newPaymentStatus = inputField ? inputField.value : null;

	console.log(`User ${userId} 결제 상태 변경: ${newPaymentStatus}`);

//	const handle = {
//		onSuccess: (data) => {
//			alert('정보가 관리자 권한으로 성공적으로 수정되었습니다!');
//			const spanField = row.querySelector('span.current-value[data-field="paymentStatus"]');
//			if (spanField) {
//				spanField.textContent = newPaymentStatus;
//				spanField.style.display = 'inline-block';
//			}
//			router.navigate("/admin/users");
//		},
//		onBadRequest: () => {
//			alert("업로드가 실패했습니다.");
//		},
//	};

    const handle = {
        success:{
            navigate:"/admin/users"
        },
    };

	await axiosPatch({ endpoint: `/api/admin/users/${userId}/payment`, body: newPaymentStatus, handle });
	cancelChanges(button);
}

function cancelChanges(button) {
	const row = button.closest('tr');
	row.querySelectorAll('.choose-creditBalance, .choose-subscriptionId, .choose-paymentStatus, .apply-creditBalance, .apply-subscriptionId, .apply-paymentStatus, .cancel-button')
		.forEach(btn => btn.style.display = 'none');
	row.querySelectorAll('.editable-field').forEach(input => {
		input.style.display = 'none';
	});
	row.querySelectorAll('.current-value').forEach(span => {
		span.style.display = 'inline-block';
	});
	const editBtn = row.querySelector('.edit-button');
	if (editBtn) {
		editBtn.style.display = 'inline-block';
	}
}

/**
 * 인라인 이벤트 핸들러 대신 렌더링 후 각 버튼에 이벤트 리스너를 붙입니다.
 */
function attachRowButtonListeners() {
	document.querySelectorAll('.edit-button').forEach(button => {
		button.addEventListener('click', () => enableEditing(button));
	});
	document.querySelectorAll('.choose-creditBalance').forEach(button => {
		button.addEventListener('click', () => enableCreditBalanceEditing(button));
	});
	document.querySelectorAll('.choose-subscriptionId').forEach(button => {
		button.addEventListener('click', () => enableSubscriptionIdEditing(button));
	});
	document.querySelectorAll('.choose-paymentStatus').forEach(button => {
		button.addEventListener('click', () => enablePaymentStatusEditing(button));
	});
	document.querySelectorAll('.apply-creditBalance').forEach(button => {
		button.addEventListener('click', () => applyCreditBalanceChange(button));
	});
	document.querySelectorAll('.apply-subscriptionId').forEach(button => {
		button.addEventListener('click', () => applySubscriptionId(button));
	});
	document.querySelectorAll('.apply-paymentStatus').forEach(button => {
		button.addEventListener('click', () => applyPaymentStatus(button));
	});
	document.querySelectorAll('.cancel-button').forEach(button => {
		button.addEventListener('click', () => cancelChanges(button));
	});
}

/**
 * 사용자 정보와 역할을 렌더링하는 함수 (모듈로 export)
 */
export function renderUserInfoWithRole(response) {
	const subscriptionMapping = {
		"1": "basic",
		"2": "premium",
		"3": "vip"
	};

	const roleMapping = {
		"1": "회원",
		"2": "매니저",
		"3": "관리자"
	};

	if (response.dtoList && response.dtoList.length > 0) {
		const container = document.getElementById("content-body");
		container.innerHTML = '';

		if (response.dtoList && response) {
			// 인라인 onclick 제거하고 버튼 클래스만 남김
			const renderHtml = `
				<div class="table-actions">
					<div>
						<label for="rows-per-page">페이지 당 데이터 수:</label>
						<select id="rows-per-page">
							<option value="5">5</option>
							<option value="10">10</option>
							<option value="15">15</option>
							<option value="20">20</option>
						</select>
					</div>
				</div>
				<div class="table-wrapper">
					<table>
						<thead>
							<tr>
								<th class="sortable" data-sort-key="userId">ID</th>
								<th>이름</th>
								<th>별명</th>
								<th class="sortable" data-sort-key="subscriptionId">구독</th>
								<th class="sortable" data-sort-key="creditBalance">크레딧</th>
								<th class="sortable" data-sort-key="paymentStatus">결제상태</th>
								<th class="sortable" data-sort-key="roleId">권한</th>
								<th>가입일</th>
								<th>작업</th>
							</tr>
						</thead>
						<tbody>
							${response.dtoList.map(manage => `
								<tr
									data-subscription='${JSON.stringify(manage.userSubscriptionDTO)}'
									data-moreinformation='${JSON.stringify(manage.userDTO)}'
									data-role='${JSON.stringify(manage.userRoleDTO)}'>

									<td class="userMoreInfo">${manage.userDTO.userId}</td>
									<td class="userMoreInfo">${manage.userDTO.name}</td>
									<td class="userMoreInfo">${manage.userDTO.nickname}</td>

									<td class="userSubscriptionMoreInfo">
										<span class="current-value" data-field="subscriptionId">
											${subscriptionMapping[manage.userDTO.subscriptionId] || manage.userDTO.subscriptionId}
										</span>
										<select class="editable-field" data-field="subscriptionId" style="display:none;">
											<option value="1" ${manage.userDTO.subscriptionId == 1 ? 'selected' : ''}>basic</option>
											<option value="2" ${manage.userDTO.subscriptionId == 2 ? 'selected' : ''}>premium</option>
											<option value="3" ${manage.userDTO.subscriptionId == 3 ? 'selected' : ''}>vip</option>
										</select>
									</td>

									<td class="userSubscriptionMoreInfo">
										<span class="current-value" data-field="creditBalance">${manage.userDTO.creditBalance}</span>
										<input class="editable-field" type="number" data-field="creditBalance" value="${manage.userDTO.creditBalance}" style="display:none;">
									</td>

									<td class="userSubscriptionMoreInfo">
										<span class="current-value" data-field="paymentStatus">${manage.userSubscriptionDTO.paymentStatus}</span>
										<select class="editable-field" data-field="paymentStatus" style="display:none;">
											<option value="true" ${manage.userSubscriptionDTO.paymentStatus === true ? 'selected' : ''}>true</option>
											<option value="false" ${manage.userSubscriptionDTO.paymentStatus === false ? 'selected' : ''}>false</option>
										</select>
									</td>

									<td class="userRoleMoreInfo">
										<span class="current-value" data-field="roleId">
											${roleMapping[manage.userRoleDTO.roleId] || manage.userRoleDTO.roleId}
										</span>
										<select class="editable-field" data-field="roleId" style="display:none;">
											<option value="1" ${manage.userRoleDTO.roleId == 1 ? 'selected' : ''}>회원</option>
											<option value="2" ${manage.userRoleDTO.roleId == 2 ? 'selected' : ''}>매니저</option>
											<option value="3" ${manage.userRoleDTO.roleId == 3 ? 'selected' : ''}>관리자</option>
										</select>
									</td>

									<td>${formatDate(manage.userDTO.createDate)}</td>

									<td>
										<div class="button-wrapper">
											<button class="edit-button">수정하기</button>
											<button class="choose-creditBalance" style="display:none;">크레딧</button>
											<button class="choose-subscriptionId" style="display:none;">구독</button>
											<button class="choose-paymentStatus" style="display:none;">결제상태</button>
											<button class="apply-creditBalance" style="display:none;">적용</button>
											<button class="apply-subscriptionId" style="display:none;">적용</button>
											<button class="apply-paymentStatus" style="display:none;">적용</button>
											<button class="cancel-button" style="display:none;">취소</button>
										</div>
									</td>
								</tr>
							`).join('')}
						</tbody>
					</table>
				</div>
			`;
			container.innerHTML = renderHtml;

			const urlParams = new URLSearchParams(window.location.search);
			const selectedSize = urlParams.get('size');
			if (selectedSize) {
				document.getElementById('rows-per-page').value = selectedSize;
			}

			// 정렬 아이콘 등록 및 버튼 이벤트 리스너 붙이기
			addSortIcons();
			attachRowButtonListeners();
		} else {
			container.innerHTML = '<p>렌더링 할 정보가 없습니다.</p>';
		}

		// 상세 정보 모달 등 나머지 셀 클릭 이벤트 처리
		document.querySelectorAll('.userMoreInfo').forEach(cell => {
			cell.addEventListener('click', (event) => {
				if (event.target.closest('.editable-field')) {
					return;
				}
				const row = cell.closest('tr');
				const userInfo = safeJSONParse(row.dataset.moreinformation);
				const modalTitle = document.querySelector('#userDetailModal h2');
				modalTitle.innerText = `${userInfo.name}의 자세한 정보`;
				const modalBody = document.getElementById('detail-modal-body');
				modalBody.innerHTML = `
					<div class="table-wrapper">
						<table>
							<thead>
								<tr>
									<th>ID</th>
									<th>구독 ID</th>
									<th>이름</th>
									<th>별명</th>
									<th>전화번호</th>
									<th>이메일</th>
									<th>이메일 인증</th>
									<th>크레딧</th>
									<th>프로필</th>
									<th>생년월일</th>
									<th>가입일</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>${userInfo.userId}</td>
									<td>${subscriptionMapping[userInfo.subscriptionId] || userInfo.subscriptionId}</td>
									<td>${userInfo.name}</td>
									<td>${userInfo.nickname}</td>
									<td>${userInfo.phoneNumber}</td>
									<td>${userInfo.email}</td>
									<td>${userInfo.emailVerified}</td>
									<td>${userInfo.creditBalance}</td>
									<td>${userInfo.profileImagePath}</td>
									<td>${userInfo.birth}</td>
									<td>${formatDate(userInfo.createDate)}</td>
								</tr>
							</tbody>
						</table>
					</div>
				`;
				document.getElementById('userDetailModal').style.display = 'block';
			});
		});

		document.querySelectorAll('.userSubscriptionMoreInfo').forEach(cell => {
			cell.addEventListener('click', (event) => {
				if (event.target.closest('.editable-field')) {
					return;
				}
				const row = cell.closest('tr');
				const userSubscriptionInfo = safeJSONParse(row.dataset.subscription);
				const userInfo = safeJSONParse(row.dataset.moreinformation);
				const modalTitle = document.querySelector('#userDetailModal h2');
				modalTitle.innerText = `${userInfo.name}의 구독 정보`;
				const modalBody = document.getElementById('detail-modal-body');
				modalBody.innerHTML = `
					<table>
						<thead>
							<tr>
								<th>ID</th>
								<th>구독 ID</th>
								<th>첫 결제일</th>
								<th>다음 결제일</th>
								<th>결제 여부</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>${userSubscriptionInfo.userId}</td>
								<td>${subscriptionMapping[userSubscriptionInfo.subscriptionId] || userSubscriptionInfo.subscriptionId}</td>
								<td>${userSubscriptionInfo.firstBillingDate}</td>
								<td>${userSubscriptionInfo.nextBillingDate}</td>
								<td>${userSubscriptionInfo.paymentStatus}</td>
							</tr>
						</tbody>
					</table>
				`;
				document.getElementById('userDetailModal').style.display = 'block';
			});
		});

		// 모달 닫기 버튼
		document.querySelector('#userDetailModal .close').addEventListener('click', () => {
			document.getElementById('userDetailModal').style.display = 'none';
		});
	}

	document.getElementById('rows-per-page').addEventListener('change', (event) => {
      const selectedSize = event.target.value;
      const currentParams = new URLSearchParams(window.location.search);
      currentParams.set('size', selectedSize);
      const newUrl = `${window.location.pathname}?${currentParams.toString()}`;
      router.navigate(newUrl);
    });
}

/**
 * 모달 외부 클릭 시 닫힘 처리
 */
window.addEventListener('click', (event) => {
	const modal = document.getElementById('userDetailModal');
	if (event.target === modal) {
		modal.style.display = 'none';
	}
});

/**
 * 셀 내부에서 편집필드 클릭 시 이벤트 버블링 중지
 */
const contentBody = document.getElementById('content-body');

if (contentBody) {
    contentBody.addEventListener('click', (event) => {
        if (event.target.matches('.editable-field')) {
            event.stopPropagation();
        }
    });
}


/**
 * 정렬 파라미터 업데이트 함수 (모듈로 export)
 */
export function updateSortParams(sortKey) {
	const currentParams = new URLSearchParams(window.location.search);
	const currentSortOrder = currentParams.get(`more[${sortKey}]`);
	if (!currentSortOrder || currentSortOrder === "desc") {
		currentParams.set(`more[${sortKey}]`, "asc");
	} else if (currentSortOrder === "asc") {
		currentParams.set(`more[${sortKey}]`, "desc");
	}
	const newQueryString = currentParams.toString();
	const newUrl = `${window.location.pathname}?${newQueryString}`;
	router.navigate(newUrl);
}

