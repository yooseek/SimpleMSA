package com.example.orderservicemsa.service;

import com.example.orderservicemsa.domain.OrderEntity;
import com.example.orderservicemsa.domain.OrderRepository;
import com.example.orderservicemsa.dto.OrderDTO;
import com.example.orderservicemsa.dto.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService implements IOrderService{

    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Iterable<OrderEntity> getAllOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        orderDTO.setOrderId(UUID.randomUUID().toString());
        orderDTO.setTotalPrice(orderDTO.getQty() * orderDTO.getUnitPrice());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity entity = mapper.map(orderDTO,OrderEntity.class);

        orderRepository.save(entity);

        OrderDTO result = mapper.map(entity,OrderDTO.class);

        return result;
    }

    @Override
    public OrderDTO getOrderByOrderId(String orderId) {

        OrderEntity order = orderRepository.findByOrderId(orderId);
        OrderDTO result = new ModelMapper().map(order,OrderDTO.class);

        return result;
    }
}
