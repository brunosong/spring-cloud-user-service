package com.study.userservice.controller;

import com.study.userservice.dto.UserDto;
import com.study.userservice.jpa.UserEntity;
import com.study.userservice.service.UserService;
import com.study.userservice.vo.Greeting;
import com.study.userservice.vo.RequestUser;
import com.study.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/user-service")
public class UserController {

    // Environment 사용과 @Value 사용 두가지가 존재
    private Environment env;

    private Greeting greeting;

    private UserService userService;

    @Autowired
    public UserController(Environment env, Greeting greeting, UserService userService) {
        this.env = env;
        this.greeting = greeting;
        this.userService = userService;
    }

    @GetMapping("/health_check")
    @Timed(value = "users.status" , longTask = true )
    public String status() {


        return String.format("It's Working in User Service"
                + ", (local.server.port) is : " + env.getProperty("local.server.port")
                + ", (server.port) is : " + env.getProperty("server.port")
                + ", Token secret is : " + env.getProperty("token.secret")
                + ", Token expiration time is : " + env.getProperty("token.expiration_time"));

    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome" , longTask = true )
    public String welcome() {
        //return env.getProperty("greeting.message");
        return greeting.getMessage();
    }

    /* 사용자로 부터 받은 자료를 디비에 저장한다. */
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {

        //DTO 변경 프로세스
        //RequestUser -> UserDto -> UserEntity -> 저장 -> UserDto -> ResponseUser
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }


    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {

        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        userList.forEach( user -> {
            result.add(mapper.map(user,ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId) {

        UserDto userDto = userService.getUserByUserId(userId);

        ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }





}
