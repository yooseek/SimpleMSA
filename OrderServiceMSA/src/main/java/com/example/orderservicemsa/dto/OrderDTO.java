package com.example.orderservicemsa.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDTO implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;
}
