package com.springboot.web.user.infrastructure.api;

import com.springboot.web.common.application.mediator.Mediator;
import com.springboot.web.user.application.command.login.LoginUserRequest;
import com.springboot.web.user.application.command.login.LoginUserResponse;
import com.springboot.web.user.application.command.register.RegisterUserRequest;
import com.springboot.web.user.application.command.register.RegisterUserResponse;
import com.springboot.web.user.infrastructure.api.dto.LoginRequestDto;
import com.springboot.web.user.infrastructure.api.dto.RegisterRequestDto;
import com.springboot.web.user.infrastructure.api.dto.TokenResponseDto;
import com.springboot.web.user.infrastructure.api.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing Users")
public class UserController {

    private final Mediator mediator;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT token")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        LoginUserRequest request = userMapper.mapToLoginUserRequestDto(loginRequestDto);

        LoginUserResponse response = mediator.dispatch(request);

        TokenResponseDto tokenResponseDto = userMapper.mapToTokenResponseDto(response);

        return ResponseEntity.ok(tokenResponseDto);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user and return JWT token")
    public ResponseEntity<TokenResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {

        RegisterUserRequest request = userMapper.mapToRegisterUserRequestDto(registerRequestDto);

        RegisterUserResponse response = mediator.dispatch(request);

        TokenResponseDto tokenResponseDto = userMapper.mapToTokenResponseDto(response);

        log.info("User registered successfully: {}", registerRequestDto.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponseDto);
    }

}
