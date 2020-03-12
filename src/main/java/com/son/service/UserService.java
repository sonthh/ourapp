package com.son.service;

import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.repository.UserRepository;
import com.son.request.FirebaseTokenRequest;
import com.son.security.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User findOne(Integer userID) throws ApiException {
        Optional<User> optional = userRepository.findById(userID);

        if (!optional.isPresent()) {
            throw new ApiException(400, "UserNotFound");
        }

        return optional.get();
    }

    public void subscribeFirebaseToken(FirebaseTokenRequest firebaseTokenRequest, UserDetailsImpl user) {
        String topic = "user-" + user.getId();
        fcmService.subscribeToTopic(List.of(firebaseTokenRequest.getToken()), topic);
    }

    public void unsubscribeFirebaseToken(FirebaseTokenRequest firebaseTokenRequest, UserDetailsImpl user) {
        String topic = "user-" + user.getId();
        fcmService.unsubscribeFromTopic(List.of(firebaseTokenRequest.getToken()), topic);
    }
}
