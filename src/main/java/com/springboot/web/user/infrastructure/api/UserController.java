package com.springboot.web.user.infrastructure.api;

import com.springboot.web.common.application.mediator.Mediator;
import com.springboot.web.common.infrastructure.service.JwtService;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing Users")
public class UserController {

    private final Mediator mediator;
    private final UserMapper userMapper;
    private final JwtService jwtService;

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

    @GetMapping("/logout")
    @Operation(summary = "Logout user and invalidate JWT token")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request) {

        // Extraer y invalidar el token si existe
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtService.invalidateToken(token);
            log.debug("Token invalidated");
        }

        // Limpiar el SecurityContext
        SecurityContextHolder.clearContext();

        // Invalidar la sesión si existe
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        log.debug("User logged out successfully");

        return ResponseEntity.ok(Map.of(
                "message", "Logout successful",
                "status", "success"
        ));
    }

}
