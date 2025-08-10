package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.DTO.ClientInfoDto;
import org.example.bizkit.Model.Admin;
import org.example.bizkit.Model.Client;
import org.example.bizkit.Repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final AdminService adminService;

    // ===================== READ =====================
    public List<?> getAllClients(Integer adminId) {
        // only admin can see the clients info
        adminService.getAdminByIdAndCheckIfExist(adminId);

        List<Client> clients = clientRepository.findAll();
        ArrayList<ClientInfoDto> clientsDtoList = new ArrayList<>();
        for (Client client : clients) {
            clientsDtoList.add(new ClientInfoDto(client.getName(),client.getEmail(),
                                                 client.getPhone(),client.getCompanyName(),
                                                 client.getAddress()));
        }
        return clientsDtoList;
    }

    public List<?> getActiveClients(Integer adminId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);

        List<Client> clients = clientRepository.findAllByStatus("active");
        ArrayList<ClientInfoDto> clientsDtoList = new ArrayList<>();
        for (Client client : clients) {
            clientsDtoList.add(new ClientInfoDto(client.getName(),client.getEmail(),
                    client.getPhone(),client.getCompanyName(),
                    client.getAddress()));
        }
        return clientsDtoList;
    }

    public List<?> getUnactiveClients(Integer adminId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);

        List<Client> clients = clientRepository.findAllByStatus("unactive");
        ArrayList<ClientInfoDto> clientsDtoList = new ArrayList<>();
        for (Client client : clients) {
            clientsDtoList.add(new ClientInfoDto(client.getName(),client.getEmail(),
                    client.getPhone(),client.getCompanyName(),
                    client.getAddress()));
        }
        return clientsDtoList;
    }

    // ===================== CREATE =====================
    public void addClient(Client client) {
        // any Company can register to the website as a client = customer
        clientRepository.save(client);
    }

    // ===================== UPDATE =====================
    public void blockClient(Integer adminId, Integer clientId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        Client client = getClientByIdAndCheckIfExist(clientId);
        client.setStatus("unactive");
        clientRepository.save(client);
    }

    public void unblockClient(Integer adminId, Integer clientId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        Client client = getClientByIdAndCheckIfExist(clientId);
        client.setStatus("active");
        clientRepository.save(client);

    }

    public void updateClient(Integer clientIdUpdated, Client newClient) {
        Client oldClient = getClientByIdAndCheckIfExist(clientIdUpdated);
        oldClient.setName(newClient.getName());
        oldClient.setEmail(newClient.getEmail());
        oldClient.setPassword(newClient.getPassword());
        oldClient.setPhone(newClient.getPhone());
        oldClient.setCompanyName(newClient.getCompanyName());
        oldClient.setAddress(newClient.getAddress());
//        oldClient.setStatus(newClient.getStatus());
//        status will never change by client only admin can change the status
        clientRepository.save(oldClient);
    }

    // ===================== DELETE =====================
    public void deleteClient(Integer adminId, Integer clientIdDeleted) {
        // only admins can delete accounts
        adminService.getAdminByIdAndCheckIfExist(adminId);
        Client clientDeleted = clientRepository.findClientById(clientIdDeleted);
        if (clientDeleted == null) {
            throw new ApiException("the Client Not Found");
        }
        clientRepository.delete(clientDeleted);
    }

    // ===================== INTERNAL HELPERS =====================
    protected Client getClientByIdAndCheckIfExist(Integer id) {
        Client client = clientRepository.findClientById(id);
        if (client == null) {
            throw new ApiException("the Client Not Found");
        }
        return client;
    }
}
