package com.programmers.handyV.user.repository;

import com.programmers.handyV.user.domain.User;

public interface UserRepository {
    User save(User user);
}
