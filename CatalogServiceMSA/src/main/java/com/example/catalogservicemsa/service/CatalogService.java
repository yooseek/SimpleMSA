package com.example.catalogservicemsa.service;

import com.example.catalogservicemsa.domain.CatalogEntity;
import com.example.catalogservicemsa.domain.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogService implements ICatalogService{

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
