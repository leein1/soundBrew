package com.soundbrew.soundbrew.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final BUSINESS_ERROR error;

    public BusinessException(BUSINESS_ERROR error) {
        // 개발자용 상세 메시지를 super()로 전달하여 로그 등에서 활용할 수 있도록 합니다.
        super(error.getDeveloperMessage());
        this.error = error;
    }

    public HttpStatus getStatus() {
        return error.getStatus();
    }

    // 개발자용 상세 메시지 (로그 기록 등 내부 진단용)
    public String getDeveloperMessage() {
        return error.getDeveloperMessage();
    }

    // 클라이언트용 일반화된 메시지 (응답용)
    public String getClientMessage() {
        return error.getClientMessage();
    }

    // 비즈니스 예외에서는 되도록 400, 404의 예외를 일괄적으로 응답하는 방식으로 작성하였다.
    @Getter
    public enum BUSINESS_ERROR {
        RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,
                "요청하신 정보를 찾을 수 없습니다.",
                "요청 리소스가 존재하지 않습니다."),

        RESOURCE_NOT_ACCESS(HttpStatus.BAD_REQUEST,
                "해당 정보를 열람할 수 없습니다.",
                "리소스 접근에 대한 적절한 권한이 없습니다."),

        // 얘는 특히 db측에서 예외가 아니라, 특정 값을 저장할때, 우선적으로 findById를 해서 우선 선 검사를 할때
        // 사용할 "사용자의 입력값에 대한 중복 데이터" 예외
        RESOURCE_DUPLICATE(HttpStatus.BAD_REQUEST,
                "이미 존재하는 데이터입니다.",
                "중복된 리소스를 생성하려고 했습니다."),

        // 아래는 핸들링을 하지 않는 비즈니스 예외
        // 아래는 핸들링을 하지 않는 비즈니스 예외
        // 아래는 핸들링을 하지 않는 비즈니스 예외
        INVALID_ARGUMENT(HttpStatus.BAD_REQUEST,
                "입력값이 올바르지 않습니다. 다시 확인해주세요.",
                "입력값이 올바르지 않습니다."),

        INVALID_TYPE(HttpStatus.BAD_REQUEST,
                "잘못된 요청 형식입니다. 입력값의 타입을 확인해주세요.",
                "요청 파라미터의 타입이 일치하지 않습니다."),

        INVALID_VALID(HttpStatus.BAD_REQUEST,
                "입력값이 유효하지 않습니다. 필수 항목을 확인하거나 형식을 맞춰주세요.",
                "요청의 유효성 검사에서 문제가 발생했습니다. @Valid, @Validated"),

        INVALID_VALIDATED(HttpStatus.BAD_REQUEST,
                "입력값이 유효하지 않습니다. 필수 항목을 확인하거나 형식을 맞춰주세요.",
                "요청의 유효성 검사에서 문제가 발생했습니다. @ModelAttribute, @RequestParam"),

        UNEXPECTED_BIND(HttpStatus.BAD_REQUEST,
                "입력값을 처리할 수 없습니다. 필드 형식이 맞는지 확인해주세요.",
                "요청의 유효성 검사에서 문제가 발생했습니다. (바인딩)");

        private final HttpStatus status;
        private final String developerMessage;
        private final String clientMessage;

        BUSINESS_ERROR(HttpStatus status, String clientMessage, String developerMessage) {
            this.status = status;
            this.clientMessage = clientMessage;
            this.developerMessage = developerMessage;
        }

    }
}
