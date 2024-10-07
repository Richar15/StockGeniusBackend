package com.API.Sistema.de.Inventario.persistence.repository;

import com.API.Sistema.de.Inventario.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<SaleEntity, Long>{

    List<SaleEntity> findAllByDate(LocalDate date);
    List<SaleEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

}
