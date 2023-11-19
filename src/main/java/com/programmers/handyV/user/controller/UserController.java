package com.programmers.handyV.user.controller;

import com.programmers.handyV.user.dto.request.CreateUserRequest;
import com.programmers.handyV.user.dto.response.CreateUserResponse;
import com.programmers.handyV.user.dto.response.UserResponse;
import com.programmers.handyV.user.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@ModelAttribute CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/car-number/{carNumber}")
    public ResponseEntity<UserResponse> findByCarNumber(@PathVariable String carNumber) {
        return ResponseEntity.ok(userService.findByCarNumber(carNumber));
    }
}
