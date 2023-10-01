package io.rainett.testtask.controller;

import io.rainett.testtask.dto.ValidationErrorDto;
import io.rainett.testtask.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ValidationErrorDto validationErrorDto = new ValidationErrorDto();
        bindingResult.getFieldErrors()
                .forEach(e -> validationErrorDto.addError(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(validationErrorDto);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleAppException(AppException ex) {
        log.error("Received an app exception: ", ex);
        return ResponseEntity.status(ex.code).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        log.error("Received an exception: ", ex);
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }


}
