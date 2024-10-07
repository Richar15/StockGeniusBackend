package com.API.Sistema.de.Inventario.web.controller;
import com.API.Sistema.de.Inventario.persistence.entity.SaleEntity;
import com.API.Sistema.de.Inventario.service.exception.PartialPeriodException;
import com.API.Sistema.de.Inventario.service.implementation.ISale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private ISale iSale;

    @PostMapping("/createSale")
    public SaleEntity createSale(@RequestBody SaleEntity sale) {
        iSale.createSale(sale);
        return sale;
    }

    @GetMapping("/day")
    public List<SaleEntity> getSalesByDay(@RequestParam("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return iSale.getSalesByDay(localDate);
    }
    @GetMapping("/week")
    public Map<String, Object> getSalesByWeek(@RequestParam("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        Map<String, Object> response = new HashMap<>();
        try {
            List<SaleEntity> sales = iSale.getSalesByWeek(localDate);
            response.put("message", "Ventas de la semana completa.");
            response.put("sales", sales);
        } catch (PartialPeriodException e) {
            response.put("message", e.getMessage());
            response.put("sales", e.getSales());
        }
        return response;
    }

    @GetMapping("/month")
    public Map<String, Object> getSalesByMonth(@RequestParam("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        Map<String, Object> response = new HashMap<>();
        try {
            List<SaleEntity> sales = iSale.getSalesByMonth(localDate);
            response.put("message", "Ventas del mes completo.");
            response.put("sales", sales);
        } catch (PartialPeriodException e) {
            response.put("message", e.getMessage());
            response.put("sales", e.getSales());
        }
        return response;
    }

}
