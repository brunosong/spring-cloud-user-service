package com.study.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {

    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @Size(min = 2, message = "이메일은 2글자 이상입니다.")
    @Email
    private String email;

    @NotNull(message = "패스워드는 필수 입력 값입니다.")
    @Size(min = 8, message = "패스워드는 8글자 이상입니다.")
    private String password;

}
