package com.study.userservice.vo;



import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
//@NoArgsConstructor  // 디폴트 생성자를 만들어 주겠다.
public class Greeting {

    @Value("${greeting.message}")
    private String message;

}
