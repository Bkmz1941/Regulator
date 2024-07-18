package ru.bkmz1994.http.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.bkmz1994.http.dto.Response;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler()
    public ResponseEntity<Object> handle(Exception ex) {
        Response<Object> response = Response.builder().message(ex.getMessage()).build();
        return new ResponseEntity<Object>(response, HttpStatus.TOO_MANY_REQUESTS);
    }
}
