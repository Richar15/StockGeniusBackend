package com.API.Sistema.de.Inventario.service.implementation;
import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.entity.SaleEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ClientRepository;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import com.API.Sistema.de.Inventario.persistence.repository.SaleRepository;
import com.API.Sistema.de.Inventario.service.exception.PartialPeriodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ISale {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    public void createSale(SaleEntity sale) {
        // Asignar la fecha actual
        sale.setDate(LocalDate.now());

        // Buscar y asignar el cliente existente
        Optional<ClientEntity> client = clientRepository.findByName(sale.getClient().getName());
        if (client.isPresent()) {
            sale.setClient(client.get());
            sale.setClientName(client.get().getName());
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }

        // Buscar y asignar los productos existentes, calcular el total y la cantidad
        List<ProductEntity> products = sale.getProducts();
        int total = 0;
        int totalAmount = 0;
        List<String> productNamesList = products.stream().map(ProductEntity::getName).collect(Collectors.toList());
        String productNames = String.join(", ", productNamesList);
        for (ProductEntity saleProduct : products) {
            Optional<ProductEntity> product = productRepository.findByName(saleProduct.getName());
            if (product.isPresent()) {
                ProductEntity productEntity = product.get();
                int quantity = saleProduct.getAmount(); // Cantidad comprada
                if (productEntity.getAmount() < quantity) {
                    throw new RuntimeException("Cantidad insuficiente para el producto: " + productEntity.getName());
                }
                total += productEntity.getPrice() * quantity;
                totalAmount += quantity;
                productEntity.setAmount(productEntity.getAmount() - quantity); // Disminuir la cantidad del producto
                productRepository.save(productEntity); // Guardar la nueva cantidad del producto
            } else {
                throw new RuntimeException("Producto no encontrado");
            }
        }
        sale.setPriceTotal(total);
        sale.setTotalAmount(totalAmount);
        sale.setProductName(productNames);

        saleRepository.save(sale);
    }

    public List<SaleEntity> getSalesByDay(LocalDate date) {
        return saleRepository.findAllByDate(date);
    }

    public List<SaleEntity> getSalesByWeek(LocalDate date) {
        LocalDate startOfWeek = date;
        LocalDate endOfWeek = date.plusDays(6);
        LocalDate currentDate = LocalDate.now();

        List<SaleEntity> sales = saleRepository.findAllByDateBetween(startOfWeek, endOfWeek);

        if (currentDate.isBefore(endOfWeek)) {
            throw new PartialPeriodException("Aún no ha transcurrido una semana completa, pero estas son las ventas hasta el momento.", sales);
        }

        return sales;
    }

    public List<SaleEntity> getSalesByMonth(LocalDate date) {
        LocalDate startOfMonth = date;
        LocalDate endOfMonth = date.plusDays(29);
        LocalDate currentDate = LocalDate.now();

        List<SaleEntity> sales = saleRepository.findAllByDateBetween(startOfMonth, endOfMonth);

        if (currentDate.isBefore(endOfMonth)) {
            throw new PartialPeriodException("Aún no ha transcurrido un mes completo, pero estas son las ventas hasta el momento.", sales);
        }

        return sales;
    }

}