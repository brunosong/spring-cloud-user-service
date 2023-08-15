package com.study.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

    /* 클라이언트에서 데이터를 직접 받을때 사용 */

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "이메일은 2글자 이상입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "이름은 2글자 이상입니다.")
    private String name;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "암호는 8글자 이상입니다.")
    private String pwd;

}
