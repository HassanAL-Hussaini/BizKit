package org.example.bizkit.Repository;

import org.example.bizkit.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Integer> {
    Orders findOrdersById(Integer id);
    List<Orders> findOrdersByClientId(Integer clientId);
    List<Orders> findOrdersByProviderId(Integer providerId);
}
