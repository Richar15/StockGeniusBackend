package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.persistence.entity.CategoriesEntity;
import com.API.Sistema.de.Inventario.service.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @PostMapping("/createCategory")
    public ResponseEntity<CategoriesEntity> createCategory(@RequestBody CategoriesEntity categoriesEntity) {
        return ResponseEntity.ok(categoriesService.createCategory(categoriesEntity));
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<CategoriesEntity> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoriesEntity categoriesEntity) {
        return ResponseEntity.ok(categoriesService.updateCategory(id, categoriesEntity));
    }
    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoriesService.deleteCategory(id));
    }


    @GetMapping("/getCategory/{id}")
    public ResponseEntity<CategoriesEntity> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoriesService.getCategory(id));
    }

    @GetMapping("/listCategory")
    public ResponseEntity<?> listCategory() {
        try {
            List<CategoriesEntity> categories = categoriesService.listCategory();
            return ResponseEntity.ok(categories);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }


    @PostMapping("/{categoryId}/products/{productId}")
    public ResponseEntity<String> addProductToCategory(
            @PathVariable Long categoryId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(categoriesService.addProductToCategory(productId, categoryId));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> removeProductOfCategory(@PathVariable Long productId) {
        return ResponseEntity.ok(categoriesService.removeProductOfCategory(productId));
    }
}
