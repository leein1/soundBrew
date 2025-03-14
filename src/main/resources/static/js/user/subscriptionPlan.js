export async function renderSubscriptionPlans() {
    const contentBody = document.querySelector(".content-body");

    if (!contentBody) {
        console.error("content-body 요소를 찾을 수 없습니다.");
        return;
    }

    contentBody.innerHTML = `
    <div class="subscriptionPlan-content-header">
        구독제

    </div>

    <div class="subscriptionPlan-content-body">
        <div class="subscription-container">
            <div class="subscription-card">
                <h2>Free</h2>
                <p>월 결제 금액: 0원</p>
                <p>월 크레딧: 0</p>
                <button type="button" class="subscribe-btn" data-plan="Free">구독하기</button>
            </div>
            <div class="subscription-card">
                <h2>Basic</h2>
                <p>월 결제 금액: 10,000원</p>
                <p>월 크레딧: 50</p>
                <button type="button" class="subscribe-btn" data-plan="Basic">구독하기</button>
            </div>
            <div class="subscription-card">
                <h2>Premium</h2>
                <p>월 결제 금액: 20,000원</p>
                <p>월 크레딧: 100</p>
                <button type="button" class="subscribe-btn" data-plan="Premium">구독하기</button>
            </div>
            <div class="subscription-card vip-card">
                <h2>VIP</h2>
                <p>월 결제 금액: 30,000원</p>
                <p>무제한으로 사용하세요!</p>
                <button type="button" class="subscribe-btn" data-plan="VIP">구독하기</button>
            </div>
        </div>
    `;

    attachSubscriptionEventHandlers();
}

function attachSubscriptionEventHandlers() {
    const subscribeButtons = document.querySelectorAll(".subscribe-btn");

    subscribeButtons.forEach((button) => {
        button.addEventListener("click", (event) => {
            const plan = event.target.dataset.plan;
            alert("결제 기능과 함께 업데이트 예정입니다.");
            // alert(`${plan} 플랜을 선택하셨습니다.`);

            // if (plan === "VIP") {
            //     alert("VIP는 무제한 서비스입니다. 고민하지 말고 바로 구독하세요!");
            // }
        });
    });
}
