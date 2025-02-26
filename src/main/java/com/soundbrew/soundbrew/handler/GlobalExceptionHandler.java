package com.soundbrew.soundbrew.handler;

import com.soundbrew.soundbrew.dto.ExceptionResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    private ResponseEntity<ExceptionResponseDTO> buildErrorResponse(HttpStatus status, String clientMessage, String errorReason) {
        ExceptionResponseDTO response = ExceptionResponseDTO.builder()
                .status(status.value())
                .error(errorReason)
                .message(clientMessage)
                .build();
        return new ResponseEntity<>(response, status);
    }

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

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponseDTO> handleInvalidValidException(Exception ex) {
        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_VALID);
        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);

        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponseDTO> handleInvalidTypeException(Exception ex) {
        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_TYPE);
        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);

        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionResponseDTO> handleInvalidValidatedException(TypeMismatchException ex) {
        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.INVALID_VALIDATED);
        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);

        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponseDTO> handleUnexpectedBindException(BindException ex) {
        BusinessException businessException = new BusinessException(BusinessException.BUSINESS_ERROR.UNEXPECTED_BIND);
        logger.warn("Business Exception(Not Throw) 발생: {}. 상세정보: {}",businessException.getDeveloperMessage() , ex.toString(), ex);

        return buildErrorResponse(businessException.getStatus(), businessException.getClientMessage(), businessException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.METHOD_NOT_ALLOWED);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponseDTO> handleIOException(IOException ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.IO_EXCEPTION);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<ExceptionResponseDTO> handleSocketTimeoutException(SocketTimeoutException ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.CONNECT_TIMEOUT);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ExceptionResponseDTO> handleConnectException(ConnectException ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.CONNECT_EXCEPTION);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_INTEGRITY_VIOLATION);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ExceptionResponseDTO> handleSQLException(SQLException ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_SQL_EXCEPTION);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDataAccessException(DataAccessException ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.DATA_ACCESS_EXCEPTION);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleUnexpectedException(Exception ex) {
        TechnicException technicException = new TechnicException(TechnicException.TECHNIC_ERROR.UNEXPECTED_ERROR);
        logger.error("Technic Exception(Not Throw) 발생: {}. 상세정보: {}", technicException.getDeveloperMessage(), ex.toString(), ex);

        return buildErrorResponse(technicException.getStatus(), technicException.getClientMessage(), technicException.getStatus().getReasonPhrase());
    }
}
