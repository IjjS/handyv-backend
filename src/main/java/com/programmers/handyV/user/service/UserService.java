package com.programmers.handyV.user.service;

import org.springframework.stereotype.Service;

import com.programmers.handyV.user.domain.User;
import com.programmers.handyV.user.dto.request.CreateUserRequest;
import com.programmers.handyV.user.dto.response.CreateUserResponse;
import com.programmers.handyV.user.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CreateUserResponse create(CreateUserRequest request) {
        User user = User.createNormalUser(request.frontNumber(), request.backNumber());
        User savedUser = userRepository.save(user);
        return CreateUserResponse.from(savedUser);
    }
}
