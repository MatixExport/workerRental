package spring.controllers.exception.handlers;

import exceptions.RentAlreadyEndedException;
import exceptions.ResourceNotFoundException;
import exceptions.WorkerRentedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.security.JWTException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorkerRentedException.class)
    public ResponseEntity<String> handleWorkerRentedException(WorkerRentedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RentAlreadyEndedException.class)
    public ResponseEntity<String> handleRentAlreadyEndedException(RentAlreadyEndedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JWTException.class)
    public ResponseEntity<String> handleJWTException(JWTException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
