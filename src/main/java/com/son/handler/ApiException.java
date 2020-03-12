package com.son.handler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends Throwable {
    private Integer status;
    private String message;

    public ApiException(Integer status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
