package com.springboot.web.user.application.command.login;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest implements Request<LoginUserResponse> {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
