package com.API.Sistema.de.Inventario.persistence.repository;

import com.API.Sistema.de.Inventario.persistence.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer>{


}
