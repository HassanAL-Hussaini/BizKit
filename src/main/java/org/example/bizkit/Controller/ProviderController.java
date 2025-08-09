package org.example.bizkit.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Model.Provider;
import org.example.bizkit.Service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    // ===================== GET =====================

    // Admin only - get all providers
    @GetMapping("/get-all/{adminId}")
    public ResponseEntity<?> getAllProviders(@PathVariable Integer adminId) {
        return ResponseEntity.ok(providerService.getAllProviders(adminId));
    }

    // Admin only - get unactive providers (pending / not approved)
    @GetMapping("/unactive/{adminId}")
    public ResponseEntity<?> getUnactiveProviders(@PathVariable Integer adminId) {
        return ResponseEntity.ok(providerService.getUnactiveProviders(adminId));
    }

    // Public - active providers
    @GetMapping("/active")
    public ResponseEntity<?> getActiveProviders() {
        return ResponseEntity.ok(providerService.getActiveProviders());
    }

    // ===================== POST =====================

    // provider self-registers then goes to waitlist until admin make the provider active after that he can show his products to clients
    @PostMapping("/add/")
    public ResponseEntity<?> addProvider( @Valid @RequestBody Provider provider,Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        providerService.addProvider( provider);
        return ResponseEntity.status(201).body(new ApiResponse("Provider added successfully (pending activation)"));
    }

    // ===================== PUT =====================

    // Provider updates own data
    @PutMapping("/update/{providerId}")
    public ResponseEntity<?> updateProvider(@PathVariable Integer providerId,
                                            @Valid @RequestBody Provider newProvider,
                                            Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        providerService.updateProvider(providerId, newProvider);
        return ResponseEntity.ok(new ApiResponse("Provider updated successfully"));
    }

    // Admin activates provider (sets isActive = true)
    @PutMapping("/activate/{adminId}/{providerId}")
    public ResponseEntity<?> activateProvider(@PathVariable Integer adminId,
                                              @PathVariable Integer providerId) {
        providerService.UpdateProviderStateByAdmin(adminId, providerId);
        return ResponseEntity.ok(new ApiResponse("Provider activated successfully"));
    }

    // ===================== DELETE =====================

    // Delete provider (make sure service checks for orders in progress)
    @DeleteMapping("/delete/{providerId}")
    public ResponseEntity<?> deleteProvider(@PathVariable Integer providerId) {
        providerService.deleteProvider(providerId);
        return ResponseEntity.ok(new ApiResponse("Provider deleted successfully"));
    }
}

