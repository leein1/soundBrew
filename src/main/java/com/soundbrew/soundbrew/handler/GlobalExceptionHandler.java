package com.soundbrew.soundbrew.handler;

import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.handler.custom.ResourceOwnershipException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    예외 처리에 대한 고민 더 필요 할거 같음
     */

//    응답코드 내림차순 정렬
//    비슷한 오류들 최대한 병합 - 지나친 세부 분류 X
//    클라이언트 기준 중요해 보이는 예외들 처리


    // 핸들러에서는 기본 메시지를 작성, 여기에서 내가 커스텀 메시지를 줬다면 변경하도록 작성
    private ResponseDTO<String> buildResponseDTOwithMessage(Exception ex,String defaultMessage){
//        String exceptionMessage = ex.getMessage() != null ? ex.getMessage() : "요청 데이터에 문제가 발생했습니다.";
        String exceptionMessage = ex.getMessage() != null ? ex.getMessage() : defaultMessage;

//        return createErrorResponse(HttpStatus.BAD_REQUEST, "요청 데이터에 문제가 발생했습니다.");

        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
                .message(exceptionMessage)
                .build();

        return responseDTO;
    }

    // 400 - 클라이언트측 오류
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class, IOException.class})
    public ResponseEntity<ResponseDTO<String>> handleBadRequestExceptions(Exception ex) {

//        String exceptionMessage = ex.getMessage() != null ? ex.getMessage() : "요청 데이터에 문제가 발생했습니다.";
//
////        return createErrorResponse(HttpStatus.BAD_REQUEST, "요청 데이터에 문제가 발생했습니다.");
//
//        ResponseDTO<String> responseDTO = ResponseDTO.<String>withMessage()
//                .message(exceptionMessage)
//                .build();
        String defaultMessage = "요청 데이터에 문제가 발생했습니다.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    // 404 - 리소스 찾지 못한 경우
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseDTO<String>> handleNotFoundException(NoSuchElementException ex) {

        String defaultMessage = "결과를 찾을 수 없습니다.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    // 409 - 무결성 위반
    // 클라이언트에게 필요한 정보인가...?
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<String>> handleConflictException(DataIntegrityViolationException ex) {

        String defaultMessage = "데이터 처리 중 오류가 발생했습니다.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(responseDTO);
    }

    // 500 - 데이터 베이스 관련 오류
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<ResponseDTO<String>> handleDatabaseExceptions(Exception ex) {

        String defaultMessage = "데이터 처리 중 오류가 발생했습니다.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    // 500 - 런타임 오류
    @ExceptionHandler({ConnectException.class, SocketTimeoutException.class, RuntimeException.class})
    public ResponseEntity<ResponseDTO<String>> handleRuntimeExceptions(RuntimeException ex) {

        String defaultMessage = "서버에서 알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    // 503 서버 자체 문제
//    @ExceptionHandler({ConnectException.class, SocketTimeoutException.class})
//    public ResponseEntity<ResponseDTO<String>> handleServerUnavailableExceptions(Exception ex) {
//
//        String defaultMessage = "서버 연결에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.";
//        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);
//
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDTO);
//    }
    @ExceptionHandler(ResourceOwnershipException.class)
    public ResponseEntity<ResponseDTO<String>> accessDeniedExceptions(ResourceOwnershipException ex) {

        String defaultMessage = "해당 리소스에 접근 권한이 없습니다.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDTO);
    }

    // @RequestBody와 함께 @Valid(또는 @Validated)가 적용된 객체의 바인딩 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // BindingResult에서 필드 오류 가져오기
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // fieldName과 error message를 매핑하여 반환
            errors.put(error.getField(), error.getDefaultMessage());
        });

        // errors를 하나의 문자열로 결합 (여러 오류 메시지를 이어 붙임)
        StringBuilder combinedMessage = new StringBuilder();
        errors.forEach((field, message) -> {
            combinedMessage.append(field).append(": ").append(message).append("\n");
        });

        // ResponseDTO 형태로 오류 반환
        ResponseDTO<Map<String, String>> responseDTO = ResponseDTO.<Map<String, String>>builder()
                .message(combinedMessage.toString().trim())  // 결합된 메시지 문자열
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    // BindException 처리 (ModelAttribute에서 발생하는 예외)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleBindExceptions(BindException ex) {
        Map<String, String> errors = new HashMap<>();

        // BindingResult에서 필드 오류 가져오기
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // fieldName과 error message를 매핑하여 반환
            errors.put(error.getField(), error.getDefaultMessage());
        });

        // errors를 하나의 문자열로 결합 (여러 오류 메시지를 이어 붙임)
        StringBuilder combinedMessage = new StringBuilder();
        errors.forEach((field, message) -> {
            combinedMessage.append(field).append(": ").append(message).append("\n");
        });

        // ResponseDTO 형태로 오류 반환
        ResponseDTO<Map<String, String>> responseDTO = ResponseDTO.<Map<String, String>>builder()
                .message(combinedMessage.toString().trim())  // 결합된 메시지 문자열
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

    // ConstraintViolationException (@Validated)을 사용하는 경우,개별 파라미터(@PathVariable, @RequestParam 등) 예외
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString(); // 유효성 검사 실패한 필드명
            String message = violation.getMessage(); // 오류 메시지
            errors.put(fieldName, message);
        });

        // 오류 메시지를 하나의 문자열로 결합
        StringBuilder combinedMessage = new StringBuilder();
        errors.forEach((field, message) -> {
            combinedMessage.append(field).append(": ").append(message).append("\n");
        });

        ResponseDTO<Map<String, String>> responseDTO = ResponseDTO.<Map<String, String>>builder()
                .message(combinedMessage.toString().trim()) // 결합된 메시지
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
    }

}

