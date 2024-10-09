package com.API.Sistema.de.Inventario.service.service;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import com.API.Sistema.de.Inventario.service.exception.ImageServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {


    @Autowired
    private ProductRepository productRepository;

    public ProductEntity uploadImage(Long productId, MultipartFile imageFile) throws ImageServiceException {
        Optional<ProductEntity> productOpt = productRepository.findById(productId);
        ImageServiceException.validateProductExists(productOpt, productId);

        ProductEntity product = productOpt.get();
        try {
            byte[] imageData = imageFile.getBytes();
            product.setImage(imageData);
            productRepository.save(product);
        } catch (IOException e) {
            throw new ImageServiceException("Error al subir la imagen" + e);
        }

        return product;
    }

    public ProductEntity updateImage(Long productId, MultipartFile imageFile) throws ImageServiceException {
        return uploadImage(productId, imageFile);
    }

    }



