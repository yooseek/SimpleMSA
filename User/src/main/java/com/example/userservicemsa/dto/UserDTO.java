package com.example.userservicemsa.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    private String name;
    private String email;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;

    private List<ResponseOrder> orders;
}
