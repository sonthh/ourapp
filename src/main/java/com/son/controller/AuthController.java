package com.son.controller;

import com.son.dto.LoginResponseDto;
import com.son.handler.ApiException;
import com.son.request.LoginRequest;
import com.son.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Authentication", value = "Authentication")
@RestController
@RequestMapping("auth")
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation("Login to get jwt token")
    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(
        @Valid @RequestBody LoginRequest loginRequest
    ) throws ApiException {
        LoginResponseDto result = authService.usernamePasswordLogin(loginRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}