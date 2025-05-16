//package com.example.eventManagement.exception;
//
//import com.epic.fc.api.util.LogFileCreator;
//import com.example.eventManagement.dto.ResponseBean;
//import com.example.eventManagement.util.MessageVarList;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.exception.ConstraintViolationException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//
//@ControllerAdvice
//@Slf4j
//public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @Value("${file.error-dir}")
//    String errorLogPath;
//    ResponseBean errorResponse;
//
//    @ExceptionHandler(DataNotFounException.class)
//    public ResponseEntity<ResponseBean> customHandleNoDataFoundException(Exception ex, WebRequest request) {
//
//        LogFileCreator.writeErrorTologs(ex, errorLogPath);
//        log.error("Error : ", ex);
//        errorResponse.setResponse(MessageVarList.RSP_NO_DATA_FOUND);
//        if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
//            errorResponse.setMessage(ex.getMessage());
//        }
//        errorResponse.setContent(null);
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
//
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ResponseBean> customHandleException(Exception ex, WebRequest request) {
//
//        LogFileCreator.writeErrorTologs(ex, errorLogPath);
//        log.error("Error : ", ex);
//        errorResponse.setStatus(MessageVarList.RSP_NO_DATA_FOUND);
//        errorResponse.setMessage(ex.getMessage());
//        errorResponse.setContent(null);
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
//
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ResponseBean> customHandleConstraintViolationException(Exception ex, WebRequest request) {
//
//        LogFileCreator.writeErrorTologs(ex, errorLogPath);
//        log.error("Error : ", ex);
//        errorResponse.setStatus(MessageVarList.RSP_NO_DATA_FOUND);
//        errorResponse.setMessage("Invalid Request");
//        errorResponse.setContent(ex.getMessage());
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
//
//    }
//
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        LogFileCreator.writeErrorTologs(ex, errorLogPath);
//        log.error("Error : ", ex);
//        StringBuilder sb = new StringBuilder();
//
//        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
//            sb.append("[").append(error.getDefaultMessage()).append("] ");
//        }
//
//        errorResponse = new ResponseBean();
//        errorResponse.setStatus(MessageVarList.RSP_NO_DATA_FOUND);
//        errorResponse.setMessage("Invalid Request");
//        errorResponse.setContent(sb.toString());
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
//    }
//
//}
