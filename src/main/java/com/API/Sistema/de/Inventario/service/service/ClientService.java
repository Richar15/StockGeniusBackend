package com.API.Sistema.de.Inventario.service.service;

import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ClientRepository;
import com.API.Sistema.de.Inventario.service.exception.ClientServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientEntity saveOrUpdateClient(ClientEntity client) {
        ClientServiceException.validateClient(client, clientRepository);
        return ClientServiceException.handleSaveException(clientRepository, client);
    }

    public void deleteByName(String name) {
        ClientServiceException.validateName(name);
        Optional<ClientEntity> client = clientRepository.findByName(name);
        ClientServiceException.validateClientExists(client, name);
        clientRepository.delete(client.get());
    }

    public List<ClientEntity> searchClientByName(String name) throws ClientServiceException {
        List<ClientEntity> clients = clientRepository.findByNameContainingIgnoreCase(name);
        Optional<ClientEntity> client = clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
        ClientServiceException.validateClientExists(client, name);
        return clients;
    }
    public List<ClientEntity> findAll() throws ClientServiceException {

        List<ClientEntity> clients = clientRepository.findAll();
        ClientServiceException.validateClientList(clients);
        return clients;
    }

}
