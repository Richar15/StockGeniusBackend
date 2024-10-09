package com.API.Sistema.de.Inventario.service.exception;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public class ImageServiceException extends RuntimeException {
    public ImageServiceException(String message) {
        super(message);
    }

    public static void validateProductExists(Optional<?> productOpt, Long productId) throws ImageServiceException {
        if (!productOpt.isPresent()) {
            throw new ImageServiceException("Producto no encontrado: " + productId + " no existe");
        }
    }

    public static ProductEntity handleImageUpload(ProductEntity product, MultipartFile imageFile) throws ImageServiceException {
        try {
            product.setImage(imageFile.getBytes());
            return product;
        } catch (IOException e) {
            throw new ImageServiceException("Error al subir la imagen: " + e.getMessage());
        }
    }
}
