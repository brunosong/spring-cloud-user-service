package com.study.userservice.service;

import com.study.userservice.client.OrderServiceClient;
import com.study.userservice.dto.UserDto;
import com.study.userservice.jpa.UserEntity;
import com.study.userservice.jpa.UserRepository;
import com.study.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final RestTemplate restTemplate;

    private final Environment env;

    private final OrderServiceClient orderServiceClient;


    @Override
    public UserDto createUser(UserDto userDto) {

        userDto.setUserId(UUID.randomUUID().toString()); //랜덤으로 사용자의 ID를 생성한다.

        /* userDto 를 userEntity 로 변환해준다. */
        ModelMapper mapper = new ModelMapper();
        //강력하게 이름이 맞을때만 변환을 해준다.
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = mapper.map(userDto,UserEntity.class);

        String encryptPwd = encoder.encode(userDto.getPwd());
        userEntity.setEncryptedPwd(encryptPwd);

        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity getUserEntity = userRepository.findByUserId(userId);

        if(getUserEntity == null)
            throw new UsernameNotFoundException("유저가 없습니다");

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(getUserEntity, UserDto.class);

        /* feign client 사용 */
        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity byUserEntity = userRepository.findByEmail(username);

        if(byUserEntity == null) throw new UsernameNotFoundException(username);

        return new User(byUserEntity.getEmail(),byUserEntity.getEncryptedPwd(), true,true,true,true,
                new ArrayList<>());

    }

    @Override
    public UserDto getUserDetailsByEmail(String username) {

        UserEntity byUserEntity = userRepository.findByEmail(username);

        if(byUserEntity == null) throw new UsernameNotFoundException(username);

        UserDto userDto = new ModelMapper().map(byUserEntity, UserDto.class);

        return userDto;
    }

}
