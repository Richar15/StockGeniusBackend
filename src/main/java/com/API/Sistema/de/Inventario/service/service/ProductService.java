package com.API.Sistema.de.Inventario.service.service;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import com.API.Sistema.de.Inventario.service.exception.ProductServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductEntity saveOrUpdateProduct(ProductEntity product) {
        ProductServiceException.validateProduct(product, productRepository);
        return ProductServiceException.handleSaveException(productRepository, product);
    }


    public boolean deleteProductByName(String name) throws ProductServiceException {
        Optional<ProductEntity> product = productRepository.findByName(name);
        ProductServiceException.validateProductExists(product, name);

        productRepository.delete(product.get());
        return true;
    }


    public List<ProductEntity> searchProductsByName(String name) throws ProductServiceException {
        List<ProductEntity> products = productRepository.findByNameContainingIgnoreCase(name);
        ProductServiceException.validateProducts(products, name);
        return products;
    }

    public List<ProductEntity> findAll() throws ProductServiceException {
        List<ProductEntity> products = productRepository.findAll();
        ProductServiceException.validateProductList(products);
        return products;
    }


}

