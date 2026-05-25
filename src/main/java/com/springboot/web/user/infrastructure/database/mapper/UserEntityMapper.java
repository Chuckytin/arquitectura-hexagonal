package com.springboot.web.user.infrastructure.database.mapper;

import com.springboot.web.user.domain.entity.User;
import com.springboot.web.user.infrastructure.database.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface UserEntityMapper {

    UserEntity mapToUserEntity(User user);

    User mapToUser(UserEntity userEntity);

}
