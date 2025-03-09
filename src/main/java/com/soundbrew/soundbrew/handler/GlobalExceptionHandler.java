package com.soundbrew.soundbrew.handler;

import com.soundbrew.soundbrew.dto.ExceptionResponseDTO;
import com.soundbrew.soundbrew.security.exception.AccessTokenException;
import com.soundbrew.soundbrew.security.exception.RefreshTokenException;
import org.codehaus.groovy.syntax.TokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponseDTO> handleBusinessException(BusinessException ex) {
        logger.warn("Business Exception(Throw) 발생: {}. 상세정보: {}", ex.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(ex.getStatus(), ex.getClientMessage(), ex.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(TechnicException.class)
    public ResponseEntity<ExceptionResponseDTO> handleTechnicException(TechnicException ex) {
        logger.error("Technic Exception(Throw) 발생: {}. 상세정보: {}", ex.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(ex.getStatus(), ex.getClientMessage(), ex.getStatus().getReasonPhrase());
    }
    




//    @ExceptionHandler(AccessTokenException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleAccessTokenException(AccessTokenException ex){
//        logger.warn("AccessToken Exception(Throw) 발생:{}. 상세정보:{}", ex.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(ex.getStatus(), ex.getClientMessage(), ex.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(RefreshTokenException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleRefrashTokenException(RefreshTokenException ex){
//        logger.warn("RefreshToken Exception(Throw) 발생:{}. 상세정보:{}", ex.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(ex.getStatus(), ex.getClientMessage(), ex.getStatus().getReasonPhrase());
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleAllExceptions(Exception ex) throws Exception {
        if (ex instanceof AccessDeniedException) {
            throw ex;
        }

        BusinessException businessException = null;
        TechnicException technicException = null;

        if (ex instanceof MethodArgumentNotValidException || ex instanceof ConstraintViolationException) {
            businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_VALID);
            logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}", businessException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof MethodArgumentTypeMismatchException || ex instanceof HttpMessageNotReadableException) {
            businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_TYPE);
            logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof TypeMismatchException) {
            businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_VALIDATED);
            logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}", businessException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof BindException) {
            businessException = new BusinessException(BusinessException.BUSINESS_ERROR.UNEXPECTED_BIND);
            logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}", businessException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.METHOD_NOT_ALLOWED);
            logger.error("Technic Exception(Not Throw) 발생1: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof SocketTimeoutException) {
            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.CONNECT_TIMEOUT);
            logger.error("Technic Exception(Not Throw) 발생2: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof ConnectException) {
            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.CONNECT_EXCEPTION);
            logger.error("Technic Exception(Not Throw) 발생3: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof IOException) {
            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.IO_EXCEPTION);
            logger.error("Technic Exception(Not Throw) 발생4: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof DataIntegrityViolationException) {
            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_INTEGRITY_VIOLATION);
            logger.error("Technic Exception(Not Throw) 발생5: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof SQLException) {
            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_SQL_EXCEPTION);
            logger.error("Technic Exception(Not Throw) 발생6: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
        } else if (ex instanceof DataAccessException) {
            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_ACCESS_EXCEPTION);
            logger.error("Technic Exception(Not Throw) 발생7: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
        }
//        } else {
//            technicException = new TechnicException(TechnicException.TECHNIC_ERROR.UNEXPECTED_ERROR);
//            logger.error("Technic Exception(Not Throw) 발생8: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//        }

        if(businessException != null){
            return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
        }else if(technicException != null){
            return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
        }else{
            logger.error("GLOBAL EXCEPTION HANDLER 에서 조건에 없는(예상하지 못한) 예외 발생: {}. 상세정보: {}",ex.getMessage(),ex.toString(),ex);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(),ex.toString());
        }
    }

    private ResponseEntity<ExceptionResponseDTO> buildErrorResponse(HttpStatus status, String clientMessage, String errorReason) {
        ExceptionResponseDTO response = ExceptionResponseDTO.builder()
                .status(status.value())
                .error(errorReason)
                .message(clientMessage)
                .build();
        return new ResponseEntity<>(response, status);
    }

//    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
//    public ResponseEntity<ExceptionResponseDTO> handleInvalidValidException(Exception ex) {
//        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_VALID);
//        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);
//
//        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
//    public ResponseEntity<ExceptionResponseDTO> handleInvalidTypeException(Exception ex) {
//        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_TYPE);
//        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);
//
//        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(TypeMismatchException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleInvalidValidatedException(TypeMismatchException ex) {
//        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_VALIDATED);
//        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);
//
//        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleUnexpectedBindException(BindException ex) {
//        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.UNEXPECTED_BIND);
//        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);
//
//        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.METHOD_NOT_ALLOWED);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleIOException(IOException ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.IO_EXCEPTION);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(SocketTimeoutException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleSocketTimeoutException(SocketTimeoutException ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.CONNECT_TIMEOUT);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(ConnectException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleConnectException(ConnectException ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.CONNECT_EXCEPTION);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_INTEGRITY_VIOLATION);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleSQLException(SQLException ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_SQL_EXCEPTION);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(DataAccessException.class)
//    public ResponseEntity<ExceptionResponseDTO> handleDataAccessException(DataAccessException ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_ACCESS_EXCEPTION);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionResponseDTO> handleUnexpectedException(Exception ex) {
//        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.UNEXPECTED_ERROR);
//        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);
//
//        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
//    }



}
