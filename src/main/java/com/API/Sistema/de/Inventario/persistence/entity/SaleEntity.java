package com.API.Sistema.de.Inventario.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "sales")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "date")
    private LocalDate date;

    @Column(name = "price_total")
    private int PriceTotal;

    @Column(name = "total_amount")
    private int totalAmount;

    @OneToMany
    @JoinColumn(name = "products")
    private List<ProductEntity> products;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @Column(name = "client_name")
    private String clientName;


    @Column(name = "product_name")
    private String productName;
}

