package org.example.bizkit.Repository;

import org.example.bizkit.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    OrderItem findOrderItemById(Integer id);
    OrderItem findOrderItemByOrderId(Integer orderId);
}
