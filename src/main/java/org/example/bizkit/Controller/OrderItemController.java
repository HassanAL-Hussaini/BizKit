package org.example.bizkit.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Model.OrderItem;
import org.example.bizkit.Service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    // ===================== READ =====================
    @GetMapping("/get-all/{adminId}")
    public ResponseEntity<?> getAllOrderItems(@PathVariable Integer adminId) {
        return ResponseEntity.ok(orderItemService.getAllOrderItems(adminId));
    }

    // ===================== CREATE =====================
    //No create endpoint


    // ===================== UPDATE =====================
    //No Update endpoint

    // ===================== DELETE =====================
    @DeleteMapping("/delete/{adminId}/{orderItemId}")
    public ResponseEntity<?> deleteOrderItem(@PathVariable Integer adminId,
                                             @PathVariable Integer orderItemId) {
        orderItemService.deleteOrderItem(adminId, orderItemId);
        return ResponseEntity.ok(new ApiResponse("Orders item deleted successfully"));
    }
}
