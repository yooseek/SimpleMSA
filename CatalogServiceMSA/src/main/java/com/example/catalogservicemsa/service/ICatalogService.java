package com.example.catalogservicemsa.service;

import com.example.catalogservicemsa.domain.CatalogEntity;

public interface ICatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
