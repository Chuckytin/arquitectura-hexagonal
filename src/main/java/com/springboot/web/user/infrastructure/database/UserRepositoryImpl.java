package com.springboot.web.user.infrastructure.database;

import com.springboot.web.user.domain.entity.User;
import com.springboot.web.user.domain.port.UserRepository;
import com.springboot.web.user.infrastructure.database.entity.UserEntity;
import com.springboot.web.user.infrastructure.database.mapper.UserEntityMapper;
import com.springboot.web.user.infrastructure.database.repository.QueryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final QueryUserRepository queryUserRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return queryUserRepository.findByEmail(email).map(userEntityMapper::mapToUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return queryUserRepository.findByEmail(email).isPresent();
    }

    @Override
    public User upsert(User user) {
        UserEntity userEntity = userEntityMapper.mapToUserEntity(user);
        UserEntity savedUserEntity = queryUserRepository.save(userEntity);
        return userEntityMapper.mapToUser(savedUserEntity);
    }
}
