package com.soundbrew.soundbrew.handler;

import com.soundbrew.soundbrew.dto.ResponseDTO;
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
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

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
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<String>> handleRuntimeExceptions(RuntimeException ex) {

        String defaultMessage = "서버에서 알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    // 503 서버 자체 문제
    @ExceptionHandler({ConnectException.class, SocketTimeoutException.class})
    public ResponseEntity<ResponseDTO<String>> handleServerUnavailableExceptions(Exception ex) {

        String defaultMessage = "서버 연결에 문제가 발생했습니다. 잠시 후 다시 시도해주세요.";
        ResponseDTO responseDTO = buildResponseDTOwithMessage(ex,defaultMessage);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDTO);
    }
}

