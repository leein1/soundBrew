package com.soundbrew.soundbrew.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class TechnicException extends RuntimeException {
    private final TECHNIC_ERROR error;

    public TechnicException(TECHNIC_ERROR error) {
        // 개발자용 상세 메시지를 super()로 전달합니다.
        super(error.getDeveloperMessage());
        this.error = error;
    }

    public HttpStatus getStatus() {
        return error.getStatus();
    }

    // 개발자용 상세 메시지 (로그에 활용)
    public String getDeveloperMessage() {
        return error.getDeveloperMessage();
    }

    // 클라이언트용 일반화된 메시지 (응답 메시지)
    public String getClientMessage() {
        return error.getClientMessage();
    }

    @Getter
    public enum TECHNIC_ERROR {
        IO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE,
                "파일 크기가 너무 큽니다. 제한된 크기를 확인해주세요.",
                "업로드 파일 크기가 허용된 제한을 초과했습니다."),

        // 개발자가 핸들링 하지않는 테크닉 예외
        // 개발자가 핸들링 하지않는 테크닉 예외
        // 개발자가 핸들링 하지않는 테크닉 예외
        METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,
                "잘못된 요청입니다. 요청 방식을 확인해주세요.",
                "허용되지 않은 HTTP 메서드 요청입니다.")

        /* 네트워크/IO 관련 예외 (HTTP 500 또는 504 등) */,
        IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,
                "네트워크 또는 입출력 처리 중 문제가 발생했습니다.",
                "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
        CONNECT_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT,
                "네트워크 통신 시간 초과가 발생했습니다.",
                "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
        CONNECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 연결에 실패했습니다.",
                "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

        /* 데이터베이스 관련 예외 (HTTP 409, 500) */
        DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT,
                "데이터 무결성 위반이 발생했습니다.",
                "데이터 처리 중 문제가 발생했습니다."),
        DATA_SQL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,
                "데이터베이스 작업 중 문제가 발생했습니다.",
                "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
        DATA_ACCESS_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,
                "데이터 접근 중 문제가 발생했습니다.",
                "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

        /* 예측하지 못한 일반 런타임 예외 (HTTP 500) */
        UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
                "예기치 않은 시스템 오류가 발생했습니다.",
                "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");

        private final HttpStatus status;
        private final String developerMessage;
        private final String clientMessage;

        TECHNIC_ERROR(HttpStatus status, String developerMessage, String clientMessage) {
            this.status = status;
            this.developerMessage = developerMessage;
            this.clientMessage = clientMessage;
        }

    }
}
