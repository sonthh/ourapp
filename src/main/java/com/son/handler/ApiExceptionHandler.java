package com.son.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // error handler for @Valid @RequestBody Dto
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status, WebRequest request
    ) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> {
//                return x.getObjectName() + "." + x.getField() + ": " + x.getDefaultMessage();
                    return x.getField() + ": " + x.getDefaultMessage();

                })
                .collect(Collectors.toList());

        ApiExceptionResponse body = new ApiExceptionResponse();
        body.setErrors(errors);
        body.setStatus(status.value());

        return new ResponseEntity<>(body, headers, status);
    }

    // error handler for PathVariable RequestParam
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(
            ConstraintViolationException ex
    ) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(each -> {
                    String[] arrayPropertyPath = each.getPropertyPath().toString().split("\\.");

                    if (arrayPropertyPath.length > 0) {
                        return arrayPropertyPath[arrayPropertyPath.length - 1] + ": " + each.getMessage();
                    }

                    return each.getPropertyPath() + ": " + each.getMessage();
                })
                .collect(Collectors.toList());

        ApiExceptionResponse body = new ApiExceptionResponse();
        body.setErrors(errors);
        body.setStatus(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    // error handle @PathVariable
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handle(MethodArgumentTypeMismatchException ex) {
        String error = ex.getName() + ": " + ex.getMessage();

        if (ex.getCause() instanceof NumberFormatException) {
            error = ex.getName() + ": must match number format ("
                    + ex.getCause().getClass().getSimpleName() + ")";
        }

        ApiExceptionResponse body = new ApiExceptionResponse();
        body.setErrors(error);
        body.setStatus(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> apiException(ApiException ex) {
        ApiExceptionResponse body = new ApiExceptionResponse();
        body.setErrors(ex.getMessage());
        body.setStatus(ex.getStatus());

        return new ResponseEntity<>(body, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> apiException(AccessDeniedException ex) {
        ApiExceptionResponse body = new ApiExceptionResponse();
        body.setErrors("Access is denied");
        body.setStatus(403);

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
}
