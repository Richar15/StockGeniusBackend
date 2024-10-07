package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.service.exception.IImageException;
import com.API.Sistema.de.Inventario.service.exception.IProductException;
import com.API.Sistema.de.Inventario.service.implementation.IImage;
import com.API.Sistema.de.Inventario.service.implementation.IProduct;
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
    private IProduct iproduct;


    @Autowired
    private IImage iImage;

    public ProductController(IProduct iProduct) {
        this.iproduct = iProduct;
    }

    @PostMapping("/createProduct")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product) {
        ProductEntity savedProduct = iproduct.saveOrUpdateProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody ProductEntity product) {
        product.setId(id);
        ProductEntity updatedProduct = iproduct.saveOrUpdateProduct(product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct/{name}")
    public ResponseEntity<String> deleteProduct(@PathVariable String name) throws IProductException {
        iproduct.deleteProductByName(name);
        return new ResponseEntity<>("Producto eliminado exitosamente.", HttpStatus.OK);
    }

    @GetMapping("/searchProduct/{name}")
    public ResponseEntity<List<ProductEntity>> searchProductsByName(@PathVariable String name) throws IProductException {
        List<ProductEntity> products = iproduct.searchProductsByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/listOfProducts")
    public ResponseEntity<List<ProductEntity>> findAll() throws IProductException {
        List<ProductEntity> products = iproduct.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @PostMapping("/{productId}/image")
    public ResponseEntity<ProductEntity> uploadImage(@PathVariable Long productId, @RequestParam("imageFile") MultipartFile imageFile) throws IImageException {
        ProductEntity updatedProduct = iImage.uploadImage(productId, imageFile);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/{productId}/image")
    public ResponseEntity<ProductEntity> updateImage(@PathVariable Long productId, @RequestParam ("imageFile")MultipartFile imageFile) throws IImageException {
        ProductEntity updatedProduct = iImage.updateImage(productId, imageFile);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}









