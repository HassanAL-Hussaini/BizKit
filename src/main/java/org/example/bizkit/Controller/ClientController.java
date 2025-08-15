package org.example.bizkit.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Model.Client;
import org.example.bizkit.Service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // ===================== READ =====================

    // Admin only - get all clients
    @GetMapping("/get-all/{adminId}")
    public ResponseEntity<?> getAllClients(@PathVariable Integer adminId) {
        return ResponseEntity.ok(clientService.getAllClients(adminId));
    }

    // Admin only - get active clients
    @GetMapping("/active/{adminId}")
    public ResponseEntity<?> getActiveClients(@PathVariable Integer adminId) {
        return ResponseEntity.ok(clientService.getActiveClients(adminId));
    }

    // Admin only - get unactive clients
    @GetMapping("/unactive/{adminId}")
    public ResponseEntity<?> getUnactiveClients(@PathVariable Integer adminId) {
        return ResponseEntity.ok(clientService.getUnactiveClients(adminId));
    }

    // ===================== CREATE =====================

    // Public - register a new client
    @PostMapping("/add")
    public ResponseEntity<?> addClient(@Valid @RequestBody Client client) {

        clientService.addClient(client);
        return ResponseEntity.status(201).body(new ApiResponse("Client added successfully"));
    }

    // ===================== UPDATE =====================

    // Admin only - block client (set status to unactive)
    @PutMapping("/block/{adminId}/{clientId}")
    public ResponseEntity<?> blockClient(@PathVariable Integer adminId,
                                         @PathVariable Integer clientId) {
        clientService.blockClient(adminId, clientId);
        return ResponseEntity.ok(new ApiResponse("Client blocked successfully"));
    }

    // Admin only - unblock client (set status to active)
    @PutMapping("/unblock/{adminId}/{clientId}")
    public ResponseEntity<?> unblockClient(@PathVariable Integer adminId,
                                           @PathVariable Integer clientId) {
        clientService.unblockClient(adminId, clientId);
        return ResponseEntity.ok(new ApiResponse("Client unblocked successfully"));
    }

    // Client - update own data (status not changed here)
    @PutMapping("/update/{clientId}")
    public ResponseEntity<?> updateClient(@PathVariable Integer clientId,
                                          @Valid @RequestBody Client newClient) {

        clientService.updateClient(clientId, newClient);
        return ResponseEntity.ok(new ApiResponse("Client updated successfully"));
    }

    // ===================== DELETE =====================

    // Admin only - delete client
    @DeleteMapping("/delete/{adminId}/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable Integer adminId,
                                          @PathVariable Integer clientId) {
        clientService.deleteClient(adminId, clientId);
        return ResponseEntity.ok(new ApiResponse("Client deleted successfully"));
    }
}
