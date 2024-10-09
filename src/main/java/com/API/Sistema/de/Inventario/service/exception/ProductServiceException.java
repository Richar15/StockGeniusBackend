package com.API.Sistema.de.Inventario.service.exception;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

public class ProductServiceException extends RuntimeException {

    public ProductServiceException(String message) {
        super(message);
    }

    public static void validateProduct(ProductEntity product, ProductRepository productRepository) {
        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException("La descripción del producto es obligatoria");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor que cero");
        }
        if (product.getAmount() < 0) {
            throw new IllegalArgumentException("La cantidad del producto no puede ser negativa");
        }

        Optional<ProductEntity> existingProductWithSameDescription = productRepository.findByDescription(product.getDescription());
        if (existingProductWithSameDescription.isPresent() && !existingProductWithSameDescription.get().getId().equals(product.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con esa descripción");
        }

        Optional<ProductEntity> existingProductWithSameName = productRepository.findByName(product.getName());
        if (existingProductWithSameName.isPresent() && !existingProductWithSameName.get().getId().equals(product.getId())) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }
    }

    public static ProductEntity handleSaveException(ProductRepository productRepository, ProductEntity product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al guardar el producto: " + e.getMessage());
        }
    }

    public static void validateProductExists(Optional<ProductEntity> product, String name) throws ProductServiceException {
        if (!product.isPresent()) {
            throw new ProductServiceException("Producto no encontrado: " + name);
        }
    }

    public static void validateProducts(List<ProductEntity> products, String name) throws ProductServiceException {
        if (products.isEmpty()) {
            throw new ProductServiceException("No se encontraron productos con el nombre: " + name);
        }
    }

    public static void validateProductList(List<ProductEntity> products) throws ProductServiceException {
        if (products == null || products.isEmpty()) {
            throw new ProductServiceException("No fue posible traer los productos.");
        }
    }

}
