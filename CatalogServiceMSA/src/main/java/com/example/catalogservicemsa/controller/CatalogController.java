package com.example.catalogservicemsa.controller;

import com.example.catalogservicemsa.domain.CatalogEntity;
import com.example.catalogservicemsa.dto.ResponseCatalog;
import com.example.catalogservicemsa.service.CatalogService;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {

    Environment environment;
    CatalogService catalogService;

    public CatalogController(Environment environment, CatalogService catalogService) {
        this.environment = environment;
        this.catalogService = catalogService;
    }

    @GetMapping("/health_check")
    public String status(){
        // Return the port number
        return String.format("It's Working in Catalog Service Port Number : %s",environment.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs(){
        Iterable<CatalogEntity> userList =  catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
