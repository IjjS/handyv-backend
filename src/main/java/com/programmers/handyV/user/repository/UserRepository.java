package com.programmers.handyV.user.repository;

import com.programmers.handyV.user.domain.CarNumber;
import com.programmers.handyV.user.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    List<User> findAll();

    Optional<User> findByCarNumber(CarNumber carNumber);
}
