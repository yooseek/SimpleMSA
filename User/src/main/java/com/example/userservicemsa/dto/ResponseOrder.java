package com.example.userservicemsa.dto;

import lombok.Data;

@Data
public class ResponseOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Data createdAt;

    private String orderId;
}
