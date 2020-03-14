package com.son.service;

import com.son.entity.User;
import com.son.entity.UserStatus;
import com.son.handler.ApiException;
import com.son.repository.UserRepository;
import com.son.request.FirebaseTokenRequest;
import com.son.request.NotificationTypesRequest;
import com.son.security.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final FcmService fcmService;

    public UserService(ModelMapper modelMapper, UserRepository userRepository, FcmService fcmService) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.fcmService = fcmService;
    }

    public User findOneByUsername(String username) throws ApiException {
        Optional<User> optional = userRepository.findOneByUsername(username);

        if (!optional.isPresent()) {
            throw new ApiException(404, "UserNotFound");
        }

        return optional.get();
    }

    public User findOneActiveUser(String username) throws ApiException {
        Optional<User> optional = userRepository.findActiveUser(username, UserStatus.ACTIVE);

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

    public void subscribeFirebaseToken(FirebaseTokenRequest firebaseTokenRequest, UserDetailsImpl credentials) {
        String topic = "user-" + credentials.getId();
        fcmService.subscribeToTopic(List.of(firebaseTokenRequest.getToken()), topic);
    }

    public void unsubscribeFirebaseToken(FirebaseTokenRequest firebaseTokenRequest, UserDetailsImpl credentials) {
        String topic = "user-" + credentials.getId();
        fcmService.unsubscribeFromTopic(List.of(firebaseTokenRequest.getToken()), topic);
    }

    public boolean addNotificationTypes(
        NotificationTypesRequest notificationTypesRequest, UserDetailsImpl credentials
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
}
