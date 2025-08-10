package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Admin;
import org.example.bizkit.Model.OrderItem;
import org.example.bizkit.Model.Product;
import org.example.bizkit.Repository.OrderItemRepository;
import org.example.bizkit.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final AdminService adminService;
    // شلّنا OrderService
    private final ProductService productService;
    // لو تحتاج تتأكد أن orderId صالح بدون استدعاء OrderService
    private final OrderRepository orderRepository;

    // ===================== READ =====================
    public List<?> getAllOrderItems(Integer adminId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        return orderItemRepository.findAll();
    }

    // ===================== CREATE =====================
    protected void addOrderItem(Integer orderId, Integer productId, Integer quantity) {
        if (!orderRepository.existsById(orderId)) {
            throw new ApiException("Order not found");
        }

        Product product = productService.getProductByIdAndCheckIfExist(productId);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setProductId(product.getId());
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        orderItemRepository.save(orderItem);
    }

    // ===================== UPDATE =====================
    public void updateOrderItem(Integer adminId, Integer orderItemIdUpdated, OrderItem newOrderItem) {
        adminService.getAdminByIdAndCheckIfExist(adminId);

        OrderItem old = getOrderItemByIdAndCheckIfExist(orderItemIdUpdated);

        if (!orderRepository.existsById(newOrderItem.getOrderId())) {
            throw new ApiException("Order not found");
        }
        productService.getProductByIdAndCheckIfExist(newOrderItem.getProductId());

        old.setOrderId(newOrderItem.getOrderId());
        old.setProductId(newOrderItem.getProductId());
        old.setQuantity(newOrderItem.getQuantity());
        old.setPrice(newOrderItem.getPrice());
        orderItemRepository.save(old);
    }

    // ===================== DELETE =====================
    public void deleteOrderItem(Integer adminId, Integer orderItemIdDeleted) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
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
