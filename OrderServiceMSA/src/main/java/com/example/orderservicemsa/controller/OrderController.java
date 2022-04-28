package com.example.orderservicemsa.controller;

import com.example.orderservicemsa.domain.OrderEntity;
import com.example.orderservicemsa.dto.OrderDTO;
import com.example.orderservicemsa.dto.RequestOrder;
import com.example.orderservicemsa.dto.ResponseOrder;
import com.example.orderservicemsa.messagequeue.KafkaOrderProducer;
import com.example.orderservicemsa.messagequeue.KafkaProducer;
import com.example.orderservicemsa.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    Environment environment;
    OrderService orderService;
    KafkaProducer kafkaProducer;
    KafkaOrderProducer kafkaOrderProducer;

    @Autowired
    public OrderController(Environment environment, OrderService orderService,
                           KafkaProducer kafkaProducer,KafkaOrderProducer kafkaOrderProducer) {
        this.environment = environment;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.kafkaOrderProducer = kafkaOrderProducer;
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

        /* jpa */
//        OrderDTO createOrder = orderService.createOrder(orderDTO);
//        ResponseOrder response = mapper.map(createOrder, ResponseOrder.class);

        /* kafka */
        orderDTO.setOrderId(UUID.randomUUID().toString());
        orderDTO.setTotalPrice(order.getQty() * order.getUnitPrice());


        // send this order to the kafka
        kafkaProducer.send("example-catalog-topic",orderDTO);
        kafkaOrderProducer.send("orders",orderDTO);

        ResponseOrder responseOrder = mapper.map(orderDTO,ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getUsers(@PathVariable String userId){
        Iterable<OrderEntity> orderList =  orderService.getAllOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
