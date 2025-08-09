package org.example.bizkit.Repository;

import org.example.bizkit.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client,Integer>{
    Client findClientById(Integer id);

    Client findByCompanyName(String companyName);

    List<Client> findAllByStatus(String status);
}
