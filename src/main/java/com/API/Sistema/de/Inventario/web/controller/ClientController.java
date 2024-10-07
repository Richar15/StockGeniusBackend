package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import com.API.Sistema.de.Inventario.persistence.entity.ProductEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ClientRepository;
import com.API.Sistema.de.Inventario.service.exception.IClientException;
import com.API.Sistema.de.Inventario.service.exception.IProductException;
import com.API.Sistema.de.Inventario.service.implementation.IClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    public IClient iclient;


    @PostMapping("/createClient")
    public ResponseEntity<ClientEntity> createClient(@RequestBody ClientEntity client) {
        IClientException.validateClient(client, clientRepository);
        return ResponseEntity.ok(IClientException.handleSaveException(clientRepository, client));
    }

    @PutMapping("/updateClient/{id}")
    public ResponseEntity<ClientEntity> updateClient(@PathVariable Long id, @RequestBody ClientEntity client) {
        client.setId(id);
        IClientException.validateClient(client, clientRepository);
        return ResponseEntity.ok(IClientException.handleSaveException(clientRepository, client));
    }

    @DeleteMapping("/deleteClient/{name}")
    public ResponseEntity<String> deleteByName(@PathVariable String name) throws IClientException {
        iclient.deleteByName(name);
        return new ResponseEntity<>("Cliente eliminado exitosamente.", HttpStatus.OK);
    }


    @GetMapping("/searchClient/{name}")
    public ResponseEntity<List<ClientEntity>> searchClientByName(@PathVariable String name) throws IClientException {
        List<ClientEntity> clients = iclient.searchClientByName(name);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/listOfClients")
    public ResponseEntity<List<ClientEntity>> findAll() throws IClientException {
        List<ClientEntity> clients = iclient.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

}
