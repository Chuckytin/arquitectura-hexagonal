package com.springboot.web.user.domain.port;

public interface PasswordEncoderPort {

    String encode(String password);

}
