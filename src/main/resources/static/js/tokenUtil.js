export default class TokenUtils {
  static getToken(key = '우리가 정한 토큰 명') {
    return localStorage.getItem(key) || null;
  }

  static decodeToken(token) {
    try {
      const payload = token.split('.')[1];
      const decoded = atob(payload);
      return JSON.parse(decoded);
    } catch (error) {
      console.error("토큰 디코딩 실패:", error);
      return null;
    }
  }

  static isTokenExpired(token) {
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return true;
    const now = Date.now() / 1000;
    return decoded.exp < now;
  }

  static getUserInfo(token) {
    const decoded = this.decodeToken(token);
    if (!decoded) return null;
    const { sub: 유저아이디, 룰, 아티스트명(닉네임) } = decoded;
    return { userId, roles, nickname };
  }

  static validateToken(key = '우리가 정한 토큰 명') {
    const token = this.getToken(key);
    if (!token) return { valid: false, reason: "No token found" };
    if (this.isTokenExpired(token)) return { valid: false, reason: "Token expired" };
    return { valid: true, reason: "Token is valid" };
  }
}
