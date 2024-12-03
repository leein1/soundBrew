// standardResponse.js
export const handleErrorResponse = (response, errorData) => {
    alert(`Error ${errorData.code}: ${errorData.message}`);

    switch (response.status) {
        case 400:
            window.history.back();
            break;
        case 401:
            window.location.href = '/login';
            break;
        case 404:
            window.location.href = '/';
            break;
        case 500:
            alert("서버에서 오류가 발생했습니다.");
            break;
        default:
            alert(`예기치 못한 오류: ${response.status}`);
    }
};
