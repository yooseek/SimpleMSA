package com.example.userservicemsa.service;

import com.example.userservicemsa.domain.UserEntity;
import com.example.userservicemsa.domain.UserRepository;
import com.example.userservicemsa.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService implements IUserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setUserId((UUID.randomUUID().toString()));

        ModelMapper mapper = new ModelMapper();
        // 정확히 일치하는 값을 변환시켜주는 정책 설정
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDTO,UserEntity.class);

        userEntity.setEncryptedPwd("encrypted_password");

        userRepository.save(userEntity);

        UserDTO returnUser = mapper.map(userEntity,UserDTO.class);

        return returnUser;
    }
}
