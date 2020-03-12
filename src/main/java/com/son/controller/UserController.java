package com.son.controller;

import com.son.dto.UserInfoDto;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.handler.Dto;
import com.son.request.FirebaseTokenRequest;
import com.son.security.UserDetailsImpl;
import com.son.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Api(tags = "Users", value = "User Controller")
@RestController
@RequestMapping("users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("get my information")
    @GetMapping("me")
    @PreAuthorize("hasAnyRole('BASIC')")
    @Dto(value = UserInfoDto.class)
    public ResponseEntity<Object> userMe(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl user
    ) throws ApiException {

        User userMe = userService.findOne(user.getId());

        return new ResponseEntity<>(userMe, HttpStatus.OK);
    }

    @ApiOperation("subscribe firebase token")
    @PutMapping("me/token/subscribe")
    @PreAuthorize("hasAnyRole('BASIC')")
    public ResponseEntity<Object> subscribeFirebaseToken(
            @Valid @NotNull @RequestBody(required = false) FirebaseTokenRequest firebaseTokenRequest,
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl user
    ) {

        userService.subscribeFirebaseToken(firebaseTokenRequest, user);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ApiOperation("unsubscribe firebase token")
    @PutMapping("me/token/unsubscribe")
    @PreAuthorize("hasAnyRole('BASIC')")
    public ResponseEntity<Object> unsubscribeFirebaseToken(
            @Valid @NotNull @RequestBody(required = false) FirebaseTokenRequest firebaseTokenRequest,
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl user
    ) {
        userService.unsubscribeFirebaseToken(firebaseTokenRequest, user);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}