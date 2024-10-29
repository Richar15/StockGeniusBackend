package com.API.Sistema.de.Inventario.service.exception;

import java.util.Optional;

public class ImageServiceException extends RuntimeException {
  public ImageServiceException(String message) {
    super(message);
  }

  public static void validateProductExists(Optional<?> productOpt, Long productId) throws ImageServiceException {
    if (productOpt.isEmpty()) {
      throw new ImageServiceException("Producto no encontrado: " + productId + " no existe.");
    }
  }
}
