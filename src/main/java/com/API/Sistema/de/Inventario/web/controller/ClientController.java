package com.API.Sistema.de.Inventario.web.controller;

import com.API.Sistema.de.Inventario.persistence.entity.ClientEntity;
import com.API.Sistema.de.Inventario.persistence.repository.ClientRepository;
import com.API.Sistema.de.Inventario.service.exception.ClientServiceException;
import com.API.Sistema.de.Inventario.service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    public ClientService clientService;


    @PostMapping("/createClient")
    public ResponseEntity<ClientEntity> createClient(@RequestBody ClientEntity client) {
        ClientServiceException.validateClient(client, clientRepository);
        return ResponseEntity.ok(ClientServiceException.handleSaveException(clientRepository, client));
    }

    @PutMapping("/updateClient/{id}")
    public ResponseEntity<ClientEntity> updateClient(@PathVariable Long id, @RequestBody ClientEntity client) {
        Optional<ClientEntity> existingClient = clientRepository.findById(id);
        if (!existingClient.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        client.setId(id);
        ClientServiceException.validateClient(client, clientRepository);
        return ResponseEntity.ok(ClientServiceException.handleSaveException(clientRepository, client));
    }


    @DeleteMapping("/deleteClient/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            clientService.deleteById(id);
            return new ResponseEntity<>("Cliente eliminado exitosamente.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el cliente.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long id) {
        Optional<ClientEntity> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client.get());
    }


    @GetMapping("/searchClient/{name}")
    public ResponseEntity<List<ClientEntity>> searchClientByName(@PathVariable String name) throws ClientServiceException {
        List<ClientEntity> clients = clientService.searchClientByName(name);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/listOfClients")
    public ResponseEntity<List<ClientEntity>> findAll() throws ClientServiceException {
        List<ClientEntity> clients = clientService.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

}
