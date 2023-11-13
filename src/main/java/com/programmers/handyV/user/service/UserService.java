package com.programmers.handyV.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmers.handyV.user.domain.User;
import com.programmers.handyV.user.dto.request.CreateUserRequest;
import com.programmers.handyV.user.dto.response.CreateUserResponse;
import com.programmers.handyV.user.dto.response.UserResponse;
import com.programmers.handyV.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
        User user = User.createNormalUser(request.frontNumber(), request.backNumber());
        User savedUser = userRepository.save(user);
        return CreateUserResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return UserResponse.listOf(users);
    }
}
