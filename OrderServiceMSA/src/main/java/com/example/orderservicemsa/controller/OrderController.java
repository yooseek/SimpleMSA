package com.example.orderservicemsa.controller;

import com.example.orderservicemsa.domain.OrderEntity;
import com.example.orderservicemsa.dto.OrderDTO;
import com.example.orderservicemsa.dto.RequestOrder;
import com.example.orderservicemsa.dto.ResponseOrder;
import com.example.orderservicemsa.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    Environment environment;
    OrderService orderService;

    public OrderController(Environment environment, OrderService orderService) {
        this.environment = environment;
        this.orderService = orderService;
    }

    @GetMapping("/health_check")
    public String status(){
        // Return the port number
        return String.format("It's Working in Order Service Port Number : %s",environment.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder order, @PathVariable String userId){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDTO orderDTO = mapper.map(order,OrderDTO.class);
        orderDTO.setUserId(userId);

        OrderDTO createOrder = orderService.createOrder(orderDTO);

        ResponseOrder response = mapper.map(createOrder, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getUsers(@PathVariable String userId){
        Iterable<OrderEntity> userList =  orderService.getAllOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
