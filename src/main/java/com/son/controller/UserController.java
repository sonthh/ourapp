package com.son.controller;

import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_READ, @scopes.PER_USER_READ)")
    public ResponseEntity<User> userMe(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        User userMe = userService.findOne(credentials.getId());

        return new ResponseEntity<>(userMe, HttpStatus.OK);
    }

    @ApiOperation("update my information")
    @PutMapping("me")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_UPDATE, @scopes.PER_USER_UPDATE)")
    public ResponseEntity<User> updateMe(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Valid @RequestBody UpdateUserMeRequest updateUserRequest
    ) throws ApiException {

        User userMe = userService.updateOneMe(updateUserRequest, credentials);

        return new ResponseEntity<>(userMe, HttpStatus.OK);
    }

    @ApiOperation("update my avatar")
    @PostMapping("me/avatar")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_UPDATE, @scopes.PER_USER_UPDATE)")
    public ResponseEntity<String> updateAvatar(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Valid UpdateAvatarRequest updateAvatarRequest
    ) throws ApiException {
        String avatar = userService.updateAvatar(updateAvatarRequest, credentials);

        return new ResponseEntity<>(avatar, HttpStatus.OK);
    }

    @ApiOperation("get one user")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_READ)")
    public ResponseEntity<User> findOneUser(
            @Min(1) @PathVariable Integer userId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        User user = userService.findOne(userId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation("find many users")
    @GetMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_READ)")
    public ResponseEntity<Page<User>> findMany(
            @Valid FindAllUserRequest findAllUserRequest,
            @ApiIgnore BindingResult errors,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Page<User> page = userService.findMany(credentials, findAllUserRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @ApiOperation("create one user")
    @PostMapping
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_CREATE)")
    public ResponseEntity<User> createOne(
            @ApiIgnore @AuthenticationPrincipal Credentials credentials,
            @Valid @RequestBody CreateUserRequest createUserRequest
    ) throws ApiException {
        User newUser = userService.createOne(credentials, createUserRequest);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @ApiOperation("update one user")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_UPDATE)")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateOne(
            @Valid @RequestBody UpdateUserRequest updateUserRequest,
            @Min(1) @PathVariable Integer userId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        User updatedUser = userService.updateOneUser(credentials, updateUserRequest, userId);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @ApiOperation("delete one user")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_DELETE)")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Boolean> deleteOne(
            @Min(1) @PathVariable Integer userId,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Boolean isDeleted = userService.deleteOneUser(credentials, userId);

        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @ApiOperation("delete many users")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteManyUser(
            @Valid @RequestBody DeleteManyByIdRequest deleteManyByIdRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        Boolean isDeleted = userService.deleteMany(credentials, deleteManyByIdRequest);

        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @ApiOperation("subscribe firebase token")
    @PutMapping("me/token/subscribe")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_UPDATE, @scopes.PER_USER_UPDATE)")
    public ResponseEntity<Boolean> subscribeFirebaseToken(
            @Valid @NotNull @RequestBody(required = false) FirebaseTokenRequest firebaseTokenRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) {
        userService.subscribeFirebaseToken(firebaseTokenRequest, credentials);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ApiOperation("unsubscribe firebase token")
    @PutMapping("me/token/unsubscribe")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_UPDATE, @scopes.PER_USER_UPDATE)")
    public ResponseEntity<Boolean> unsubscribeFirebaseToken(
            @Valid @NotNull @RequestBody(required = false) FirebaseTokenRequest firebaseTokenRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) {
        userService.unsubscribeFirebaseToken(firebaseTokenRequest, credentials);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ApiOperation("add notification types")
    @PutMapping("me/notifications/type/add")
    @PreAuthorize("hasAnyAuthority(@scopes.ALL_USER_UPDATE, @scopes.PER_USER_UPDATE)")
    public ResponseEntity<Boolean> addNotificationTypes(
            @Valid @NotNull @RequestBody(required = false) NotificationTypesRequest notificationTypesRequest,
            @ApiIgnore @AuthenticationPrincipal Credentials credentials
    ) throws ApiException {
        boolean isUpdated = userService.addNotificationTypes(notificationTypesRequest, credentials);

        return new ResponseEntity<>(isUpdated, HttpStatus.OK);
    }
}
