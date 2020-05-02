package com.son.service;

import com.son.constant.Exceptions;
import com.son.entity.Role;
import com.son.entity.User;
import com.son.handler.ApiException;
import com.son.model.Gender;
import com.son.repository.RoleRepository;
import com.son.repository.UserRepository;
import com.son.request.*;
import com.son.security.Credentials;
import com.son.util.common.EnumUtil;
import com.son.util.page.PageUtil;
import com.son.util.spec.SpecificationBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.son.util.spec.SearchOperation.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FcmService fcmService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final RoleRepository roleRepository;

    public UserService(
            UserRepository userRepository, FcmService fcmService, ModelMapper modelMapper,
            @Lazy BCryptPasswordEncoder passwordEncoder, CloudinaryService cloudinaryService,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.fcmService = fcmService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.cloudinaryService = cloudinaryService;
        this.roleRepository = roleRepository;
    }

    private void validatePermission(Credentials credentials, User user) throws ApiException {
        if (user == null) {
            return;
        }

        // is admin
        if (credentials.isAdmin() || credentials.isManager()) {
            return;
        }

        // is owner
        User createdBy = user.getCreatedBy();
        if (createdBy != null && credentials.sameEntity(user)) {
            return;
        }

        // is me
        if (credentials.sameEntity(user)) {
            return;
        }

        throw new ApiException(403, Exceptions.NO_PERMISSION);
    }

    public User findOneByUsername(String username) throws ApiException {
        Optional<User> optional = userRepository.findOneByUsername(username);

        if (!optional.isPresent()) {
            throw new ApiException(404, Exceptions.USER_NOT_FOUND);
        }

        return optional.get();
    }

    public User findOneActiveUser(String username) throws ApiException {
        Optional<User> optional = userRepository.findActiveUser(username, User.Status.ACTIVE);

        if (!optional.isPresent()) {
            throw new ApiException(404, Exceptions.USER_NOT_FOUND_OR_IS_INACTIVE);
        }

        return optional.get();
    }

    public User findOne(Integer userID) throws ApiException {
        Optional<User> optional = userRepository.findById(userID);

        if (!optional.isPresent()) {
            throw new ApiException(404, Exceptions.USER_NOT_FOUND);
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
        User user = findOne(credentials.getId());

        List<String> notificationTypes = notificationTypesRequest.getNotificationTypes();
        user.getNotificationTypes().addAll(notificationTypes);

        userRepository.save(user);
        return true;
    }

    public User updateOneMe(UpdateUserMeRequest updateUserRequest, Credentials credentials) throws ApiException {
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
            throw new ApiException(400, Exceptions.UPLOAD_FAILURE);
        }

        if (avatar == null) {
            throw new ApiException(400, Exceptions.UPLOAD_FAILURE);
        }

        updatedUser.setAvatar(avatar);
        userRepository.save(updatedUser);

        return avatar;
    }

    public User createOne(Credentials credentials, CreateUserRequest createUserRequest) throws ApiException {
        Optional<User> optional = userRepository.findOneByUsername(createUserRequest.getUsername());
        if (optional.isPresent()) {
            throw new ApiException(400, Exceptions.USERNAME_EXIST);
        }

        List<Integer> roleIds = createUserRequest.getRoleIds();
        List<Role> roles = (List<Role>) roleRepository.findAllById(roleIds);

        if (roles.size() != roleIds.size()) {
            throw new ApiException(404, Exceptions.ROLE_NOT_FOUND);
        }

        User savedUser = modelMapper.map(createUserRequest, User.class);
        savedUser.setRoles(roles);
        savedUser.setPassword(passwordEncoder.encode(savedUser.getPassword()));

        validatePermission(credentials, savedUser);

        return userRepository.save(savedUser);
    }

    public User updateOneUser(Credentials credentials, UpdateUserRequest updateUserRequest, Integer userId)
            throws ApiException {
        User updatedUser = findOne(userId);

        if (updateUserRequest.getPassword() != null) {
            updateUserRequest.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }

        modelMapper.map(updateUserRequest, updatedUser);

        List<Integer> roleIds = updateUserRequest.getRoleIds();
        if (roleIds != null) {
            List<Role> roles = (List<Role>) roleRepository.findAllById(roleIds);

            if (roles.size() != roleIds.size()) {
                throw new ApiException(404, Exceptions.ROLE_NOT_FOUND);
            }
            updatedUser.setRoles(roles);
        }

        validatePermission(credentials, updatedUser);

        updatedUser = userRepository.save(updatedUser);

        return updatedUser;
    }

    public Boolean deleteOneUser(Credentials credentials, Integer userId) throws ApiException {
        User user = findOne(userId);

        validatePermission(credentials, user);

        userRepository.delete(user);

        return true;
    }

    public Boolean deleteMany(Credentials credentials, DeleteManyByIdRequest deleteManyByIdRequest)
            throws ApiException {
        List<Integer> ids = deleteManyByIdRequest.getIds();

        List<User> users = (List<User>) userRepository.findAllById(ids);

        if (users.size() != ids.size()) {
            throw new ApiException(404, Exceptions.USER_NOT_FOUND);
        }

        userRepository.deleteAll(users);

        return true;
    }

    public Page<User> findMany(Credentials credentials, FindAllUserRequest findAllUserRequest)
            throws ApiException {
        User.Status status = EnumUtil.getEnum(User.Status.class, findAllUserRequest.getStatus());
        Gender gender = EnumUtil.getEnum(Gender.class, findAllUserRequest.getGender());

        String createdBy = findAllUserRequest.getCreatedBy();
        String lastModifiedBy = findAllUserRequest.getLastModifiedBy();
        String username = findAllUserRequest.getUsername();
        String address = findAllUserRequest.getAddress();
        String fullName = findAllUserRequest.getFullName();
        String identification = findAllUserRequest.getIdentification();
        String phoneNumber = findAllUserRequest.getPhoneNumber();
        String email = findAllUserRequest.getEmail();
        List<Integer> userIds = findAllUserRequest.getIds();

        Integer currentPage = findAllUserRequest.getCurrentPage();
        Integer limit = findAllUserRequest.getLimit();
        String sortDirection = findAllUserRequest.getSortDirection();
        String sortBy = findAllUserRequest.getSortBy();


        SpecificationBuilder<User> builder = new SpecificationBuilder<>();
        builder
                .query("address", CONTAINS, address)
                .query("username", CONTAINS, username)
                .query("email", CONTAINS, email)
                .query("fullName", CONTAINS, fullName)
                .query("phoneNumber", CONTAINS, phoneNumber)
                .query("identification", CONTAINS, identification)
                .query("status", EQUALITY, status)
                .query("gender", EQUALITY, gender)
                .query("id", IN, userIds)
                .query("createdBy.username", CONTAINS, createdBy)
                .query("lastModifiedBy.username", CONTAINS, lastModifiedBy);

        Specification<User> spec = builder.build();

        Pageable pageable = PageUtil.getPageable(currentPage, limit, sortDirection, sortBy);

        return userRepository.findAll(spec, pageable);
    }
}
