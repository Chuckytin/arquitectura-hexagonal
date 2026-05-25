package com.springboot.web.user.domain.port;

import com.springboot.web.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User upsert(User user);

}
