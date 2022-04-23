package com.example.orderservicemsa.service;

import com.example.orderservicemsa.domain.OrderEntity;
import com.example.orderservicemsa.dto.OrderDTO;

public interface IOrderService {
    Iterable<OrderEntity> getAllOrdersByUserId(String userId);
    OrderDTO createOrder (OrderDTO orderDTO);
    OrderDTO getOrderByOrderId (String orderId);
}
