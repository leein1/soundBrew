import {router} from "/js/router.js"

export const handleResponse = (status, data, customHandlers = {}) => {
    // 상태 코드에 따른 기본 동작
    switch (status) {
        case 200:
            if (customHandlers?.onSuccess){
                customHandlers.onSuccess(data);
            }else if(data.message){
                alert(data.message);
                return;
            }
            return data;
        case 400:
            if (customHandlers?.onBadRequest) return customHandlers.onBadRequest(data);
            alert(data.message);
            break;
        case 401:
            if (customHandlers?.onUnAuthorized) return customHandlers.onUnauthorized(data);
            alert(data.message);
            break;
        case 403:
            if (customHandlers?.onForbidden) return customHandlers.onForbidden(data);
            alert(data.message);
            break;
        case 404:
            if (customHandlers?.onNotFound) return customHandlers.onNotFound(data);
            alert(data.message);
            break;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
        case 500:
            if (customHandlers?.onServerError) return customHandlers.onServerError(data);
            alert(data.message);
            break;
        case 503:
            break;
        default:
            if (customHandlers?.onDefault) return customHandlers.onDefault(data);
            console.error(`알 수 없는 상태 코드 (${status}):`, data);
            alert("예기치 않은 오류가 발생했습니다.");
            break;
    }

    // 기본적으로 null 반환
    return null;
};
