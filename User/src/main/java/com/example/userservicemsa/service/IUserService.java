package com.example.userservicemsa.service;

import com.example.userservicemsa.domain.UserEntity;
import com.example.userservicemsa.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByUserId(String userId);

    Iterable<UserEntity> getUserByAll();

    UserDTO getUserDetailsByEmail(String userName);
}
