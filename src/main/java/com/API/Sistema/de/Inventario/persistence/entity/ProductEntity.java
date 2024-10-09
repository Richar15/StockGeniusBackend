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

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "description")
    private String description;

    @JoinColumn(name = "price")
    private int price;

    @JoinColumn(name = "amount")
    private int amount;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;


}
