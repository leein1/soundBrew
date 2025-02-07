import { axiosPost } from "/js/fetch/standardAxios.js";
import {inputHandler} from '/js/check/inputHandler.js';

export function getLoginFormProcess() {
    document.getElementById("login-form").addEventListener("submit", async (event) => {
        event.preventDefault(); // 폼의 기본 동작 막기

        const username = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const data = { username, password };

        try {
            const response = await generateTokenFromLogin(data);
            alert(response.response.status.toString());
            if(response.response && response.response ===200){
                alert("ASdfasdfas")
            }
        } catch (error) {
            console.error("Error during login or resource access:", error);
            alert("로그인 또는 리소스 요청 중 문제가 발생했습니다.");
        }
    });
}

export async function generateTokenFromLogin(userInput) {
    try {
        // const response = await axios.post("http://localhost:8080/generateToken", userInput);
        const response = await axiosPost({endpoint:'/generateToken',body:userInput});

        const accessToken = response.data.accessToken;
        const refreshToken = response.data.refreshToken;
        const redirectUrl = response.data.redirectUrl;

        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);


        return { accessToken, refreshToken, redirectUrl, response };
    } catch (err) {
        console.error("로그인 과정 중 실패 :", err);
    }
}
