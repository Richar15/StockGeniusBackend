package com.API.Sistema.de.Inventario.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "admin")
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_name")
    private String userName;

    @JoinColumn(name = "password" )
    private String password;

    @Column(name = "email")
    private String email;
}
