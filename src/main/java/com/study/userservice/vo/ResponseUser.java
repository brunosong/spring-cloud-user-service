package com.study.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {

    /* 클라이언트에서 어떠한 데이터가 저장되었는지 확인하라는 용도로 생성 */
    private String email;
    private String name;
    private String userId;

    private List<ResponseOrder> orders;

}
