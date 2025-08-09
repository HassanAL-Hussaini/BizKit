package org.example.bizkit.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Model.Product;
import org.example.bizkit.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ===================== READ =====================
    // Public - get all products
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get all products for a specific provider/company
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<?> getProductsByProviderId(@PathVariable Integer providerId) {
        return ResponseEntity.ok(productService.getProductsByProviderId(providerId));
    }


    // ===================== CREATE =====================
    // Create a new product (provider must be active)
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        productService.addProduct(product);
        return ResponseEntity.status(201).body(new ApiResponse("Product added successfully"));
    }

    // ===================== UPDATE =====================
    // Update an existing product by id
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId,
                                           @Valid @RequestBody Product newProduct,
                                           Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        productService.updateProduct(productId, newProduct);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully"));
    }

    // ===================== DELETE =====================
    // Delete a product by id
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully"));
    }
}

