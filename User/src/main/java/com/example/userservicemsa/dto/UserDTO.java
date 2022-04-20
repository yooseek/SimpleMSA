package com.example.userservicemsa.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;
}
