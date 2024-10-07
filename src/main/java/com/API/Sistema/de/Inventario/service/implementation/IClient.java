package com.API.Sistema.de.Inventario.service.implementation;

import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ClientRepository;
import com.API.Sistema.de.Inventario.service.exception.IClientException;
import com.API.Sistema.de.Inventario.service.exception.IProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class IClient {

    @Autowired
    private ClientRepository clientRepository;

    public ClientEntity saveOrUpdateClient(ClientEntity client) {
        IClientException.validateClient(client, clientRepository);
        return IClientException.handleSaveException(clientRepository, client);
    }

    public void deleteByName(String name) {
        IClientException.validateName(name);
        Optional<ClientEntity> client = clientRepository.findByName(name);
        IClientException.validateClientExists(client, name);
        clientRepository.delete(client.get());
    }

    public List<ClientEntity> searchClientByName(String name) throws IClientException {
        List<ClientEntity> clients = clientRepository.findByNameContainingIgnoreCase(name);
        Optional<ClientEntity> client = clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
        IClientException.validateClientExists(client, name);
        return clients;
    }
    public List<ClientEntity> findAll() throws IClientException {

        List<ClientEntity> clients = clientRepository.findAll();
        IClientException.validateClientList(clients);
        return clients;
    }

}
