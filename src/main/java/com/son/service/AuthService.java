package com.son.service;

import com.son.dto.LoginResponseDto;
import com.son.handler.ApiException;
import com.son.request.LoginRequest;
import com.son.security.Credentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthService(
        UserService userService, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public LoginResponseDto usernamePasswordLogin(LoginRequest loginRequest) throws ApiException {
        try {
            userService.findOneByUsername(loginRequest.getUsername());
        } catch (ApiException e) {
            throw new ApiException(401, "Unauthorized");
        }

        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(auth);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Credentials credentials = (Credentials) authentication.getPrincipal();

            String jwtToken = jwtTokenService.generateToken(credentials);

            return new LoginResponseDto(jwtToken);
        } catch (Exception ex) {
            throw new ApiException(401, "Unauthorized");
        }
    }
}
