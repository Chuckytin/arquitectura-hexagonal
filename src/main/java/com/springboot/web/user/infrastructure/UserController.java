package com.springboot.web.user.infrastructure;

import com.springboot.web.common.infrastructure.service.JwtService;
import com.springboot.web.user.infrastructure.dto.LoginRequestDto;
import com.springboot.web.user.infrastructure.dto.LoginResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(loginRequestDto.getEmail())
                .password(loginRequestDto.getPassword())
                .roles("USER")
                .build();

        String token = jwtService.generateToken(user);

        log.debug("Token generated successfully");

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

}
