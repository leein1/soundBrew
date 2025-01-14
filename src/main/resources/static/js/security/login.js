import { axiosPost } from "/js/fetch/standardAxios.js";

export function getLoginFormProcess() {
    document.querySelector("button[type='submit']").addEventListener("click", async (event) => {
        event.preventDefault(); // 폼의 기본 동작 막기

        const username = document.querySelector("#email").value;
        const password = document.querySelector("#password").value;
        const data = { username, password };

        try {
            const response = await generateTokenFromLogin(data);

            await redirectUrlFromToken(response);
        } catch (error) {
            console.error("Error during login or resource access:", error);
            alert("로그인 또는 리소스 요청 중 문제가 발생했습니다.");
        }
    });
}

export async function generateTokenFromLogin(userInput) {
    try {
        const response = await axios.post("http://localhost:8080/generateToken", userInput);
        const accessToken = response.data.accessToken;
        const refreshToken = response.data.refreshToken;
        const redirectUrl = response.data.redirectUrl;

        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);


        return { accessToken, refreshToken,redirectUrl };
    } catch (err) {
        console.error("로그인 과정 중 실패 :", err);
    }
}

async function redirectUrlFromToken(data){
    if (data.redirectUrl) {
        // Axios로 보호된 리소스 요청
        const resourceResponse = await axios.get(data.redirectUrl, {
            headers: {
                Authorization: `Bearer ${data.accessToken}`,
            },
        });

        // HTML 응답 삽입
        const container = document.createElement("div");
        container.innerHTML = resourceResponse.data; // HTML을 동적으로 삽입
        document.body.innerHTML = "";               // 기존 콘텐츠 제거
        document.body.appendChild(container);       // 새 콘텐츠 추가

        // 브라우저 URL도 변경
        window.history.pushState({}, "", data.redirectUrl);
    }
}