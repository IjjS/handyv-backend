package com.programmers.handyV.user.repository;

import java.util.List;
import java.util.Optional;

import com.programmers.handyV.user.domain.CarNumber;
import com.programmers.handyV.user.domain.User;

public interface UserRepository {
    User save(User user);

    List<User> findAll();

    Optional<User> findByCarNumber(CarNumber carNumber);
}
