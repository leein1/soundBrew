import { router } from '/js/router.js';
import { globalStateManager } from '/js/globalState.js';

export const handleResponse = async (status, data, handle) => {
    switch (status) {
        // 200번: 성공 – handle.success가 있으면 alert와 화면전환, callback 실행 등 처리
        case 200: {
            const { message, navigate, location, callback } = handle?.success || {};

            // handle.success가 있을 때
            if (handle?.success) {
                if (message) alert(message);
                else alert(data.message);

                // callback이 있을 경우 실행 (UI 업데이트 등 추가 동작)
                if (typeof callback === 'function') {
                    await callback(data);
                }

                if (location) window.location.href = location;
                if (navigate) router.navigate(navigate);

                // 아무 동작도 하지 않은 경우 기본 경로로 이동
                if (!location && !navigate) router.navigate('/sounds/tracks');
            }
            // handle.success가 없을 때
            else if (data.message) {
                alert(data.message + "!!");
            }

            return data;
        }

        // 400, 401, 404, 405, 500, 503, 504 등 실패 관련 상태
        case 400:
        case 401:
        case 404:
        case 405:
        case 500:
        case 503:
        case 504:
            const { message, navigate, location, callback } = handle?.failure || {};
            if (handle?.failure) {
                if (message) alert(message);
                else alert(data.message);

                // callback이 있을 경우 실행 (UI 업데이트 등 추가 동작)
                if (typeof callback === 'function') {
                    await callback(data);
                }

                if (location) window.location.href = location;
                if (navigate) router.navigate(navigate);

                // 아무 동작도 하지 않은 경우 기본 경로로 이동
                if (!location && !navigate) window.history.back();
            } else {
                alert(data.message);
                window.history.back();
            }
            break;

        case 403:
            alert(data.message);
            window.location.href = '/login';
            break;

        // 그 외 예상하지 못한 상태 코드
        default:
            alert("예기치 않은 오류가 발생했습니다.");
            console.error(`알 수 없는 상태 코드 (${status}):`, data);
            break;
    }
};
