const BASE_URL = "http://localhost:8080";
// Axios 기본 설정

const axiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

const fetchData = async ({ endpoint, body = null, useToken = false, params = {}, method = 'GET' }) => {
    const options = {
        method,
        url: endpoint,
        params,
        data: body,
    };

    // 토큰 처리
    if (useToken) {
        const token = localStorage.getItem('authToken');
        if (token) {
            options.headers = { Authorization: `Bearer ${token}` };
        }
    }

    try {
        const response = await axiosInstance(options);
        return response.data; // 서버에서 보낸 ResponseEntity의 body를 반환
    } catch (error) {
            // 응답이 있는 경우 (서버 에러일 때도 응답을 그대로 반환)
        if (error.response) {
            return error.response; // 서버에서 보낸 응답 데이터를 그대로 반환
        }else {
            // 네트워크 오류 등
            console.error("네트워크 오류:", error.message);
        };
    }
};

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

