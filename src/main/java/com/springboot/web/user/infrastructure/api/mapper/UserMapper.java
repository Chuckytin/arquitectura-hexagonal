package com.springboot.web.user.infrastructure.api.mapper;

import com.springboot.web.user.application.command.login.LoginUserRequest;
import com.springboot.web.user.application.command.login.LoginUserResponse;
import com.springboot.web.user.application.command.register.RegisterUserRequest;
import com.springboot.web.user.application.command.register.RegisterUserResponse;
import com.springboot.web.user.infrastructure.api.dto.LoginRequestDto;
import com.springboot.web.user.infrastructure.api.dto.RegisterRequestDto;
import com.springboot.web.user.infrastructure.api.dto.TokenResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

    LoginUserRequest mapToLoginUserRequestDto(LoginRequestDto loginRequestDto);

    RegisterUserRequest mapToRegisterUserRequestDto(RegisterRequestDto registerRequestDto);

    TokenResponseDto mapToTokenResponseDto(LoginUserResponse loginUserResponse);

    TokenResponseDto mapToTokenResponseDto(RegisterUserResponse registerUserResponse);

}
