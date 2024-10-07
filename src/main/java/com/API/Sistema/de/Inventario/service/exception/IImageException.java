package com.API.Sistema.de.Inventario.service.exception;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public class IImageException extends RuntimeException {
    public IImageException(String message) {
        super(message);
    }

    public static void validateProductExists(Optional<?> productOpt, Long productId) throws IImageException {
        if (!productOpt.isPresent()) {
            throw new IImageException("Producto no encontrado: " + productId + " no existe");
        }
    }

    public static ProductEntity handleImageUpload(ProductEntity product, MultipartFile imageFile) throws IImageException {
        try {
            product.setImage(imageFile.getBytes());
            return product;
        } catch (IOException e) {
            throw new IImageException("Error al subir la imagen: " + e.getMessage());
        }
    }
}
