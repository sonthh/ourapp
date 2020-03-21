package com.son.controller;

import com.son.constant.AuthzConstant;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.request.FirebaseTokenRequest;
import com.son.request.NotificationTypesRequest;
import com.son.security.Credentials;
import com.son.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation("get my information")
    @GetMapping("me")
    @PreAuthorize(AuthzConstant.HAS_ROLE_BASIC)
    //    @Dto(value = UserInfoDto.class)
    public ResponseEntity<User> userMe(
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {

        User userMe = userService.findOne(credentials.getId());

        return new ResponseEntity<>(userMe, HttpStatus.OK);
    }

    @ApiOperation("subscribe firebase token")
    @PutMapping("me/token/subscribe")
    @PreAuthorize(AuthzConstant.HAS_ROLE_BASIC)
    public ResponseEntity<Boolean> subscribeFirebaseToken(
        @Valid @NotNull @RequestBody(required = false) FirebaseTokenRequest firebaseTokenRequest,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) {
        userService.subscribeFirebaseToken(firebaseTokenRequest, credentials);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ApiOperation("unsubscribe firebase token")
    @PutMapping("me/token/unsubscribe")
    @PreAuthorize(AuthzConstant.HAS_ROLE_BASIC)
    public ResponseEntity<Boolean> unsubscribeFirebaseToken(
        @Valid @NotNull @RequestBody(required = false) FirebaseTokenRequest firebaseTokenRequest,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) {
        userService.unsubscribeFirebaseToken(firebaseTokenRequest, credentials);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ApiOperation("add notification types")
    @PutMapping("me/notifications/type/add")
    @PreAuthorize(AuthzConstant.HAS_ROLE_BASIC)
    public ResponseEntity<Boolean> addNotificationTypes(
        @Valid @NotNull @RequestBody(required = false) NotificationTypesRequest notificationTypesRequest,
        @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        boolean isUpdated = userService.addNotificationTypes(notificationTypesRequest, credentials);

        return new ResponseEntity<>(isUpdated, HttpStatus.OK);
    }
}
