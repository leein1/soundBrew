import { axiosPost } from "/js/fetch/standardAxios.js";
import {inputHandler} from '/js/check/inputHandler.js';

export function getLoginFormProcess() {
    document.getElementById("login-form").addEventListener("submit", async (event) => {
        event.preventDefault(); // 폼의 기본 동작 막기

        const loginForm = document.getElementById("login-form");
        // submit 버튼만 선택하여 비활성화
        const submitButton = loginForm.querySelector('button[type="submit"]');
        submitButton.disabled = true;

        const username = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const userInput = { username, password };



        try {

            // const response = await axios.post("http://localhost:8080/generateToken", userInput);
            // const response = await axios.post("http://Soundvbrew-envenv.eba-gpmigkef.ap-northeast-2.elasticbeanstalk.com/generateToken", userInput);
            const response = await axios.post("http://localhost:8080/generateToken", userInput);
            console.log("generateTokenFromLogin - " + response.status); //확인용

            const accessToken = response.data.accessToken;
            const refreshToken = response.data.refreshToken;

            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);

            // alert(response.data.redirectUrl);
            window.location.href = response.data.redirectUrl;

        } catch (error) {

            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");

            alert(
                error.response.data.message
                // + "\n" +  error.response.data.redirectUrl
                // + "\n" + error.response.data.resetToken
            );

            // submitButton.disabled = false;

            const resetToken = error.response.data.resetToken

            localStorage.setItem("resetToken",resetToken);

            window.location.href = error.response.data.redirectUrl;
        }
    });
}

// export async function generateTokenFromLogin(userInput) {
//     try {
//         const response = await axios.post("http://localhost:8080/generateToken", userInput);
//         // const response = await axiosPost({endpoint:'/generateToken',body:userInput});
//
//         console.log("generateTokenFromLogin - " + response.status);
//
//         const accessToken = response.data.accessToken;
//         const refreshToken = response.data.refreshToken;
//         const redirectUrl = response.data.redirectUrl;
//
//         localStorage.setItem("accessToken", accessToken);
//         localStorage.setItem("refreshToken", refreshToken);
//
//
//         return { accessToken, refreshToken, redirectUrl, response };
//     } catch (err) {
//         console.error("로그인 과정 중 실패 :", err);
//     }
// }
