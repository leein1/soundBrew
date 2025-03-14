export async function renderMySubscription() {
    const contentBody = document.querySelector(".content-body");

    contentBody.innerHTML = `
    <div class="mySubscription-content-header">
        내 구독 정보
    </div>

    <div class="mySubscription-content-body">
        <div class="dashboard-container">
            <!-- 왼쪽 열: 결제 수단 카드 -->
            <div class="left-column">

                <div class="subscription-info-container">
                    <div class="subscription-info-card">
                        <h2>Free</h2>
                        <p>월 결제 금액: 0원</p>
                        <p>월 크레딧: 0</p>
                        <p>가입일: <span>2025-01-01</span></p>
                        <div class="button-container">
                            <button type="button" id="change-subscription-btn">구독 변경</button>
                            <button type="button" id="cancel-subscription-btn">구독 취소</button>
                        </div>
                    </div>
                </div>

                <div class="payment-method-container">
                    <div class="payment-method-card">
                        <div class="card-name">SoundBrew Test Card</div>
                        <div class="card-info">
                            <div class="card-number">
                                <p>**** **** **** 1234</p>
                            </div>
                            <div class="cvc">
                                <p>123</p>
                            </div>
                        </div>
                        <div class="card-details">
                            <p>유효기간: 12/24</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 오른쪽 열: 구독 정보 및 결제 내역 -->
            <div class="right-column">
                <div class="payment-history-container">
                    <div class="payment-history-card">
                        <h2>결제 내역</h2>
                        <!-- 결제 내역 예시 -->
                        <p>2025-01-01: 결제 실패</p>
                        <p>2024-12-01: 결제 완료</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `;

    attachDashboardEventHandlers();
}

function attachDashboardEventHandlers() {
    const changeSubscriptionBtn = document.getElementById("change-subscription-btn");
    const cancelSubscriptionBtn = document.getElementById("cancel-subscription-btn");

    if (changeSubscriptionBtn) {
        changeSubscriptionBtn.addEventListener("click", () => {
            alert("구독 변경 기능은 아직 구현되지 않았습니다.");
        });
    }

    if (cancelSubscriptionBtn) {
        cancelSubscriptionBtn.addEventListener("click", () => {
            const confirmCancel = confirm("정말로 구독을 취소하시겠습니까?");
            if (confirmCancel) {
                alert("구독이 취소되었습니다.");
                // 여기에 구독 취소 API 호출 로직 추가
            }
        });
    }
}
