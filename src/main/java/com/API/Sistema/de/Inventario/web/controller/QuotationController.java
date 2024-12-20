package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.service.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quotations")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @PostMapping("/createQuotation")
    public ResponseEntity<?> createQuotation(@RequestBody QuotationRequest quotationRequest) {
        try {
            String quotationMessage = quotationService.createQuotation(quotationRequest.getSelectedProducts(), quotationRequest.getClientName());

            File file = new File("Cotizacion.pdf");
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            ResponseEntity<InputStreamResource> response = ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(resource);

            return response;
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.badRequest().body("Error al crear la cotización: " + e.getMessage());
        }
    }

    public static class QuotationRequest {
        private String clientName;
        private List<ProductEntity> selectedProducts;

        // Getters and setters
        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public List<ProductEntity> getSelectedProducts() {
            return selectedProducts;
        }

        public void setSelectedProducts(List<ProductEntity> selectedProducts) {
            this.selectedProducts = selectedProducts;
        }
    }
}
