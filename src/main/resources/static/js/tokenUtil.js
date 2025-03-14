export default class TokenUtil {
    // localStorage에서 토큰을 가져옴 (기본 키: 'accessToken')
    static getToken(key = 'accessToken') {
        return localStorage.getItem(key) || null;
    }

   // Base64 URL -> Base64 변환
    static base64UrlToBase64(base64Url) {
        return base64Url.replace(/-/g, '+').replace(/_/g, '/');
    }

    // UTF-8 디코딩을 고려한 JWT 디코딩 함수
    static decodeToken(token) {
        try {
            const payloadBase64Url = token.split('.')[1];
            const payloadBase64 = this.base64UrlToBase64(payloadBase64Url);
            const decodedStr = atob(payloadBase64); // Base64 디코딩
            const utf8DecodedStr = decodeURIComponent(escape(decodedStr)); // UTF-8 디코딩
            return JSON.parse(utf8DecodedStr);
        } catch (error) {
            console.error("토큰 디코딩 실패:", error);
            return null;
        }
    }

    // 토큰의 만료 여부를 체크 (만료 시간(exp) 필드를 사용)
    // ** 토큰이 expired라면?, 로그인의 연계로 refresh토큰을 통해서 재발급을 하고 그럼에도 문제가 발생한다면, 다시 로그인을 하는것이 맞을꺼 같다.
    // 해당 재발급 시점을 잘 생각해보자.
    // 토큰에서 확인을 한다면? 이후의 행동때 또 expired가 발생할 수 있으니까. 잘 생각해보자.
    static isTokenExpired(token) {
        const decoded = this.decodeToken(token);
        if (!decoded || !decoded.exp) return true;
        const now = Date.now() / 1000;
        return decoded.exp < now;
    }

    // 토큰에서 사용자 정보를 추출하여 반환 (예: userId, roles, nickname)
    static getUserInfo(token) {
        const decoded = this.decodeToken(token);
        if (!decoded) return null;
        // JWT payload의 필드 이름은 상황에 따라 다를 수 있으니, 실제 토큰 구조에 맞게 수정 필요
        const userId = decoded.sub || decoded.userId;
        const roles = decoded.roles;
        const nickname = decoded.nickname;
        const profileImagePath = decoded.profileImagePath
        return { userId, roles, nickname, profileImagePath };
    }

    // 토큰의 유효성을 검사 (토큰 존재 여부 및 만료 여부 체크)
    static validateToken(key = 'accessToken') {
        const token = this.getToken(key);
        if (!token) return { valid: false, reason: "No token found" };
        if (this.isTokenExpired(token)) return { valid: false, reason: "Token expired" };
        return { valid: true, reason: "Token is valid" };
    }
}
