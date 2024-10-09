package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.service.exception.ImageServiceException;
import com.API.Sistema.de.Inventario.service.exception.ProductServiceException;
import com.API.Sistema.de.Inventario.service.service.ImageService;
import com.API.Sistema.de.Inventario.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")

public class ProductController {

    @Autowired
    private ProductService productService;


    @Autowired
    private ImageService imageService;

    public ProductController(ProductService iProduct) {
        this.productService = iProduct;
    }

    @PostMapping("/createProduct")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        ProductEntity savedProduct = productService.saveOrUpdateProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody ProductEntity product) {
        product.setId(id);
        ProductEntity updatedProduct = productService.saveOrUpdateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable String name) throws ProductServiceException {
        productService.deleteProductByName(name);
        return new ResponseEntity<>("Producto eliminado exitosamente.", HttpStatus.OK);
    }

    @GetMapping("/searchProduct/{name}")
    public ResponseEntity<List<ProductEntity>> searchProductsByName(@PathVariable String name) throws ProductServiceException {
        List<ProductEntity> products = productService.searchProductsByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/listOfProducts")
    public ResponseEntity<List<ProductEntity>> findAll() throws ProductServiceException {
        List<ProductEntity> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @PostMapping("/{productId}/image")
    public ResponseEntity<ProductEntity> uploadImage(@PathVariable Long productId, @RequestParam("imageFile") MultipartFile imageFile) throws ImageServiceException {
        ProductEntity updatedProduct = imageService.uploadImage(productId, imageFile);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{productId}/image")
    public ResponseEntity<ProductEntity> updateImage(@PathVariable Long productId, @RequestParam ("imageFile")MultipartFile imageFile) throws ImageServiceException {
        ProductEntity updatedProduct = imageService.updateImage(productId, imageFile);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}









