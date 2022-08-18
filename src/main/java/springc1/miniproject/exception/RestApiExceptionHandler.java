package springc1.miniproject.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springc1.miniproject.controller.response.ResponseDto;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseDto<?> handleApiRequestException(IllegalArgumentException ex) {
//        new ResponseEntity(ResponseDto.fail("BAD_REQUEST",ex.getMessage()),HttpStatus.BAD_REQUEST);
        return ResponseDto.fail("BAD_REQUEST",ex.getMessage());
    }
}