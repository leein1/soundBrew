
const BASE_URL = "http://localhost:8080";
// Axios 기본 설정
// ** create는 기본적으로 request를 반환한다. **request는 StringBuilder처럼 내가 주는 필드(option)을 덧붙인다.
const axiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

export const fetchData = async (endpoint, method = 'GET', body = null, useToken = false, params = {}) => {
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
        // 요청 전송 및 응답 반환
        const response = await axiosInstance(options);
        return response.data; // 서버에서 보낸 ResponseEntity의 body를 반환
    } catch (error) {
        // 예상치 못한 에러 처리
        console.error('통신 중 오류 발생:', error.message);

        // 응답이 있는 경우 (서버 에러일 때도 응답을 그대로 반환)
        if (error.response) {
            return error.response.data; // 서버에서 보낸 응답 데이터를 그대로 반환
        }

        // 기타 네트워크 에러 (예: 인터넷 연결 문제)
        throw new Error('네트워크 오류 또는 서버가 응답하지 않습니다.');
    }
};