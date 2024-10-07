package com.API.Sistema.de.Inventario.service.implementation;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import com.API.Sistema.de.Inventario.service.exception.IProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IProduct {

    @Autowired
    private ProductRepository productRepository;

    public ProductEntity saveOrUpdateProduct(ProductEntity product) {
        IProductException.validateProduct(product, productRepository);
        return IProductException.handleSaveException(productRepository, product);
    }


    public boolean deleteProductByName(String name) throws IProductException {
        Optional<ProductEntity> product = productRepository.findByName(name);
        IProductException.validateProductExists(product, name);

        productRepository.delete(product.get());
        return true;
    }


    public List<ProductEntity> searchProductsByName(String name) throws IProductException {
        List<ProductEntity> products = productRepository.findByNameContainingIgnoreCase(name);
        IProductException.validateProducts(products, name);
        return products;
    }

    public List<ProductEntity> findAll() throws IProductException {
        List<ProductEntity> products = productRepository.findAll();
        IProductException.validateProductList(products);
        return products;
    }


}

