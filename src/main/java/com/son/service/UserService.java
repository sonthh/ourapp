package com.son.service;

import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.UserRepository;
import com.son.request.FirebaseTokenRequest;
import com.son.request.NotificationTypesRequest;
import com.son.request.UpdateAvatarRequest;
import com.son.request.UpdateUserRequest;
import com.son.security.Credentials;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;

    public UserService(
            UserRepository userRepository, FcmService fcmService, ModelMapper modelMapper,
            @Lazy BCryptPasswordEncoder passwordEncoder, CloudinaryService cloudinaryService
    ) {
        this.userRepository = userRepository;
        this.fcmService = fcmService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
    }

    public User findOneByUsername(String username) throws ApiException {
        Optional<User> optional = userRepository.findOneByUsername(username);

        if (!optional.isPresent()) {
            throw new ApiException(404, "UserNotFound");
        }

        return optional.get();
    }

    public User findOneActiveUser(String username) throws ApiException {
        Optional<User> optional = userRepository.findActiveUser(username, User.Status.ACTIVE);

        if (!optional.isPresent()) {
            throw new ApiException(404, "UserNotFoundOrIsInactive");
        }

        return optional.get();
    }

    public User findOne(Integer userID) throws ApiException {
        Optional<User> optional = userRepository.findById(userID);

        if (!optional.isPresent()) {
            throw new ApiException(404, "UserNotFound");
        }

        return optional.get();
    }

    public void subscribeFirebaseToken(FirebaseTokenRequest firebaseTokenRequest, Credentials credentials) {
        String topic = "user-" + credentials.getId();
        fcmService.subscribeToTopic(Collections.singletonList(firebaseTokenRequest.getToken()), topic);
    }

    public void unsubscribeFirebaseToken(FirebaseTokenRequest firebaseTokenRequest, Credentials credentials) {
        String topic = "user-" + credentials.getId();
        fcmService.unsubscribeFromTopic(Collections.singletonList(firebaseTokenRequest.getToken()), topic);
    }

    public boolean addNotificationTypes(
            NotificationTypesRequest notificationTypesRequest, Credentials credentials
    ) throws ApiException {
        Optional<User> optional = userRepository.findById(credentials.getId());

        if (!optional.isPresent()) {
            throw new ApiException(404, "UserNotFound");
        }
        User user = optional.get();

        List<String> notificationTypes = notificationTypesRequest.getNotificationTypes();
        user.getNotificationTypes().addAll(notificationTypes);

        userRepository.save(user);
        return true;
    }

    public User updateOne(UpdateUserRequest updateUserRequest, Credentials credentials) throws ApiException {
        User updatedUser = findOne(credentials.getId());

        if (updateUserRequest.getPassword() != null) {
            updateUserRequest.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        modelMapper.map(updateUserRequest, updatedUser);

        updatedUser = userRepository.save(updatedUser);

        return updatedUser;
    }

    public String updateAvatar(UpdateAvatarRequest updateAvatarRequest, Credentials credentials)
            throws ApiException {
        User updatedUser = findOne(credentials.getId());

        String avatar;
        try {
            avatar = cloudinaryService.upload(updateAvatarRequest.getAvatar());
        } catch (IOException e) {
            throw new ApiException(400, "Upload file failed");
        }

        if (avatar == null) {
            throw new ApiException(400, "Upload file failed");
        }

        updatedUser.setAvatar(avatar);
        userRepository.save(updatedUser);

        return avatar;
    }
}
