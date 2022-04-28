package com.example.orderservicemsa.dto.kafka;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class KafkaOrderDTO implements Serializable {
    private Schema schema;
    private Payload payload;
}
