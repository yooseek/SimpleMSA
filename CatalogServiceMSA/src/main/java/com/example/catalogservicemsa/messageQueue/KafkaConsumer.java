package com.example.catalogservicemsa.messageQueue;

import com.example.catalogservicemsa.domain.CatalogEntity;
import com.example.catalogservicemsa.domain.CatalogRepository;
import com.example.catalogservicemsa.service.CatalogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KafkaConsumer {
    CatalogRepository repository;

    @Autowired
    public KafkaConsumer(CatalogRepository catalogRepository){
        this.repository = catalogRepository;
    }

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage){
        log.info("Kafka message -> "+ kafkaMessage);

        Map<Object,Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }

        CatalogEntity entity = repository.findByProductId((String) map.get("productId"));
        if(entity != null){
            entity.setStock(entity.getStock() - (Integer)map.get("qty"));
            repository.save(entity);
        }
    }
}
