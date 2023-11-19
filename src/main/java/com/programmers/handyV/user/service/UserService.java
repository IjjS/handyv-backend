package com.programmers.handyV.user.service;

import com.programmers.handyV.common.exception.BadRequestException;
import com.programmers.handyV.user.domain.CarNumber;
import com.programmers.handyV.user.domain.User;
import com.programmers.handyV.user.dto.request.CreateUserRequest;
import com.programmers.handyV.user.dto.response.CreateUserResponse;
import com.programmers.handyV.user.dto.response.UserResponse;
import com.programmers.handyV.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateUserResponse create(CreateUserRequest request) {
        User user = request.isAdmin()
                ? User.createAdminUser(request.frontNumber(), request.backNumber())
                : User.createNormalUser(request.frontNumber(), request.backNumber());
        validateDuplicateCarNumber(user.getCarFullNumber());
        User savedUser = userRepository.save(user);
        return CreateUserResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return UserResponse.listOf(users);
    }

    @Transactional(readOnly = true)
    public UserResponse findByCarNumber(String fullNumber) {
        CarNumber carNumber = new CarNumber(fullNumber);
        User user = userRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new BadRequestException(carNumber.getFullNumber() + "의 번호로 등록된 차량이 없습니다."));
        return UserResponse.from(user);
    }

    private void validateDuplicateCarNumber(String fullNumber) {
        CarNumber carNumber = new CarNumber(fullNumber);
        if (userRepository.existsByCarNumber(carNumber)) {
            throw new BadRequestException(fullNumber + "의 차 번호로 등록된 사용자가 있습니다");
        }
    }
}
