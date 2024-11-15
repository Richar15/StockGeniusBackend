package com.API.Sistema.de.Inventario.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "amount")
    private int amount;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoriesEntity category;
}
