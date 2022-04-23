package com.example.catalogservicemsa.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<CatalogEntity,Long> {
    CatalogEntity findByProductId(String productId);
}
