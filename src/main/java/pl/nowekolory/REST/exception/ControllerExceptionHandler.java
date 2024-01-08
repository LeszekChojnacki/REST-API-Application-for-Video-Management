package pl.nowekolory.REST.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @ExceptionHandler(ResponseBodyFormatException.class)
    public ResponseEntity<Object> ResponseBodyFormatException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
