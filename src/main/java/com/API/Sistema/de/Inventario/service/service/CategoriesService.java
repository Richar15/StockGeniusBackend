package com.API.Sistema.de.Inventario.service.service;

import com.API.Sistema.de.Inventario.persistence.entity.CategoriesEntity;
import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.CategoriesRepository;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private ProductRepository productRepository;

    public CategoriesEntity createCategory(CategoriesEntity categoriesEntity) {
        if (categoriesRepository.existsByName(categoriesEntity.getName())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }
        return categoriesRepository.save(categoriesEntity);
    }

    public CategoriesEntity updateCategory(Long id, CategoriesEntity categoriesEntity) {
        CategoriesEntity categoryExist = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!categoryExist.getName().equals(categoriesEntity.getName()) &&
                categoriesRepository.existsByName(categoriesEntity.getName())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }
        categoryExist.setName(categoriesEntity.getName());
        return categoriesRepository.save(categoryExist);
    }
    public String deleteCategory(Long id) {
        CategoriesEntity categoriesEntity = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        for (ProductEntity productEntity: categoriesEntity.getProducts()) {
            productEntity.setCategory(null);
            productRepository.save(productEntity);
        }

        categoriesRepository.delete(categoriesEntity);
        return "Categoría eliminada correctamente";
    }

    public CategoriesEntity getCategory(Long id) {
        return categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public List<CategoriesEntity> listCategory() {
        List<CategoriesEntity> categories = categoriesRepository.findAll();
        if (categories.isEmpty()) {
            throw new RuntimeException("No hay categorías registradas en el sistema");
        }
        return categories;

    }
    public String addProductToCategory(Long productId, Long categoryId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CategoriesEntity categoriesEntity = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        productEntity.setCategory(categoriesEntity);
        productRepository.save(productEntity);
        return "Producto agregado a la categoría correctamente";
    }
    public String removeProductOfCategory(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productEntity.setCategory(null);
        productRepository.save(productEntity);
        return "Producto removido de la categoría correctamente";
    }

}