package com.springboot.web.user.domain.port;

public interface AuthenticationPort {

    String authenticate(String email, String password);

}
