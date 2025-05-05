package com.dev.user_manage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
}
