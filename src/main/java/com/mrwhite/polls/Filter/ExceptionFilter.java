package com.mrwhite.polls.Filter;

import com.mrwhite.polls.Exception.PollException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;

@ControllerAdvice
@Slf4j
public class ExceptionFilter {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex) throws Exception {
        if (ex instanceof PollException) {
            HttpStatus httpStatus = ((PollException) ex).getCode().getHttpStatus();
            String errorResponse = ex.getMessage();
            return ResponseEntity.status(httpStatus).body(errorResponse);
        }
        else if(ex instanceof ServletException){
            String errorResponse = ex.getMessage();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        else {
            throw ex;
        }
    }
}
