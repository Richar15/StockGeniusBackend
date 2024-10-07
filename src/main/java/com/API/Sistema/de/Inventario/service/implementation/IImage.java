package com.API.Sistema.de.Inventario.service.implementation;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import com.API.Sistema.de.Inventario.service.exception.IImageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class IImage {


    @Autowired
    private ProductRepository productRepository;

    public ProductEntity uploadImage(Long productId, MultipartFile imageFile) throws IImageException {
        Optional<ProductEntity> productOpt = productRepository.findById(productId);
        IImageException.validateProductExists(productOpt, productId);

        ProductEntity product = productOpt.get();
        try {
            byte[] imageData = imageFile.getBytes();
            product.setImage(imageData);
            productRepository.save(product);
        } catch (IOException e) {
            throw new IImageException("Error al subir la imagen" + e);
        }

        return product;
    }

    public ProductEntity updateImage(Long productId, MultipartFile imageFile) throws IImageException {
        return uploadImage(productId, imageFile);
    }

    }



