package com.API.Sistema.de.Inventario.service.exception;

import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientServiceException extends RuntimeException {

    public ClientServiceException(String message) {
        super(message);
    }

    public static void validateClient(ClientEntity client, ClientRepository clientRepository) {
        if (client == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        if (client.getName() == null || client.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (client.getLastName() == null || client.getLastName().isEmpty()) {
            throw new IllegalArgumentException("El apellido del cliente es obligatorio");
        }
        if (client.getPhone() == null || client.getPhone().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del cliente es obligatorio");
        }
        if (client.getPhone().length() != 10) {
            throw new IllegalArgumentException("El teléfono debe tener 10 dígitos");
        }

        Optional<ClientEntity> existingClientWithSameName = clientRepository.findByName(client.getName());
        if (existingClientWithSameName.isPresent() && !existingClientWithSameName.get().getId().equals(client.getId())) {
            throw new IllegalArgumentException("Ya existe un cliente con ese nombre");
        }

        List<ClientEntity> existingClientsWithSameLastName = clientRepository.findByLastName(client.getLastName());
        for (ClientEntity existingClient : existingClientsWithSameLastName) {
            if (!existingClient.getId().equals(client.getId())) {
                throw new IllegalArgumentException("Ya existe un cliente con ese apellido");
            }
        }

        List<ClientEntity> existingClientsWithSamePhone = clientRepository.findByPhone(client.getPhone());
        for (ClientEntity existingClient : existingClientsWithSamePhone) {
            if (!existingClient.getId().equals(client.getId())) {
                throw new IllegalArgumentException("Ya existe un cliente con ese teléfono");
            }
        }
    }

    public static ClientEntity handleSaveException(ClientRepository clientRepository, ClientEntity client) {
        try {
            return clientRepository.save(client);
        } catch (Exception e) {
            throw new ClientServiceException("Error al guardar el cliente: " + e.getMessage());
        }
    }

    public static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
    }

    public static void validateClientExists(Optional<ClientEntity> client, String name) {
        if (!client.isPresent()) {
            throw new IllegalArgumentException("No existe un cliente con el nombre " + name);
        }
    }
    public static void validateClientList(List<ClientEntity> clients) throws ClientServiceException {
        if (clients == null || clients.isEmpty()) {
            throw new ClientServiceException("No fue posible traer los clientes.");
        }
    }

}

