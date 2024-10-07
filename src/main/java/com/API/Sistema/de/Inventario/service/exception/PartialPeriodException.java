package com.API.Sistema.de.Inventario.service.exception;

import com.API.Sistema.de.Inventario.persistence.entity.SaleEntity;

import java.util.List;

public class PartialPeriodException  extends RuntimeException{
    private List<SaleEntity> sales;

    public PartialPeriodException(String message, List<SaleEntity> sales) {
        super(message);
        this.sales = sales;
    }

    public List<SaleEntity> getSales() {
        return sales;
    }

}
