package com.example.userservicemsa.controller;

import com.example.userservicemsa.domain.UserEntity;
import com.example.userservicemsa.dto.RequestUser;
import com.example.userservicemsa.dto.ResponseUser;
import com.example.userservicemsa.dto.UserDTO;
import com.example.userservicemsa.service.UserService;
import com.example.userservicemsa.vo.Greeting;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/")
@RestController
public class UserController {

    private Greeting greeting;
    private Environment environment;
    private UserService userService;

    public UserController(Environment environment, Greeting greeting,UserService userService){
        this.environment = environment;
        this.greeting = greeting;
        this.userService =userService;
    }
    @GetMapping("/welcome")
    public String welcom(){
//        return environment.getProperty("greeting.message");
        return greeting.getMessage();
    }

    @GetMapping("/health_check")
    public String status(){
        // Return the port number
        return String.format("It's Working in User Service"
                + " Port Number" + environment.getProperty("local.server.port")
                + " Server Port Number" + environment.getProperty("server.port")
                + " Token secret" + environment.getProperty("token.secret")
                + " Token expiration_time" + environment.getProperty("token.expiration_time")
        );
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = mapper.map(user,UserDTO.class);

        userService.createUser(userDTO);

        ResponseUser response = mapper.map(userDTO, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList =  userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userid}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userid){

        UserDTO user = userService.getUserByUserId(userid);

        ResponseUser resultUser = new ModelMapper().map(user,ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(resultUser);
    }
}
