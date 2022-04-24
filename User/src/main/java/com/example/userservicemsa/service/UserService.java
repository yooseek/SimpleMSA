package com.example.userservicemsa.service;

import com.example.userservicemsa.domain.UserEntity;
import com.example.userservicemsa.domain.UserRepository;
import com.example.userservicemsa.dto.ResponseOrder;
import com.example.userservicemsa.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserService implements IUserService{

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;    // password Encoding
    Environment environment;

    public UserService (UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,Environment environment){
        this.userRepository =userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
    }


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setUserId((UUID.randomUUID().toString()));

        ModelMapper mapper = new ModelMapper();
        // 정확히 일치하는 값을 변환시켜주는 정책 설정
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDTO,UserEntity.class);
        // password Encoding
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDTO.getPwd()));

        userRepository.save(userEntity);

        UserDTO returnUser = mapper.map(userEntity,UserDTO.class);

        return returnUser;
    }

    @Override
    public UserDTO getUserByUserId(String userId) {
        UserEntity user = userRepository.findByUserId(userId);

        if(user == null) throw new UsernameNotFoundException("User not found");

        UserDTO userDTO = new ModelMapper().map(user,UserDTO.class);

        RestTemplate rt = new RestTemplate();
        String orderURL = String.format(environment.getProperty("order-service.url"),userId);
        log.info(orderURL);

        List<ResponseOrder> orderList =
                rt.exchange(orderURL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<ResponseOrder>>() {
                }).getBody();

        userDTO.setOrders(orderList);

        return userDTO;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity user =  userRepository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("user not found");
        }

        UserDTO userDTO = new ModelMapper().map(user,UserDTO.class);

        return userDTO;
    }

    // UserDetailService Method
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        // username, password ... Authentication
        return new User(user.getEmail(),user.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }
}
