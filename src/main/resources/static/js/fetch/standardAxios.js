import { BASE_URL } from '/js/fetch/baseUrls.js';
import { handleErrorResponse } from '/js/fetch/standardResponse.js';

// Axios 기본 설정
// ** create는 기본적으로 request를 반환한다. **request는 StringBuilder처럼 내가 주는 필드(option)을 덧붙인다.
const axiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: { 'Content-Type': 'application/json' },
});

export const fetchData = async (endpoint, method = 'GET', body = null, useAuth = false, params = {}) => {
    try {
        const options = {
            method,
            url: endpoint,
            data: body,
            params,
        };

        if (useAuth) {
            const token = localStorage.getItem('authToken');
            if (token) {
                options.headers = { Authorization: `Bearer ${token}` };
            }
        }

        const response = await axiosInstance(options);
        if (response.status === 204) {
            alert("찾으시는 정보가 없습니다.");
            return ;
        }
        return response.data;
    } catch (error) {
        if (error.response) {
            handleErrorResponse(error.response, error.response.data);
        } else {
            console.error('통신 중 오류 발생 : ', error.message);
        }
        throw error;
    }
};
