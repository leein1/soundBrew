package com.soundbrew.soundbrew.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataAccessException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

//    응답코드 내림차순 정렬
//    비슷한 오류들 최대한 병합 - 지나친 세부 분류 X
//    클라이언트 기준 중요해 보이는 예외들 처리

    // 400 - 클라이언트측 오류
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class, IOException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequestExceptions(Exception ex) {

        return createErrorResponse(HttpStatus.BAD_REQUEST, "요청 데이터에 문제가 발생했습니다.");
    }

    // 404 - 리소스 찾지 못한 경우
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NoSuchElementException ex) {

        return createErrorResponse(HttpStatus.NOT_FOUND, "결과를 찾을 수 없습니다.");
    }

    // 409 - 무결성 위반
    // 클라이언트에게 필요한 정보인가...?
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(DataIntegrityViolationException ex) {

        return createErrorResponse(HttpStatus.CONFLICT, "데이터 처리 중 오류가 발생했습니다.");
    }

    // 500 - 데이터 베이스 관련 오류
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<Map<String, Object>> handleDatabaseExceptions(Exception ex) {

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "데이터 처리 중 오류가 발생했습니다.");
    }

    // 500 - 런타임 오류
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeExceptions(RuntimeException ex) {

        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도홰주세요.");
    }

    // 503 서버 자체 문제
    @ExceptionHandler({ConnectException.class, SocketTimeoutException.class})
    public ResponseEntity<Map<String, Object>> handleServerUnavailableExceptions(Exception ex) {

        return createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "서버 연결에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }

    // 각 핸들러에서 반환시 return ResponseEntity.~~.build()가 불편함 & 예외처리 로직 변경 가능성 있으므로 공통 응답 생성 메서드 사용
    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message) {

        //  한번 넣은 값 수정(삭제) 불가능, 불가능해야 할것 같아서 put 대신 of 사용
        Map<String, Object> errorResponse = Map.of(

                //  int
                "code", status.value(),

                //  String
                "message", message
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

}

