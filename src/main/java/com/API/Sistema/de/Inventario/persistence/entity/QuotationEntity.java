package com.API.Sistema.de.Inventario.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "quotations")
public class QuotationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<ProductDetail> productDetails;

    private int totalPrice;

    @Embeddable
    @Getter
    @Setter
    public static class ProductDetail {
        private String name;
        private int quantity;
        private int pricePerUnit;
        private int totalPrice;
    }
}
