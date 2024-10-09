package com.API.Sistema.de.Inventario.service.implementation;

import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.entity.QuotationEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ProductRepository;
import com.API.Sistema.de.Inventario.persistence.repository.QuotationRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class IQuotation {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    public String createQuotation(List<ProductEntity> selectedProducts) {
        int total = 0;
        List<QuotationEntity.ProductDetail> productDetails = new ArrayList<>();
        StringBuilder quotationMessage = new StringBuilder();

        for (ProductEntity selectedProduct : selectedProducts) {
            Optional<ProductEntity> product = productRepository.findByName(selectedProduct.getName());
            if (product.isPresent()) {
                ProductEntity productEntity = product.get();
                int quantity = selectedProduct.getAmount();
                int price = productEntity.getPrice() * quantity;
                total += price;

                QuotationEntity.ProductDetail detail = new QuotationEntity.ProductDetail();
                detail.setName(productEntity.getName());
                detail.setQuantity(quantity);
                detail.setPricePerUnit(productEntity.getPrice());
                detail.setTotalPrice(price);
                productDetails.add(detail);

                quotationMessage.append("Producto: ").append(productEntity.getName())
                        .append(", Cantidad: ").append(quantity)
                        .append(", Precio por unidad: ").append(productEntity.getPrice())
                        .append(", Precio total: ").append(price).append("\n");
            } else {
                throw new RuntimeException("Producto no encontrado: " + selectedProduct.getName());
            }
        }

        QuotationEntity quotation = new QuotationEntity();
        quotation.setProductDetails(productDetails);
        quotation.setTotalPrice(total);
        quotationRepository.save(quotation);

        quotationMessage.append("El precio total es: ").append(total);

        // Generar PDF
        generatePDF(quotation);

        return quotationMessage.toString();
    }

    private void generatePDF(QuotationEntity quotation) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Cotizacion.pdf"));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Cotización", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Stream.of("Producto", "Cantidad", "Precio por unidad", "Precio total")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle, headerFont));
                        table.addCell(header);
                    });

            for (QuotationEntity.ProductDetail detail : quotation.getProductDetails()) {
                table.addCell(detail.getName());
                table.addCell(String.valueOf(detail.getQuantity()));
                table.addCell(String.valueOf(detail.getPricePerUnit()));
                table.addCell(String.valueOf(detail.getTotalPrice()));
            }

            document.add(table);

            Paragraph total = new Paragraph("Total: " + quotation.getTotalPrice(), titleFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

}
