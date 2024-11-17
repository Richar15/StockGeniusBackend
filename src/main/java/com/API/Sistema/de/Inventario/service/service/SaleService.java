package com.API.Sistema.de.Inventario.service.service;

import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.entity.SaleEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ClientRepository;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import com.API.Sistema.de.Inventario.persistence.repository.SaleRepository;
import com.API.Sistema.de.Inventario.service.exception.NoProductsSoldException;
import com.API.Sistema.de.Inventario.service.exception.PartialPeriodException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static java.lang.String.valueOf;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;


    public void createSale(SaleEntity sale) {
        sale.setDate(LocalDate.now());

        Optional<ClientEntity> client = clientRepository.findByName(sale.getClient().getName());
        if (client.isPresent()) {
            sale.setClient(client.get());
            sale.setClientName(client.get().getName());
        } else {
            throw new RuntimeException("Cliente no encontrado");
        }

        List<ProductEntity> products = sale.getProducts();
        int total = 0;
        int totalAmount = 0;
        List<String> productNamesList = products.stream().map(ProductEntity::getName).collect(Collectors.toList());
        String productNames = String.join(", ", productNamesList);
        for (ProductEntity saleProduct : products) {
            Optional<ProductEntity> product = productRepository.findByName(saleProduct.getName());
            if (product.isPresent()) {
                ProductEntity productEntity = product.get();
                int quantity = saleProduct.getAmount();
                if (productEntity.getAmount() < quantity) {
                    throw new RuntimeException("Cantidad insuficiente para el producto: " + productEntity.getName());
                }
                saleProduct.setPrice(productEntity.getPrice()); // Incluir el precio por unidad en la respuesta
                total += productEntity.getPrice() * quantity;
                totalAmount += quantity;
                productEntity.setAmount(productEntity.getAmount() - quantity);
                productRepository.save(productEntity);
            } else {
                throw new RuntimeException("Producto no encontrado");
            }
        }
        sale.setPriceTotal(total);
        sale.setTotalAmount(totalAmount);
        sale.setProductName(productNames);
        saleRepository.save(sale);

        generateSalePDF(sale);
    }

    private void generateSalePDF(SaleEntity sale) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Venta_" + sale.getId() + ".pdf"));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Comprobante de Venta ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            document.add(new Paragraph("Fecha de Venta: " + sale.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            document.add(new Paragraph("Cliente: " + sale.getClient().getName() + " " + sale.getClient().getLastName(), normalFont));
            document.add(new Paragraph("Teléfono: " + sale.getClient().getPhone(), normalFont));
            document.add(new Paragraph("Dirección: " + sale.getClient().getDirection(), normalFont));
            document.add(new Paragraph("Gmail: " + sale.getClient().getGmail(), normalFont));
            document.add(new Paragraph("ID de Venta: " + sale.getId(), normalFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Stream.of("Producto", "Cantidad", "Precio Unitario")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                        table.addCell(header);
                    });

            for (ProductEntity product : sale.getProducts()) {
                table.addCell(product.getName());
                table.addCell(valueOf(product.getAmount()));
                table.addCell(valueOf(product.getPrice()));
            }
            document.add(table);

            Paragraph total = new Paragraph("Total: $" + sale.getPriceTotal(), titleFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public Map<String, Object> getSalesByDay(LocalDate date) {
        List<SaleEntity> sales = saleRepository.findAllByDate(date);
        int totalPrice = sales.stream().mapToInt(SaleEntity::getPriceTotal).sum();
        Map<String, Object> response = new HashMap<>();
        response.put("sales", sales);
        response.put("Las ventas Generadas  hoy Han Sido de: ", totalPrice);
        return response;
    }

    public Map<String, Object> getSalesByWeek() {
        LocalDate startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        LocalDate currentDate = LocalDate.now();

        List<SaleEntity> sales = saleRepository.findAllByDateBetween(startOfWeek, endOfWeek);
        int totalPrice = sales.stream().mapToInt(SaleEntity::getPriceTotal).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("sales", sales);
        response.put("Las ventas Generadas Esta Semana Han Sido de: ", totalPrice);

        if (currentDate.isBefore(endOfWeek)) {
            response.put("message", "Aún no ha transcurrido una semana completa, pero estas son las ventas hasta el momento.");
        } else {
            response.put("message", "Ventas de la semana completa.");
        }

        return response;
    }

    public Map<String, Object> getSalesByMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        LocalDate currentDate = LocalDate.now();

        List<SaleEntity> sales = saleRepository.findAllByDateBetween(startOfMonth, endOfMonth);
        int totalPrice = sales.stream().mapToInt(SaleEntity::getPriceTotal).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("sales", sales);
        response.put("Las ventas Generadas De este Mes Han Sido de: ", totalPrice);

        if (currentDate.isBefore(endOfMonth)) {
            response.put("message", "Aún no ha transcurrido un mes completo, pero estas son las ventas hasta el momento.");
        } else {
            response.put("message", "Ventas del mes completo.");
        }

        return response;
    }

}