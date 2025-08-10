package org.example.bizkit.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //================== REQUEST ORDER ==================
    @PostMapping("/request/{clientId}/{productId}/{quantity}")
    public ResponseEntity<?> requestOrder(@PathVariable Integer clientId,
                                          @PathVariable Integer productId,
                                          @PathVariable Integer quantity) {
        orderService.requestOrder(clientId, productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Order requested successfully"));
    }

    //================== CANCEL ORDER ==================
    @PutMapping("/cancel/{clientId}/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Integer clientId,
                                         @PathVariable Integer orderId) {
        orderService.cancelOrder(clientId, orderId);
        return ResponseEntity.ok(new ApiResponse("Order cancelled successfully"));
    }

    //================== COMPLETE ORDER ==================
    @PutMapping("/complete/{providerId}/{orderId}")
    public ResponseEntity<?> completeOrder(@PathVariable Integer providerId,
                                           @PathVariable Integer orderId) {
        orderService.completeOrder(providerId, orderId);
        return ResponseEntity.ok(new ApiResponse("Order completed successfully"));
    }
    // ==================ProviderInfoDto Rejects Order ==================
    @PutMapping("/reject/{providerId}/{orderId}")
    public ResponseEntity<?> rejectOrderByProvider(@PathVariable Integer providerId,
                                                   @PathVariable Integer orderId) {
        orderService.rejectOrderByProvider(providerId, orderId);
        return ResponseEntity.ok(new ApiResponse("Order rejected by provider successfully"));
    }
    //================== UPDATE QUANTITY ==================
    @PutMapping("/update-quantity/{orderItemId}/{delta}")
    public ResponseEntity<?> updateOrderItemQuantity(@PathVariable Integer orderItemId,
                                                     @PathVariable Integer delta) {
        orderService.updateOrderItemQuantity(orderItemId, delta);
        return ResponseEntity.ok(new ApiResponse("Order quantity updated successfully"));
    }

    //================== GET ORDER BY ID ==================
    @GetMapping("/get/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    //================== GET ORDERS BY CLIENT ==================
    @GetMapping("/get-by-client/{clientId}")
    public ResponseEntity<?> getOrdersByClient(@PathVariable Integer clientId) {
        return ResponseEntity.ok(orderService.getOrdersByClient(clientId));
    }

    //================== GET ORDERS BY PROVIDER ==================
    @GetMapping("/get-by-provider/{providerId}")
    public ResponseEntity<?> getOrdersByProvider(@PathVariable Integer providerId) {
        return ResponseEntity.ok(orderService.getOrdersByProvider(providerId));
    }


}
