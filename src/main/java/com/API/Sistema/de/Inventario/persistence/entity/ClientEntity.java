package com.API.Sistema.de.Inventario.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "name")
    private String name;

    @JoinColumn(name = "last_name")
    private String lastName;

    @JoinColumn(name = "phone")
    private String phone;

    @JoinColumn(name = "email")
    private String direction;


}
