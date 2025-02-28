import { router } from '/js/router.js';
import { globalStateManager } from '/js/globalState.js';

// 코드를 보면 성공/실패 분기만 있다.
// handle에  success, failure만 있다는 것.
// 특히 저 성공/실패는 프론트측 개발자가, const handle 변수를 매개변수로 넘겼을때만 존재한다는 뜻이다.
// 그게 아니라면 이전의 버전인 data를 받게 되고, 그 안에는 평범하게 globalException, security에서 뿌린 코드, 응답 등이 있다.
export const handleResponse = (status, data, handle) => {
    switch (status) {
        // 200번: 성공 – handle.success가 있으면 alert와 화면전환을 수행, 없으면 data.message가 있을 경우 alert 후 반환
        case 200:
            if (handle?.success) {
                if (handle.success.message) alert(handle.success.message);
                if (handle.success.navigate){
                    router.navigate(handle.success.navigate);
                }else{
                    router.navigate('/sounds/tracks');
                }
            }
            else if (data.message) {
                alert(data.message+"!!");
            }
            return data;

        // 400, 401, 404, 405, 500, 503, 504 등 실패 관련 상태
        //503은 우리가 따로 리다이렉트url을 주던가?
        case 400:
        case 401:
        case 404:
        case 405:
        case 500:
        case 503:
        case 504:
        case 403:
            // 개발자가 handle을 통해 특수한 액션을 취하고자 할때. (insert, update, delete ...)
            if (handle?.failure) {
                if (handle.failure.message){
                    alert(handle.failure.message);
                }else{
                    alert(data.message);
                }
                if (handle.failure.navigate) {
                    router.navigate(handle.failure.navigate);
                }else{
                    window.history.back();
                }

            // 개발자가 handle을 사용하지 않은, 디폴트 액션일때. ( 일반적인 GET)
            } else {
                alert(data.message);
                if(data.redirectUrl){
                    window.location.href = data.redirectUrl;
                }else{
                    window.history.back();
                }
            }
            break;

        // 그 외 예상하지 못한 상태 코드
        default:
            console.error(`알 수 없는 상태 코드 (${status}):`, data);
            alert("예기치 않은 오류가 발생했습니다.");
            if (handle?.failure?.navigate) router.navigate(handle.failure.navigate);
            break;
    }
    return null;
};














//import { globalStateManager } from '/js/globalState.js';
//
//export const handleResponse = (status, data, customHandlers = {}) => {
//    alert("핸들 리스폰스 진입 !");
//    alert("globalStateManager.getState().currentView : "+globalStateManager.getState().currentView);
//
//    const 미리선언해둔화면전환포인트:{
//        유저정보업데이트 : '/.../....',
//        신규음원등록 : '/.../...',
//        유저구독제업데이트 :'/.../...'
//    }
//
//    switch (status) {
//        case 200:
//            if (customHandlers?.onSuccess) {
//                customHandlers.onSuccess(data);
//            } else if (data.message) {
//                alert(data.message);
//                return;
//            }
//            return data;
//        case 400:
//            if (customHandlers?.onBadRequest) return customHandlers.onBadRequest(data);
//            alert(data.message);
//            break;
//        case 401:
//            if (customHandlers?.onUnAuthorized) return customHandlers.onUnAuthorized(data);
//            alert(data.message);
//            break;
//        case 403:
//            if (customHandlers?.onForbidden) return customHandlers.onForbidden(data);
//            alert(data.message);
//            window.location.href=data.redirectUrl;
//            break;
//        case 404:
//            if (customHandlers?.onNotFound) return customHandlers.onNotFound(data);
//            alert(data.message);
//            break;
//        case 500:
//            if (customHandlers?.onServerError) return customHandlers.onServerError(data);
//            alert(data.message);
//            break;
//        case 503:
//            break;
//        default:
//            if (customHandlers?.onDefault) return customHandlers.onDefault(data);
//            console.error(`알 수 없는 상태 코드 (${status}):`, data);
//            alert("예기치 않은 오류가 발생했습니다.");
//            break;
//    }
//    return null;
//};
