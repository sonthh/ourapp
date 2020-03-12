package com.son.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ApiExceptionResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date timestamp;
    private Integer status;
    private Object errors;

    public ApiExceptionResponse(Integer status, Object errors) {
        this(new Date(), status, errors);
    }

    public ApiExceptionResponse() {
        timestamp = new Date();
    }
}
