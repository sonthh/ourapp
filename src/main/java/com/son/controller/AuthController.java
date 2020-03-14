package com.son.controller;

import com.son.handler.ApiException;
import com.son.request.LoginRequest;
import com.son.security.UserDetailsImpl;
import com.son.service.JwtTokenService;
import com.son.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "Authentication", value = "Authentication")
@RestController
@RequestMapping("auth")
@Validated
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    public AuthController(
        AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @ApiOperation("Login to get jwt token")
    @PostMapping("login")
    public ResponseEntity<Object> login(
        @Valid @NotNull @RequestBody(required = false) LoginRequest loginRequest
    ) throws ApiException {
        try {
            userService.findOneByUsername(loginRequest.getUsername());
        } catch (ApiException e) {
            throw new ApiException(401, "Unauthorized");
        }

        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(auth);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl credentials = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtTokenService.generateToken(credentials);

            Map<String, Object> result = new HashMap<>();
            result.put("token", jwt);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ApiException(401, "Unauthorized");
        }
    }
}