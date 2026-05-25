package com.springboot.web.user.application.command.register;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest implements Request<RegisterUserResponse> {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
