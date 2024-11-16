package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.persistence.entity.SaleEntity;
import com.API.Sistema.de.Inventario.service.exception.PartialPeriodException;
import com.API.Sistema.de.Inventario.service.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/createSale")
    public ResponseEntity<?> createSale(@RequestBody SaleEntity sale) {
        try {
            saleService.createSale(sale);


            File file = new File("Venta_" + sale.getId() + ".pdf");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.badRequest().body("Error al crear la venta: " + e.getMessage());
        }
    }

    @GetMapping("/getSaleByday")
    public ResponseEntity<Map<String, Object>> getSalesByDay(@RequestParam("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        Map<String, Object> response = saleService.getSalesByDay(localDate);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getSaleByweek")
    public ResponseEntity<Map<String, Object>> getSalesByWeek() {
        Map<String, Object> response = saleService.getSalesByWeek();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getSaleBymonth")
    public ResponseEntity<Map<String, Object>> getSalesByMonth() {
        Map<String, Object> response = saleService.getSalesByMonth();
        return ResponseEntity.ok(response);
    }

}
