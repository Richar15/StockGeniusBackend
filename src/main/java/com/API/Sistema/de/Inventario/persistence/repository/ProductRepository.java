package com.API.Sistema.de.Inventario.persistence.repository;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {


    ProductEntity save(ProductEntity product);

    Optional<ProductEntity> findByDescription(String description);

    Optional<ProductEntity> findByName(String name);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);

    List<ProductEntity> findAll();

}
