const BASE_URL = "http://localhost:8080";

// Axios 기본 설정
const axiosInstance = axios.create({
    baseURL: BASE_URL,
    // headers: { 'Content-Type': 'application/json' },
});

// 토큰 재발급 함수
const callRefresh = async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    const accessToken = localStorage.getItem('accessToken');
    const tokens = {accessToken,refreshToken}

    try {
        // 여기서 우리가 만든 axiosPost를 안쓰는이유는? => 쓰면 axiosPost함수의 addAuthHeader로 무한 루프 걸릴 수 있음
        const response = await axiosInstance.post('/refreshToken', tokens);
        const newAccessToken = response.data.accessToken;

        localStorage.setItem("accessToken", response.data.accessToken);
        localStorage.setItem("refreshToken", response.data.refreshToken);

        return newAccessToken;
    } catch (error) {
        console.error("토큰 재발급 실패:", error);
        throw new Error("로그인이 필요합니다.");
    }
};

// 토큰 포함된 요청 처리 유틸리티
const addAuthHeader = async (options) => {
    let token = localStorage.getItem("accessToken");

    if (!token) {
        alert("액세스 토큰이 없습니다, 다시 로그인을 진행해주세요");
        window.location.href = "/login"; // 로그인 페이지로 리다이렉트
        return null;
    }

    options.headers = { Authorization: `Bearer ${token}` };

    try {
        const response = await axiosInstance(options);
        return response.data;
    } catch (error) {
        if (error.response?.status === 401) {
            console.log("만료된 액세스 토큰, 재발급 시도 중...");
            try {
                const newToken = await callRefresh();
                options.headers = { Authorization: `Bearer ${newToken}` };
                const retryResponse = await axiosInstance(options); // 재요청
                return retryResponse.data;
            } catch (refreshError) {
                alert("인증 정보가 유효하지 않습니다. 다시 로그인하세요.");
                window.location.href = "/login"; // 로그인 페이지로 리다이렉트
                throw refreshError;
            }
        } else {
            throw error; // 다른 에러는 그대로 던짐
        }
    }
};

// API 요청 처리 함수
const fetchData = async ({ endpoint, body = null, useToken = false, params = {}, method = 'GET' }) => {
    const options = {
        headers: { 'Content-Type': 'application/json' },
        method,
        url: endpoint,
        params,
        data: body,
    };

    // FormData인 경우 Content-Type을 설정하지 않도록 함
    if (body instanceof FormData) {
        delete options.headers['Content-Type']; // FormData는 자동으로 처리하므로 Content-Type을 삭제
    }

    // 토큰 사용 여부에 따라 처리
    if (useToken) {
        return await addAuthHeader(options);
    }

    try {
        const response = await axiosInstance(options);
        return response.data;
    } catch (error) {
        if (error.response) {
            return error.response; // 서버 응답 반환
        } else {
            console.error("네트워크 오류:", error.message);
            throw error;
        }
    }
};

// 각 HTTP 메서드별 래퍼 함수
export const axiosGet = async ({ endpoint, useToken = false, params = {} }) =>
    fetchData({ endpoint, useToken, params, method: 'GET' });

export const axiosPost = async ({ endpoint, body = {}, useToken = false, params = {} }) =>
    fetchData({ endpoint, body, useToken, params, method: 'POST' });

export const axiosDelete = async ({ endpoint, body = {}, useToken = false, params = {} }) =>
    fetchData({ endpoint, body, useToken, params, method: 'DELETE' });

export const axiosPatch = async ({ endpoint, body = {}, useToken = false, params = {} }) =>
    fetchData({ endpoint, body, useToken, params, method: 'PATCH' });

export const axiosPut = async ({ endpoint, body = {}, useToken = false, params = {} }) =>
    fetchData({ endpoint, body, useToken, params, method: 'PUT' });
