package com.API.Sistema.de.Inventario.persistence.repository;

import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {


    ClientEntity save(ClientEntity client);

    Optional<ClientEntity> findByName(String name);

    void deleteByName(String name);

    List<ClientEntity> findByNameContainingIgnoreCase(String name);

    List<ClientEntity> findAll();

    List<ClientEntity> findByLastName(String lastName);

    List<ClientEntity> findByPhone(String phone);
}
