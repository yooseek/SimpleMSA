package com.example.userservicemsa.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String username);
}
