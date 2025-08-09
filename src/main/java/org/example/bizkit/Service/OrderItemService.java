package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Admin;
import org.example.bizkit.Model.OrderItem;
import org.example.bizkit.Repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final AdminService adminService;

    // ===================== READ =====================
    // Admin only - get all order items
    public List<?> getAllOrderItems(Integer adminId) {
        Admin admin = adminService.getAdminByIdAndCheckIfExist(adminId);
        return orderItemRepository.findAll();
    }

    // ===================== CREATE =====================
    // Admin only - create order item
    public void addOrderItem(Integer adminId, OrderItem orderItem) {
        Admin admin = adminService.getAdminByIdAndCheckIfExist(adminId);
        orderItemRepository.save(orderItem);
    }

    // ===================== UPDATE =====================
    // Admin only - update order item by id
    public void updateOrderItem(Integer adminId, Integer orderItemIdUpdated, OrderItem newOrderItem) {
        Admin admin = adminService.getAdminByIdAndCheckIfExist(adminId);
        OrderItem old = getOrderItemByIdAndCheckIfExist(orderItemIdUpdated);
        // adjust fields as per your OrderItem model
        old.setOrderId(newOrderItem.getOrderId());
        old.setProductId(newOrderItem.getProductId());
        old.setQuantity(newOrderItem.getQuantity());
        old.setPrice(newOrderItem.getPrice());
        orderItemRepository.save(old);
    }

    // ===================== DELETE =====================
    // Admin only - delete order item by id
    public void deleteOrderItem(Integer adminId, Integer orderItemIdDeleted) {
        Admin admin = adminService.getAdminByIdAndCheckIfExist(adminId);
        OrderItem item = orderItemRepository.findOrderItemById(orderItemIdDeleted);
        if (item == null) throw new ApiException("Orders Item Not Found");
        orderItemRepository.delete(item);
    }

    // ===================== INTERNAL HELPERS =====================
    protected OrderItem getOrderItemByIdAndCheckIfExist(Integer id) {
        OrderItem item = orderItemRepository.findOrderItemById(id);
        if (item == null) throw new ApiException("Orders Item Not Found");
        return item;
    }
}
